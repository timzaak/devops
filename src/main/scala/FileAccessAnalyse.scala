import com.timzaak.devops.alicloud.{CDNClient, CDNLogEntry}
import com.timzaak.devops.wechat.{WeChatWorkWebhook, MarkdownMessage}
import com.typesafe.config.ConfigFactory
import io.circe.config.syntax.*
import io.circe.generic.auto.*
import better.files.*
import sttp.client4.*
import org.flywaydb.core.Flyway
import scalasql.sqlite.SqliteDialect
import scalasql.query.*
import scalasql.Table

import java.sql.DriverManager
import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter
import scala.collection.parallel.CollectionConverters.*
import scala.util.{Success, Failure}

case class FileAccessLog(
    id: Int,
    logDate: String,
    filePath: String,
    accessCount: Int
)

object FileAccessLog extends Table[FileAccessLog] {
  override def tableName = "file_access_log"
}


object FileAccessAnalyse {

  case class CDNConfig(
    accessKeyId: String,
    accessKeySecret: String,
    regionId: String,
    logDir: String
  )

  case class DbConfig(
      path: String
  )

  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.load()
    val cdnConfig = conf.getConfig("alicloud.cdn").as[CDNConfig].toTry.get
    val dbConfig = conf.getConfig("db.sqlite").as[DbConfig].toTry.get

    // 1. Initialize and migrate database
    val dbUrl = "jdbc:sqlite:" + dbConfig.path

    // Ensure the directory for the database file exists
    File(dbConfig.path).parent.createDirectories()

    val flyway = Flyway.configure().dataSource(dbUrl, null, null).load()
    flyway.migrate()

    // Initialize scalasql context
    implicit val db: scalasql.Db = new scalasql.Db(DriverManager.getConnection(dbUrl))
    db.run(scalasql.Sql.raw("PRAGMA journal_mode=WAL;"))
    db.run(scalasql.Sql.raw("PRAGMA synchronous=NORMAL;"))


    // Initialize HTTP backend
    val backend: SyncBackend = DefaultSyncBackend()

    val cdnClient = CDNClient(
      accessKeyId = cdnConfig.accessKeyId,
      accessKeySecret = cdnConfig.accessKeySecret,
      regionId = cdnConfig.regionId,
      backend = backend
    )

    val wechatWebhook = Option(conf.getString("webhook.weChatWork")).map(url => WeChatWorkWebhook(url)(using backend))

    val yesterday = LocalDate.now().minusDays(1)

    // Process logs for each domain
    args.foreach { domain =>
      println(s"Processing domain: $domain")
      val logDir = downloadLogs(cdnClient, domain, cdnConfig.logDir, yesterday)
      val accessCounts = analyzeLogs(cdnClient, logDir)
      if (accessCounts.nonEmpty) {
        saveAccessCounts(accessCounts, yesterday)
        println(s"Saved ${accessCounts.size} file access records for $domain on $yesterday.")
      }
    }

    // Find and report unaccessed files
    findAndReportUnaccessedFiles(60, wechatWebhook)

    backend.close()
  }

  def downloadLogs(cdnClient: CDNClient, domain: String, baseLogDir: String, date: LocalDate): String = {
    val startTime = date.atStartOfDay()
    val endTime = date.atTime(23, 59, 59)

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateStr = date.format(formatter)

    val domainLogDir = s"$baseLogDir/$domain/$dateStr"
    File(domainLogDir).createDirectories()

    val logInfos = cdnClient.getDomainLogs(
      domainName = domain,
      startTime = startTime,
      endTime = endTime,
    )

    logInfos.par.foreach { info =>
      cdnClient.downloadLog(info, domainLogDir)
    }

    domainLogDir
  }

  def analyzeLogs(cdnClient: CDNClient, logDir: String): Map[String, Int] = {
    val logDirectory = File(logDir)
    if (!logDirectory.exists) return Map.empty

    val logFiles = logDirectory.listRecursively
      .filter(f => f.isRegularFile && (f.name.endsWith(".gz") || f.name.endsWith(".log")))
      .toList

    if (logFiles.isEmpty) return Map.empty

    val allLogEntries = logFiles.par.flatMap { file =>
      cdnClient.parseLogFile(file.toJava)
    }.toList

    allLogEntries
      .groupBy(_.requestURL)
      .mapValues(_.size)
      .seq
  }

  def saveAccessCounts(counts: Map[String, Int], date: LocalDate)(implicit db: scalasql.Db): Unit = {
    val dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    db.transaction {
      counts.foreach { case (path, count) =>
        val query = FileAccessLog.insert
          .columns(
            _.logDate -> dateStr,
            _.filePath -> path,
            _.accessCount -> count
          )
          .onConflict(_.logDate, _.filePath)
          .doUpdate((t, e) => t.accessCount := e.accessCount)
        db.run(query)
      }
    }
  }

  def findAndReportUnaccessedFiles(days: Int, wechatWebhook: Option[WeChatWorkWebhook])(implicit db: scalasql.Db): Unit = {
    println(s"\nChecking for files not accessed in the last $days days...")
    val cutoffDate = LocalDate.now().minusDays(days).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    val unaccessedFiles = db.run(
        FileAccessLog.select
            .groupBy(_.filePath)(p => sql"max(${p.logDate})")
            .filter(_._2 < cutoffDate)
            .map(_._1)
    )

    if (unaccessedFiles.nonEmpty) {
      println(s"Found ${unaccessedFiles.size} unaccessed files.")
      wechatWebhook.foreach { webhook =>
        val fileList = unaccessedFiles.map(f => s"- `$f`").mkString("\n")
        val message = s"""## ⚠️ Unaccessed Files Report
        |
        |The following ${unaccessedFiles.size} files have not been accessed in the last $days days:
        |
        |$fileList
        """.stripMargin
        webhook.sendMarkdown(message) match {
          case Success(_) => println("Successfully sent unaccessed files report to WeChat Work.")
          case Failure(e) => println(s"Failed to send report: ${e.getMessage}")
        }
      }
    } else {
      println("No unaccessed files found.")
    }
  }
}
