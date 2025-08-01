package com.timzaak.devops.alicloud

import better.files.*
import com.aliyuncs.{DefaultAcsClient, IAcsClient}
import com.aliyuncs.cdn.model.v20180510.{DescribeCdnDomainLogsRequest, DescribeDomainCustomLogConfigRequest}
import com.aliyuncs.profile.DefaultProfile
import sttp.client4.*
import sttp.client4.httpurlconnection.HttpURLConnectionBackend

import java.io
import java.io.{BufferedReader, InputStreamReader}
import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneOffset}
import java.util.zip.GZIPInputStream
import scala.jdk.CollectionConverters.*
import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex

// CDN日志条目的数据结构
case class CDNLogEntry(
  accessTime: String,        // 访问时间
  clientIP: String,          // 客户端IP
  proxyIP: String,           // 代理IP
  responseTime: Int,         // 响应时间
  referer: String,           // Referer
  requestMethod: String,     // 请求类型
  requestURL: String,        // 请求URL
  httpStatusCode: Int,       // HTTP状态码
  requestBytes: Int,         // 请求字节数
  responseBytes: Int,        // 响应字节数
  cacheHitStatus: String,    // 是否命中CDN节点
  userAgent: String,         // User_Agent
  fileType: String,          // 文件类型
  accessIP: String           // 访问IP
)

class CDNLogAnalyse(
                     accessKeyId: String,
                     accessKeySecret: String,
                     regionId: String,
                   ) {
  private val profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret)
  private val client: IAcsClient = new DefaultAcsClient(profile)
  private val backend = HttpURLConnectionBackend()

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
                   ): List[(String,String)] = {
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
      case Failure(e) =>
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
    val targetFile = targetDirectory / fileName
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
    val logPattern: Regex = 
      """\[([^\]]+)\]\s+(\S+)\s+(\S+)\s+(\d+)\s+"([^"]*)"\s+"([^"]*)\s+([^"]*?)"\s+(\d+)\s+(\d+)\s+(\d+)\s+(\S+)\s+"([^"]*)"\s+"([^"]*)"\s+(\S+)""".r

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
      case Failure(e) =>
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
    val logPattern: Regex = 
      """\[([^\]]+)\]\s+(\S+)\s+(\S+)\s+(\d+)\s+"([^"]*)"\s+"([^"]*)\s+([^"]*?)"\s+(\d+)\s+(\d+)\s+(\d+)\s+(\S+)\s+"([^"]*)"\s+"([^"]*)"\s+(\S+)""".r

    line match {
      case logPattern(accessTime, clientIP, proxyIP, responseTime, referer, requestMethod, requestURL, 
                     httpStatusCode, requestBytes, responseBytes, cacheHitStatus, userAgent, fileType, accessIP) =>
        Try {
          CDNLogEntry(
            accessTime = accessTime,
            clientIP = clientIP,
            proxyIP = if (proxyIP == "-") "" else proxyIP,
            responseTime = responseTime.toInt,
            referer = referer,
            requestMethod = requestMethod,
            requestURL = requestURL,
            httpStatusCode = httpStatusCode.toInt,
            requestBytes = requestBytes.toInt,
            responseBytes = responseBytes.toInt,
            cacheHitStatus = cacheHitStatus,
            userAgent = userAgent,
            fileType = fileType,
            accessIP = accessIP
          )
        }.toOption
      case _ =>
        println(s"Failed to parse log line: $line")
        None
    }
  }


}