import com.typesafe.config.ConfigFactory
import io.circe.*
import io.circe.config.syntax.*
import io.circe.generic.auto.*
import io.circe.optics.JsonPath.*
import sttp.client4.*
import sttp.client4.circe.*

import java.time.Instant
import java.util.Base64
import scala.util.{ Failure, Try }


object DockerRegistryCleaner {

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
                       created: Instant
                     )

  // 使用 circe optics 定义 JSON 路径
  object DockerRegistryJson {
    val repositories = root.repositories.each.string
    val tags = root.tags.each.string
    val history = root.history.each.json
    val v1Compatibility = root.v1Compatibility.string
    val created = root.created.string
  }

  // 主清理方法
  def cleanRepositories(config: Config): Unit = {
    given backend: WebSocketSyncBackend = DefaultSyncBackend()
    try {
      val repositories = getRepositories(config)
      repositories.foreach { repo =>
        println(s"Processing repository: $repo")
        processRepository(config, repo)
      }
      println("Cleanup completed successfully.")
    } catch {
      case e: Exception =>
        println(s"Error during cleanup: ${e.getMessage}")
        e.printStackTrace()
    } finally {
      backend.close()
    }
  }

  // 获取所有仓库列表 - 使用 optics 提取 repositories 数组
  private def getRepositories(config: Config)(implicit backend: WebSocketSyncBackend): List[String] = {
    val request = basicRequest
      .get(uri"${config.url}/v2/_catalog")
      .headers(authHeaders(config))
      .response(asJson[Json])

    val response = request.send(backend)

    response.body match {
      case Right(json) =>
        DockerRegistryJson.repositories.getAll(json)
      case Left(error) =>
        throw new RuntimeException(s"Failed to get repositories: $error")
    }
  }

  // 处理单个仓库
  private def processRepository(config: Config, repository: String)(implicit backend:WebSocketSyncBackend): Unit = {
    val tags = getTags(config, repository)

    if (tags.size > 10) {
      val imageTags = tags.flatMap { tag =>
        getImageTagDetails(config, repository, tag).toOption
      }.sortBy(_.created) // 按创建时间排序

      // 计算需要删除的数量
      val tagsToDelete = imageTags.size - 10
      if (tagsToDelete > 0) {
        println(s"Repository $repository has ${tags.size} tags, deleting $tagsToDelete oldest...")

        imageTags.take(tagsToDelete).foreach { imageTag =>
          deleteImage(config, repository, imageTag.digest)
          println(s"Deleted: $repository:${imageTag.tag} (Created: ${imageTag.created})")
        }
      }
    } else {
      println(s"$repository has no more than 10 tags")
    }

  }

  // 获取仓库的所有标签 - 使用 optics 提取 tags 数组
  private def getTags(config: Config, repository: String)(implicit backend: WebSocketSyncBackend): List[String] = {
    val request = basicRequest
      .get(uri"${config.url}/v2/$repository/tags/list")
      .headers(authHeaders(config))
      .response(asJson[Json])

    val response = request.send(backend)

    response.body match {
      case Right(json) =>
        DockerRegistryJson.tags.getAll(json)
      case Left(error) =>
        throw new RuntimeException(s"Failed to get tags for $repository: $error")
    }
  }

  // 获取标签的详细信息 - 使用 optics 提取嵌套的 created 时间
  private def getImageTagDetails(config: Config, repository: String, tag: String)(implicit backend: WebSocketSyncBackend): Try[ImageTag] = {
    val request = basicRequest
      .get(uri"${config.url}/v2/$repository/manifests/$tag")
      .headers(authHeaders(config) ++ Map("Accept" -> "application/vnd.docker.distribution.manifest.v2+json"))
      .response(asJson[Json])

    val response = request.send(backend)

    for {
      json <- response.body.toTry
      digest <- response.header("Docker-Content-Digest").toRight(new RuntimeException("No digest header found")).toTry
      created <- extractCreatedTime(json)
    } yield ImageTag(tag, digest, created)
  }

  // 使用 optics 从嵌套 JSON 中提取 created 时间
  private def extractCreatedTime(json: Json): Try[Instant] = {
    // 获取第一个 history 项的 v1Compatibility 字符串
    val v1CompatOpt = DockerRegistryJson.history
      .headOption(json)
      .flatMap(DockerRegistryJson.v1Compatibility.headOption)

    // 解析 v1Compatibility JSON 并提取 created 时间
    val createdOpt = v1CompatOpt.flatMap { v1Str =>
      io.circe.parser.parse(v1Str).toOption
        .flatMap(DockerRegistryJson.created.headOption)
    }

    createdOpt
      .map(s => Try(Instant.parse(s)))
      .getOrElse(Failure(new RuntimeException("Failed to extract created time")))
  }

  // 删除镜像
  private def deleteImage(config: Config, repository: String, digest: String)(implicit backend: WebSocketSyncBackend): Unit = {
    val request = basicRequest
      .delete(uri"${config.url}/v2/$repository/manifests/$digest")
      .headers(authHeaders(config))

    val response = request.send(backend)

    if (!response.code.isSuccess) {
      throw new RuntimeException(s"Failed to delete $repository@$digest: ${response.statusText}")
    }
  }

  // 构建认证头
  private def authHeaders(config: Config): Map[String, String] = {
    import config.*
    val auth = Base64.getEncoder.encodeToString(s"$username:$password".getBytes)
    Map("Authorization" -> s"Basic $auth")
  }

}


@main def oldDockerClean(): Unit = {
  import DockerRegistryCleaner.*
  val config = ConfigFactory.load().getConfig("repository.docker").as[Config].toTry.get
  cleanRepositories(config)

}