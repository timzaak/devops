import com.timzaak.devops.alicloud.CDNLogAnalyse
import com.typesafe.config.ConfigFactory
import io.circe.*
import io.circe.config.syntax.*
import io.circe.generic.auto.*

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ForkJoinPool
import scala.collection.parallel.ForkJoinTaskSupport
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*

import scala.collection.parallel.CollectionConverters.*



// sbt "runMain AliCDNLogAnalyse https://wiki.example.com"
object AliCDNLogAnalyse {

  case class CDNConfig(
                        accessKeyId: String,
                        accessKeySecret: String,
                        regionId: String,
                        logDir: String
                      )

  def main(args: Array[String]): Unit = {
    // Load configuration from application.conf
    val config = ConfigFactory.load().getConfig("alicloud.cdn").as[CDNConfig].toTry.get

    // Create CDNLogAnalyse instance
    val cdnLogAnalyse = CDNLogAnalyse(
      accessKeyId = config.accessKeyId,
      accessKeySecret = config.accessKeySecret,
      regionId = config.regionId
    )

    // Process each domain
    args.foreach { domain =>
      println(s"Processing domain: $domain ============")
      processDomain(cdnLogAnalyse, domain, config.logDir)
    }

    println("CDN log analysis completed successfully.")
  }

  // Process a single domain
  private def processDomain(cdnLogAnalyse: CDNLogAnalyse, domain: String, baseLogDir: String): Unit = {
    // Calculate time range (yesterday start to yesterday end)
    val endTime = LocalDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(59)
    val startTime = endTime.withHour(0).withMinute(0).withSecond(0)

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateStr = endTime.format(formatter)

    // Create domain-specific log directory
    val domainLogDir = s"$baseLogDir/$domain/$dateStr"

    val downloadPool = ForkJoinPool(4)
    val taskSupport = ForkJoinTaskSupport(downloadPool)
    val info = cdnLogAnalyse.getDomainLogs(
      domainName = domain,
      startTime = startTime,
      endTime = endTime,
    )
    val logInfo = info.par
    logInfo.tasksupport = taskSupport
    logInfo.foreach{ v=>
      cdnLogAnalyse.downloadLog(v, domainLogDir)
      println(s"download: ${v._1}")
    }
    println("download finish....")
    downloadPool.shutdown()
    println("begin to analysis")
    
    // 这里可以添加对下载的日志文件的解析逻辑
    // 使用 cdnLogAnalyse.parseLogFile() 方法解析单个文件

  }
}