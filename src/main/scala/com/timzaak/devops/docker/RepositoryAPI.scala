package com.timzaak.devops.docker

import io.circe.*
import io.circe.config.syntax.*
import io.circe.optics.JsonPath.*
import sttp.client4.*
import sttp.client4.circe.*

import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Base64
import scala.util.Try

// 配置类
case class Config(
  url: String,
  username: String,
  password: String,
)

// 镜像标签信息
case class ImageTag(
  tag: String,
  digest: String,
  lastModified: Instant
)

class RepositoryAPI(config:Config) {

  // 使用 circe optics 定义 JSON 路径
  private object DockerRegistryJson {
    val repositories = root.repositories.each.string
    val tags = root.tags.each.string
    val history = root.history.each.json
  }

  // 获取所有仓库列表 - 使用 optics 提取 repositories 数组
  def getRepositories()(using backend: WebSocketSyncBackend): List[String] = {
    val request = basicRequest
      .get(uri"${config.url}/v2/_catalog")
      .headers(authHeaders())
      .response(asJson[Json])

    val response = request.send(backend)

    response.body match {
      case Right(json) =>
        DockerRegistryJson.repositories.getAll(json)
      case Left(error) =>
        throw new RuntimeException(s"Failed to get repositories: $error")
    }
  }


  // 获取仓库的所有标签 - 使用 optics 提取 tags 数组
  def getTags(repository: String)(using backend: WebSocketSyncBackend): List[String] = {
    val request = basicRequest
      .get(uri"${config.url}/v2/$repository/tags/list")
      .headers(authHeaders())
      .response(asJson[Json])

    val response = request.send(backend)

    response.body match {
      case Right(json) =>
        DockerRegistryJson.tags.getAll(json)
      case Left(error) =>
        throw new RuntimeException(s"Failed to get tags for $repository: $error")
    }
  }

  private val formatter = DateTimeFormatter.RFC_1123_DATE_TIME

  // 获取标签的详细信息 - 使用 optics 提取嵌套的 created 时间
  def getImageTagDetails(repository: String, tag: String)(using
    backend: WebSocketSyncBackend
  ): Try[ImageTag] = {
    val request = basicRequest
      .get(uri"${config.url}/v2/$repository/manifests/$tag")
      .headers(authHeaders() ++ Map("Accept" -> "application/vnd.docker.distribution.manifest.v2+json"))
      .response(asJson[Json])

    val response = request.send(backend)

    val lastModifiedTry =
      response.header("last-modified").toRight(new RuntimeException("No last-modified header found")).toTry.flatMap {
        v =>
          Try(Instant.from(formatter.parse(v)))
      }
    for {
      digest <- response.header("Docker-Content-Digest").toRight(new RuntimeException("No digest header found")).toTry
      lastModified <- lastModifiedTry
    } yield ImageTag(tag, digest, lastModified)
  }

  // 删除镜像
  def deleteImage(repository: String, digest: String)(using
    backend: WebSocketSyncBackend
  ): Unit = {
    val request = basicRequest
      .delete(uri"${config.url}/v2/$repository/manifests/$digest")
      .headers(authHeaders())

    val response = request.send(backend)

    if (!response.code.isSuccess) {
      throw new RuntimeException(s"Failed to delete $repository@$digest: ${response.statusText}")
    }
  }

  // 构建认证头
  private def authHeaders(): Map[String, String] = {
    import config.*
    val auth = Base64.getEncoder.encodeToString(s"$username:$password".getBytes)
    Map("Authorization" -> s"Basic $auth")
  }
}
