import com.timzaak.devops.alicloud.{CDNClient, CDNLogEntry}
import com.typesafe.config.ConfigFactory
import io.circe.*
import io.circe.config.syntax.*
import io.circe.generic.auto.*
import better.files.*

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ForkJoinPool
import scala.collection.parallel.ForkJoinTaskSupport

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
    val config = ConfigFactory.load().getConfig("alicloud.cdn").as[CDNConfig].toTry.get

    val cdnLogAnalyse = CDNClient(
      accessKeyId = config.accessKeyId,
      accessKeySecret = config.accessKeySecret,
      regionId = config.regionId
    )

    args.foreach { domain =>
      println(s"Processing domain: $domain ============")
      val domainLogDir = downloadLogFile(cdnLogAnalyse, domain, config.logDir)
      // val domainLogDir = s"${config.logDir}/${domain}/2025-07-31"
      analyzeLogFiles(cdnLogAnalyse,domainLogDir)
    }



  }

  // Process a single domain
  private def downloadLogFile(cdnLogAnalyse: CDNClient, domain: String, baseLogDir: String): String = {
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
    domainLogDir
  }

  // 分析日志文件的统计功能
  private def analyzeLogFiles(cdnLogAnalyse: CDNClient, logDir: String): Unit = {
    val logDirectory = File(logDir)
    if (!logDirectory.exists) {
      println(s"Log directory does not exist: $logDir")
      return
    }

    // 获取所有日志文件
    val logFiles = logDirectory.listRecursively
      .filter(_.isRegularFile)
      .filter(f => f.name.endsWith(".gz") || f.name.endsWith(".log"))
      .toList

    if (logFiles.isEmpty) {
      println("No log files found for analysis")
      return
    }

    println(s"Found ${logFiles.length} log files to analyze")

    // 创建CDNLogAnalyse实例用于解析


    val parallelFiles = logFiles.par

    val allLogEntries = parallelFiles.flatMap { file =>
      println(s"Parsing: ${file.name}")
      cdnLogAnalyse.parseLogFile(file.toJava)
    }.toList

    if (allLogEntries.isEmpty) {
      println("No valid log entries found")
      return
    }

    performStatistics(allLogEntries)
  }

  // 执行统计分析
  private def performStatistics(logEntries: List[CDNLogEntry]): Unit = {
    println("\n" + "="*80)
    println("CDN 日志统计分析结果")
    println("="*80)

    val parallelEntries = logEntries.par
    val analysisPool = ForkJoinPool(Runtime.getRuntime.availableProcessors())
    parallelEntries.tasksupport = ForkJoinTaskSupport(analysisPool)

    // 1. 统计访问最多的IP
    println("\n1. 访问最多的IP统计:")
    val ipCounts = parallelEntries
      .groupBy(_.clientIP)
      .mapValues(_.size)
      .seq
      .toSeq
      .sortBy(-_._2)
      .take(10)

    ipCounts.zipWithIndex.foreach { case ((ip, count), index) =>
      println(f"${index + 1}%2d. IP: $ip%-15s 访问次数: $count%,d")
    }

    println("\n2. 响应字节数最多的连接:")
    val maxResponseBytes = parallelEntries.maxBy(_.responseBytes)
    println(f"IP: ${maxResponseBytes.clientIP}%-15s")
    println(f"URL: ${maxResponseBytes.requestURL}")
    println(f"响应字节数: ${maxResponseBytes.responseBytes}%,d bytes")
    println(f"User-Agent: ${maxResponseBytes.userAgent}")

    println("\n3. 请求次数最多的URL:")
    val urlCounts = parallelEntries
      .groupBy(_.requestURL)
      .mapValues(_.size)
      .seq
      .toSeq
      .sortBy(-_._2)
      .take(5)

    urlCounts.zipWithIndex.foreach { case ((url, count), index) =>
      println(f"${index + 1}%2d. 请求次数: $count%,d")
      println(f"    URL: $url")
    }

    println("\n4. 访问最多的前5个IP详细分析:")
    val top5IPs = ipCounts.take(5)

    top5IPs.zipWithIndex.foreach { case ((ip, count), index) =>
      println(f"\n${index + 1}. IP: $ip (访问次数: $count%,d)")

      val ipEntries = logEntries.filter(_.clientIP == ip)
      val ipParallel = ipEntries.par
      ipParallel.tasksupport = ForkJoinTaskSupport(analysisPool)

      // User-Agent统计
      val userAgents = ipParallel
        .groupBy(_.userAgent)
        .mapValues(_.size)
        .seq
        .toSeq
        .sortBy(-_._2)
        .take(3)

      println("   主要User-Agent:")
      userAgents.foreach { case (ua, uaCount) =>
        println(f"   - ($uaCount%,d次) $ua")
      }

      val fileTypes = ipParallel
        .groupBy(_.fileType)
        .mapValues(_.size)
        .seq
        .toSeq
        .sortBy(-_._2)
        .take(5)

      println("   访问文件类型:")
      fileTypes.foreach { case (fileType, ftCount) =>
        println(f"   - $fileType%-10s: $ftCount%,d次")
      }

      val statusCodes = ipParallel
        .groupBy(_.httpStatusCode)
        .mapValues(_.size)
        .seq
        .toSeq
        .sortBy(-_._2)

      println("   HTTP状态码分布:")
      statusCodes.foreach { case (status, statusCount) =>
        println(f"   - $status: $statusCount%,d次")
      }

      val totalBytes = ipParallel.map(_.responseBytes.toLong).sum
      println(f"   总响应流量: ${totalBytes / (1024 * 1024)}%,d MB")
    }

    println("\n5. CDN盗刷检测分析:")
    detectCDNAbuse(logEntries, analysisPool)

    analysisPool.shutdown()
  }

  // CDN盗刷检测分析
  private def detectCDNAbuse(logEntries: List[CDNLogEntry], pool: ForkJoinPool): Unit = {
    val parallelEntries = logEntries.par
    parallelEntries.tasksupport = ForkJoinTaskSupport(pool)

    // 检测可疑的高频访问IP
    val ipStats = parallelEntries
      .groupBy(_.clientIP)
      .map { case (ip, entries) =>
        val entryList = entries.toList
        val requestCount = entryList.size
        val totalBytes = entryList.map(_.responseBytes.toLong).sum
        val uniqueUrls = entryList.map(_.requestURL).distinct.size
        val userAgents = entryList.map(_.userAgent).distinct
        val avgResponseTime = if (entryList.nonEmpty) entryList.map(_.responseTime).sum / entryList.size else 0

        (ip, requestCount, totalBytes, uniqueUrls, userAgents, avgResponseTime)
      }
      .toList
      .sortBy(-_._2) // 按请求次数排序

    println("\n可疑高频访问IP分析:")
    val suspiciousIPs = ipStats.filter { case (_, requestCount, totalBytes, uniqueUrls, userAgents, _) =>
      requestCount > 1000 || // 请求次数超过1000
        totalBytes > 100 * 1024 * 1024 || // 流量超过100MB
        (requestCount > 100 && uniqueUrls < 5) || // 高频访问但URL很少
        userAgents.size == 1 // 只有一个User-Agent
    }

    if (suspiciousIPs.nonEmpty) {
      suspiciousIPs.take(10).zipWithIndex.foreach { case ((ip, requestCount, totalBytes, uniqueUrls, userAgents, avgResponseTime), index) =>
        println(f"\n${index + 1}. 可疑IP: $ip")
        println(f"   请求次数: $requestCount%,d")
        println(f"   总流量: ${totalBytes / (1024 * 1024)}%,d MB")
        println(f"   访问URL数量: $uniqueUrls")
        println(f"   User-Agent数量: ${userAgents.size}")
        println(f"   平均响应时间: ${avgResponseTime}ms")

        // 分析可疑原因
        val reasons = scala.collection.mutable.ListBuffer[String]()
        if (requestCount > 5000) reasons += "超高频访问"
        if (totalBytes > 500 * 1024 * 1024) reasons += "超大流量消耗"
        if (requestCount > 100 && uniqueUrls < 5) reasons += "重复访问少数URL"
        if (userAgents.size == 1) reasons += "单一User-Agent"
        if (avgResponseTime < 50) reasons += "响应时间异常短"

        println(f"   可疑原因: ${reasons.mkString(", ")}")

        if (userAgents.size <= 3) {
          println("   User-Agent:")
          userAgents.take(3).foreach(ua => println(f"   - $ua"))
        }
      }
    } else {
      println("未发现明显的可疑访问模式")
    }

    // 检测异常User-Agent
    println("\n异常User-Agent分析:")
    val userAgentStats = parallelEntries
      .groupBy(_.userAgent)
      .map { case (ua, entries) =>
        val entryList = entries.toList
        val requestCount = entryList.size
        val uniqueIPs = entryList.map(_.clientIP).distinct.size
        val totalBytes = entryList.map(_.responseBytes.toLong).sum
        (ua, requestCount, uniqueIPs, totalBytes)
      }
      .seq
      .toList
      .sortBy(-_._2)

    val suspiciousUserAgents = userAgentStats.filter { case (ua, requestCount, uniqueIPs, _) =>
      ua.toLowerCase.contains("bot") ||
        ua.toLowerCase.contains("crawler") ||
        ua.toLowerCase.contains("spider") ||
        ua.toLowerCase.contains("wget") ||
        ua.toLowerCase.contains("curl") ||
        (requestCount > 1000 && uniqueIPs < 5) // 高频但IP很少
    }

    if (suspiciousUserAgents.nonEmpty) {
      suspiciousUserAgents.take(5).zipWithIndex.foreach { case ((ua, requestCount, uniqueIPs, totalBytes), index) =>
        println(f"\n${index + 1}. 可疑User-Agent:")
        println(f"   User-Agent: $ua")
        println(f"   请求次数: $requestCount%,d")
        println(f"   使用IP数: $uniqueIPs")
        println(f"   总流量: ${totalBytes / (1024 * 1024)}%,d MB")
      }
    } else {
      println("未发现明显的可疑User-Agent")
    }

    // 时间分布异常检测
    println("\n访问时间分布分析:")
    val hourlyStats = parallelEntries
      .map(entry => {
        // 从访问时间中提取小时 [8/Jan/2025:20:16:54 +0800]
        val timeStr = entry.accessTime
        val hourStart = timeStr.indexOf(':') + 1
        val hourEnd = timeStr.indexOf(':', hourStart)
        if (hourStart > 0 && hourEnd > hourStart) {
          timeStr.substring(hourStart, hourEnd).toIntOption.getOrElse(0)
        } else 0
      })
      .groupBy(identity)
      .mapValues(_.size)
      .seq
      .toSeq
      .sortBy(_._1)

    println("每小时访问量分布:")
    hourlyStats.foreach { case (hour, count) =>
      val bar = "█" * (count / (hourlyStats.map(_._2).max / 50).max(1))
      println(f"$hour%2d:00 - $count%,6d $bar")
    }

    // 检测深夜异常访问
    val nightAccess = hourlyStats.filter { case (hour, _) => hour >= 0 && hour <= 5 }
    val dayAccess = hourlyStats.filter { case (hour, _) => hour >= 9 && hour <= 17 }

    if (nightAccess.nonEmpty && dayAccess.nonEmpty) {
      val nightTotal = nightAccess.map(_._2).sum
      val dayTotal = dayAccess.map(_._2).sum
      val nightRatio = nightTotal.toDouble / (nightTotal + dayTotal)

      if (nightRatio > 0.3) {
        println(f"\n⚠️  检测到异常: 深夜访问占比过高 (${nightRatio * 100}%.1f%%)")
        println("这可能表明存在自动化工具或恶意访问")
      }
    }
  }
}