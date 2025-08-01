package com.timzaak.devops.alicloud

import munit.FunSuite

class CDNClientSuite extends FunSuite {

  // 创建测试用的 CDNLogAnalyse 实例
  val cdnLogAnalyse = new CDNLogAnalyse("test-key", "test-secret", "cn-hangzhou")

  test("parseLogLine should parse valid CDN log line correctly") {
    val logLine = """[8/Jan/2025:20:16:54 +0800] 139.224.XXX.XXX - 246 "-" "GET http://cdn.aliyun.cn/images/cdn.gif" 403 369 978 MISS "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36" "application/xml" 139.224.XXX.XXX"""
    
    val result = cdnLogAnalyse.parseLogLine(logLine)
    
    assert(result.isDefined)
    val entry = result.get
    
    assertEquals(entry.accessTime, "8/Jan/2025:20:16:54 +0800")
    assertEquals(entry.clientIP, "139.224.XXX.XXX")
    assertEquals(entry.proxyIP, "")  // "-" 转换为空字符串
    assertEquals(entry.responseTime, 246)
    assertEquals(entry.referer, "-")
    assertEquals(entry.requestMethod, "GET")
    assertEquals(entry.requestURL, "http://cdn.aliyun.cn/images/cdn.gif")
    assertEquals(entry.httpStatusCode, 403)
    assertEquals(entry.requestBytes, 369)
    assertEquals(entry.responseBytes, 978)
    assertEquals(entry.cacheHitStatus, "MISS")
    assertEquals(entry.userAgent, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
    assertEquals(entry.fileType, "application/xml")
    assertEquals(entry.accessIP, "139.224.XXX.XXX")
  }

  test("parseLogLine should parse HIT cache status log line correctly") {
    val logLine = """[8/Jan/2025:20:16:55 +0800] 139.224.XXX.XXX - 197 "https://www.aliyun.com/" "GET http://cdn.aliyun.cn/images/cdn.jpg" 200 369 1143 HIT "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36" "application/xml" 139.224.XXX.XXX"""
    
    val result = cdnLogAnalyse.parseLogLine(logLine)
    
    assert(result.isDefined)
    val entry = result.get
    
    assertEquals(entry.accessTime, "8/Jan/2025:20:16:55 +0800")
    assertEquals(entry.clientIP, "139.224.XXX.XXX")
    assertEquals(entry.proxyIP, "")
    assertEquals(entry.responseTime, 197)
    assertEquals(entry.referer, "https://www.aliyun.com/")
    assertEquals(entry.requestMethod, "GET")
    assertEquals(entry.requestURL, "http://cdn.aliyun.cn/images/cdn.jpg")
    assertEquals(entry.httpStatusCode, 200)
    assertEquals(entry.requestBytes, 369)
    assertEquals(entry.responseBytes, 1143)
    assertEquals(entry.cacheHitStatus, "HIT")
    assertEquals(entry.userAgent, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
    assertEquals(entry.fileType, "application/xml")
    assertEquals(entry.accessIP, "139.224.XXX.XXX")
  }

  test("parseLogLine should return None for invalid log line") {
    val invalidLogLine = "invalid log line format"
    
    val result = cdnLogAnalyse.parseLogLine(invalidLogLine)
    
    assertEquals(result, None)
  }
}