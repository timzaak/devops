package com.timzaak.devops.location

import munit.FunSuite
import sttp.client4.*

import scala.util.{Success, Failure}

class Ip2RegionTest extends FunSuite {
  
  given backend: SyncBackend = DefaultSyncBackend()
  val ip2Region = Ip2Region()
  
  test("byIp should return region info for valid IP") {
    val result = ip2Region.byIp("8.8.8.8")
    
    result match {
      case Success(region) =>
        assertEquals(region.ip, "8.8.8.8")
        assert(region.country.nonEmpty)
        assert(region.region.nonEmpty)
      case Failure(exception) =>
        fail(s"Expected success but got failure: ${exception.getMessage}")
    }
  }
  
  test("byIp should handle invalid IP") {
    val result = ip2Region.byIp("invalid.ip")
    
    assert(result.isFailure)
  }
  
  test("byIp should handle null IP") {
    val result = ip2Region.byIp(null)
    
    assert(result.isFailure)
    result match {
      case Failure(exception) =>
        assert(exception.isInstanceOf[IllegalArgumentException])
      case _ => fail("Expected IllegalArgumentException")
    }
  }
  
  test("byIp should handle empty IP") {
    val result = ip2Region.byIp("")
    
    assert(result.isFailure)
    result match {
      case Failure(exception) =>
        assert(exception.isInstanceOf[IllegalArgumentException])
      case _ => fail("Expected IllegalArgumentException")
    }
  }
  
  test("byIps should handle multiple IPs") {
    val ips = List("8.8.8.8", "1.1.1.1")
    val results = ip2Region.byIps(ips)
    
    assertEquals(results.length, 2)
    
    results.foreach { result =>
      result match {
        case Success(region) =>
          assert(region.country.nonEmpty)
        case Failure(_) =>
          // 网络问题可能导致失败，这里不强制要求成功
      }
    }
  }
  
  test("byIps should handle null list") {
    val results = ip2Region.byIps(null)
    
    assertEquals(results.length, 0)
  }
  
  test("byIps should filter out null and empty IPs") {
    val ips = List("8.8.8.8", null, "", "  ", "1.1.1.1")
    val results = ip2Region.byIps(ips)
    
    assertEquals(results.length, 2) // 只有两个有效IP
  }
  
  test("getDetailedInfo should return detailed information") {
    val result = ip2Region.getDetailedInfo("8.8.8.8")
    
    result match {
      case Success(info) =>
        assertEquals(info.query, "8.8.8.8")
        assertEquals(info.status, "success")
        assert(info.country.nonEmpty)
        assert(info.lat != 0.0 || info.lon != 0.0)
      case Failure(exception) =>
        fail(s"Expected success but got failure: ${exception.getMessage}")
    }
  }
  
  test("getDetailedInfo should handle null IP") {
    val result = ip2Region.getDetailedInfo(null)
    
    assert(result.isFailure)
    result match {
      case Failure(exception) =>
        assert(exception.isInstanceOf[IllegalArgumentException])
      case _ => fail("Expected IllegalArgumentException")
    }
  }
  
  override def afterAll(): Unit = {
    backend.close()
  }
}