package com.timzaak.devops.location

import io.circe.*
import io.circe.generic.auto.*
import sttp.client4.*
import sttp.client4.circe.*

import scala.util.{Try, Success, Failure}

// IP地理位置信息数据类
case class IpLocationInfo(
  status: String,
  country: String,
  countryCode: String,
  region: String,
  regionName: String,
  city: String,
  zip: String,
  lat: Double,
  lon: Double,
  timezone: String,
  isp: String,
  org: String,
  as: String,
  query: String
)

// 简化的地理位置信息
case class RegionInfo(
  country: String,
  region: String,
  city: String,
  ip: String
)

class Ip2Region {
  
  private val baseUrl = "http://ip-api.com/json"
  
  /**
   * 根据IP地址查询地理位置信息
   * @param ip IP地址
   * @param backend HTTP客户端后端
   * @return 地理位置信息
   */
  def byIp(ip: String)(using backend: SyncBackend): Try[RegionInfo] = {
    if (ip == null || ip.trim.isEmpty) {
      return Failure(new IllegalArgumentException("IP地址不能为空"))
    }
    
    val request = basicRequest
      .get(uri"$baseUrl/$ip")
      .response(asJson[IpLocationInfo])
    
    Try {
      val response = request.send(backend)
      
      response.body match {
        case Right(locationInfo) =>
          if (locationInfo.status == "success") {
            RegionInfo(
              country = locationInfo.country,
              region = locationInfo.regionName,
              city = locationInfo.city,
              ip = locationInfo.query
            )
          } else {
            throw new RuntimeException(s"IP查询失败: ${locationInfo.status}")
          }
        case Left(error) =>
          throw new RuntimeException(s"解析响应失败: $error")
      }
    }
  }
  
  /**
   * 批量查询多个IP的地理位置信息
   * @param ips IP地址列表
   * @param backend HTTP客户端后端
   * @return 地理位置信息列表
   */
  def byIps(ips: List[String])(using backend: SyncBackend): List[Try[RegionInfo]] = {
    if (ips == null) {
      return List.empty
    }
    ips.filter(ip => ip != null && ip.trim.nonEmpty).map(ip => byIp(ip))
  }
  
  /**
   * 获取详细的IP地理位置信息
   * @param ip IP地址
   * @param backend HTTP客户端后端
   * @return 详细的地理位置信息
   */
  def getDetailedInfo(ip: String)(using backend: SyncBackend): Try[IpLocationInfo] = {
    if (ip == null || ip.trim.isEmpty) {
      return Failure(new IllegalArgumentException("IP地址不能为空"))
    }
    
    val request = basicRequest
      .get(uri"$baseUrl/$ip")
      .response(asJson[IpLocationInfo])
    
    Try {
      val response = request.send(backend)
      
      response.body match {
        case Right(locationInfo) =>
          if (locationInfo.status == "success") {
            locationInfo
          } else {
            throw new RuntimeException(s"IP查询失败: ${locationInfo.status}")
          }
        case Left(error) =>
          throw new RuntimeException(s"解析响应失败: $error")
      }
    }
  }
}

object Ip2Region {
  def apply(): Ip2Region = new Ip2Region()
}