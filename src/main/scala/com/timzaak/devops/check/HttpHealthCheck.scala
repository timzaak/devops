package com.timzaak.devops.check

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class HttpHealthCheck(url: String) extends HealthCheck {
  override def check(): Boolean = {
    println(s"Checking URL: $url")
    if (!url.startsWith("http://") && !url.startsWith("https://")) {
      println(s"Invalid URL format: $url")
      return false
    }

    try {
      val client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build()
      val request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
        .build()

      val response = client.send(request, HttpResponse.BodyHandlers.ofString())

      println(s"Response status code: ${response.statusCode()}")
      response.statusCode() >= 200 && response.statusCode() < 300
    } catch {
      case e: Exception =>
        println(s"Failed to connect to URL $url: ${e.getMessage}")
        false
    }
  }
}
