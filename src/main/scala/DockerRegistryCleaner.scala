import com.timzaak.devops.docker.{Config, RepositoryAPI}
import com.typesafe.config.ConfigFactory
import io.circe.*
import io.circe.config.syntax.*
import io.circe.generic.auto.*
import sttp.client4.*




object DockerRegistryCleaner {

  def main(args: Array[String]): Unit = {
    import DockerRegistryCleaner.*
    val config = ConfigFactory.load().getConfig("repository.private.office").as[Config].toTry.get
    cleanRepositories(RepositoryAPI(config))
  }

  // 处理单个仓库
  private def processRepository(api: RepositoryAPI, repository: String)(using backend: WebSocketSyncBackend): Unit = {
    val tags = api.getTags(repository)

    if (tags.size > 10) {
      val imageTags = tags
        .flatMap { tag =>
          api.getImageTagDetails(repository, tag).toOption
        }
        .sortBy(_.lastModified) // 按创建时间排序

      // 计算需要删除的数量
      val tagsToDelete = imageTags.size - 10
      if (tagsToDelete > 0) {
        println(s"Repository $repository has ${tags.size} tags, deleting $tagsToDelete oldest...")

        imageTags.take(tagsToDelete).foreach { imageTag =>
          api.deleteImage(repository, imageTag.digest)
          println(s"Deleted: $repository:${imageTag.tag} (Created: ${imageTag.lastModified})")
        }
      }
    } else {
      println(s"$repository has no more than 10 tags")
    }

  }

  // 主清理方法
  private def cleanRepositories(api:  RepositoryAPI): Unit = {
    given backend: WebSocketSyncBackend = DefaultSyncBackend()

    try {
      val repositories = api.getRepositories()
      repositories.foreach { repo =>
        println(s"Processing repository: $repo ============")
        processRepository(api, repo)
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

}
