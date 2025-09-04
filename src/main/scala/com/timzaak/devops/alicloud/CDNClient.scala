package com.timzaak.devops.alicloud

import better.files.*
import com.aliyuncs.{ DefaultAcsClient, IAcsClient }
import com.aliyuncs.cdn.model.v20180510.{ DescribeCdnDomainLogsRequest, DescribeDomainCustomLogConfigRequest }
import com.aliyuncs.profile.DefaultProfile
import sttp.client4.*
import sttp.client4.httpurlconnection.HttpURLConnectionBackend

import java.io
import java.io.{ BufferedReader, InputStreamReader }
import java.time.format.DateTimeFormatter
import java.time.{ LocalDateTime, ZoneOffset }
import java.util.zip.GZIPInputStream
import scala.jdk.CollectionConverters.*
import scala.util.{ Failure, Success, Try }

// CDN日志条目的数据结构
case class CDNLogEntry(
  accessTime: String, // 访问时间
  clientIP: String, // 客户端IP
  proxyIP: String, // 代理IP
  responseTime: Int, // 响应时间
  referer: String, // Referer
  requestMethod: String, // 请求类型
  requestURL: String, // 请求URL
  httpStatusCode: Int, // HTTP状态码
  requestBytes: Int, // 请求字节数
  responseBytes: Int, // 响应字节数
  cacheHitStatus: String, // 是否命中CDN节点
  userAgent: String, // User_Agent
  fileType: String, // 文件类型
  accessIP: String // 访问IP
)

class CDNClient(
  accessKeyId: String,
  accessKeySecret: String,
  regionId: String,
  backend: SyncBackend
) {
  private val profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret)
  private val client: IAcsClient = new DefaultAcsClient(profile)

  /**
   * Get the custom log configuration for a domain
   *
   * @param domainName The domain name to query
   * @return The log config ID if available

             def getCustomLogConfig(domainName: String): Option[String] = {
             val request = DescribeDomainCustomLogConfigRequest()
             request.setDomainName(domainName)

             Try {
             val response = client.getAcsResponse(request)
             Option(response.getConfigId)
             } match {
             case Success(configId) => configId
             case Failure(e) => 
             println(s"Failed to get custom log config: ${e.getMessage}")
             None
             }
             }
   */

  def getDomainLogs(
    startTime: LocalDateTime,
    endTime: LocalDateTime,
    domainName: String,
  ): List[(String, String)] = {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val startTimeStr = startTime.atOffset(ZoneOffset.UTC).format(formatter)
    val endTimeStr = endTime.atOffset(ZoneOffset.UTC).format(formatter)

    val request = new DescribeCdnDomainLogsRequest()
    request.setDomainName(domainName)
    request.setStartTime(startTimeStr)
    request.setEndTime(endTimeStr)

    Try {
      val response = client.getAcsResponse(request)
      val domainLogDetails = response.getDomainLogDetails

      if (domainLogDetails != null) {
        domainLogDetails.asScala.toList.flatMap { detail =>
          if (detail.getLogInfos != null) {
            detail.getLogInfos.asScala.toList.map(v => v.getLogName -> v.getLogPath)
          } else {
            List.empty
          }
        }
      } else {
        List.empty
      }
    } match {
      case Success(logUrls) => logUrls
      case Failure(e)       =>
        println(s"Failed to get domain logs: ${e.getMessage}")
        List.empty
    }
  }

  def downloadLog(
    info: (String, String),
    targetDir: String
  ): Either[String, io.File] = {
    val (fileName, logPath) = info
    val targetDirectory = File(targetDir)
    targetDirectory.createDirectories()
    val targetFile = targetDirectory / fileName

    if (targetFile.exists && targetFile.size > 0) {
      println(s"Log file ${targetFile.name} already exists in ${targetDir}. Skipping download.")
      return Right(targetFile.toJava)
    }

    val u = s"https://${logPath}"
    val request = basicRequest
      .get(uri"$u")
      .response(asFile(targetFile.toJava))
    val response = request.send(backend)
    response.body
  }

  /**
   * 解析CDN日志文件（支持gzip压缩格式）
   * 
   * @param logFile 日志文件路径
   * @return 解析后的日志条目列表
   */
  def parseLogFile(logFile: io.File): List[CDNLogEntry] = {
    Try {
      val inputStream = if (logFile.getName.endsWith(".gz")) {
        new GZIPInputStream(new java.io.FileInputStream(logFile))
      } else {
        new java.io.FileInputStream(logFile)
      }

      val reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))
      val lines = Iterator.continually(reader.readLine()).takeWhile(_ != null).toList
      reader.close()

      lines.flatMap(parseLogLine)
    } match {
      case Success(entries) => entries
      case Failure(e)       =>
        println(s"Failed to parse log file ${logFile.getName}: ${e.getMessage}")
        List.empty
    }
  }

  /**
   * 解析单行日志
   * 
   * @param line 日志行内容
   * @return 解析后的日志条目（如果解析成功）
   */
  def parseLogLine(line: String): Option[CDNLogEntry] = {
    def parseFields(): Option[CDNLogEntry] = {
      // 解析访问时间 [8/Jan/2025:20:16:54 +0800]
      val timeStart = line.indexOf('[')
      val timeEnd = line.indexOf(']', timeStart)
      if (timeStart == -1 || timeEnd == -1) return None

      val accessTime = line.substring(timeStart + 1, timeEnd)
      var pos = timeEnd + 1

      // 跳过空格
      while (pos < line.length && line.charAt(pos) == ' ') pos += 1

      // 解析客户端IP
      val clientIPStart = pos
      while (pos < line.length && line.charAt(pos) != ' ') pos += 1
      val clientIP = line.substring(clientIPStart, pos)

      // 跳过空格
      while (pos < line.length && line.charAt(pos) == ' ') pos += 1

      // 解析代理IP
      val proxyIPStart = pos
      while (pos < line.length && line.charAt(pos) != ' ') pos += 1
      val proxyIPRaw = line.substring(proxyIPStart, pos)
      val proxyIP = if (proxyIPRaw == "-") "" else proxyIPRaw

      // 跳过空格
      while (pos < line.length && line.charAt(pos) == ' ') pos += 1

      // 解析响应时间
      val responseTimeStart = pos
      while (pos < line.length && line.charAt(pos) != ' ') pos += 1
      val responseTime = line.substring(responseTimeStart, pos).toInt

      // 跳过空格
      while (pos < line.length && line.charAt(pos) == ' ') pos += 1

      // 解析 Referer (在双引号中)
      if (pos >= line.length || line.charAt(pos) != '"') return None
      pos += 1 // 跳过开始的双引号
      val refererStart = pos
      while (pos < line.length && line.charAt(pos) != '"') pos += 1
      if (pos >= line.length) return None
      val referer = line.substring(refererStart, pos)
      pos += 1 // 跳过结束的双引号

      // 跳过空格
      while (pos < line.length && line.charAt(pos) == ' ') pos += 1

      // 解析请求方法和URL (在双引号中，格式: "GET http://...")
      if (pos >= line.length || line.charAt(pos) != '"') return None
      pos += 1 // 跳过开始的双引号
      val requestStart = pos
      while (pos < line.length && line.charAt(pos) != '"') pos += 1
      if (pos >= line.length) return None
      val requestLine = line.substring(requestStart, pos)
      pos += 1 // 跳过结束的双引号

      // 分解请求行为方法和URL
      val requestParts = requestLine.split(" ", 2)
      if (requestParts.length < 2) return None
      val requestMethod = requestParts(0)
      val requestURL = requestParts(1)

      // 跳过空格
      while (pos < line.length && line.charAt(pos) == ' ') pos += 1

      // 解析HTTP状态码
      val statusCodeStart = pos
      while (pos < line.length && line.charAt(pos) != ' ') pos += 1
      val httpStatusCode = line.substring(statusCodeStart, pos).toInt

      // 跳过空格
      while (pos < line.length && line.charAt(pos) == ' ') pos += 1

      // 解析请求字节数
      val requestBytesStart = pos
      while (pos < line.length && line.charAt(pos) != ' ') pos += 1
      val requestBytes = line.substring(requestBytesStart, pos).toInt

      // 跳过空格
      while (pos < line.length && line.charAt(pos) == ' ') pos += 1

      // 解析响应字节数
      val responseBytesStart = pos
      while (pos < line.length && line.charAt(pos) != ' ') pos += 1
      val responseBytes = line.substring(responseBytesStart, pos).toInt

      // 跳过空格
      while (pos < line.length && line.charAt(pos) == ' ') pos += 1

      // 解析缓存命中状态
      val cacheHitStart = pos
      while (pos < line.length && line.charAt(pos) != ' ') pos += 1
      val cacheHitStatus = line.substring(cacheHitStart, pos)

      // 跳过空格
      while (pos < line.length && line.charAt(pos) == ' ') pos += 1

      // 解析 User-Agent (在双引号中)
      if (pos >= line.length || line.charAt(pos) != '"') return None
      pos += 1 // 跳过开始的双引号
      val userAgentStart = pos
      while (pos < line.length && line.charAt(pos) != '"') pos += 1
      if (pos >= line.length) return None
      val userAgent = line.substring(userAgentStart, pos)
      pos += 1 // 跳过结束的双引号

      // 跳过空格
      while (pos < line.length && line.charAt(pos) == ' ') pos += 1

      // 解析文件类型 (在双引号中)
      if (pos >= line.length || line.charAt(pos) != '"') return None
      pos += 1 // 跳过开始的双引号
      val fileTypeStart = pos
      while (pos < line.length && line.charAt(pos) != '"') pos += 1
      if (pos >= line.length) return None
      val fileType = line.substring(fileTypeStart, pos)
      pos += 1 // 跳过结束的双引号

      // 跳过空格
      while (pos < line.length && line.charAt(pos) == ' ') pos += 1

      // 解析访问IP (最后一个字段)
      val accessIPStart = pos
      while (pos < line.length && line.charAt(pos) != ' ') pos += 1
      val accessIP = line.substring(accessIPStart, if (pos < line.length) pos else line.length)

      Some(
        CDNLogEntry(
          accessTime = accessTime,
          clientIP = clientIP,
          proxyIP = proxyIP,
          responseTime = responseTime,
          referer = referer,
          requestMethod = requestMethod,
          requestURL = requestURL,
          httpStatusCode = httpStatusCode,
          requestBytes = requestBytes,
          responseBytes = responseBytes,
          cacheHitStatus = cacheHitStatus,
          userAgent = userAgent,
          fileType = fileType,
          accessIP = accessIP
        )
      )
    }

    Try(parseFields()).toOption.flatten.orElse {
      println(s"Failed to parse log line: $line")
      None
    }
  }

}
