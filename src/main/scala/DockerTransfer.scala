import com.timzaak.devops.docker.{ Config, RepositoryAPI }
import com.typesafe.config.ConfigFactory
import sttp.client4.{ DefaultSyncBackend, WebSocketSyncBackend }
import io.circe.*
import io.circe.config.syntax.*
import io.circe.generic.auto.*
//runMain DockerTransfer qingpan/rnacos:v0.6.14
import com.timzaak.devops.shell.extra.LocalProcessExtra.*

object DockerTransfer {
  def main(args: Array[String]) = {
    val arg :: tails = args.toList: @unchecked

    arg match {
      case "pull" => publicImageToPrivate(tails)
      case "push" => imageToAliCloud(tails*)
    }

  }

  def imageToAliCloud(args: String*) = {
    val c = ConfigFactory.load()
    val host = c.getString("repository.private.office.host")
    val aliHost = c.getString("repository.private.aliCloud.host")

    given backend: WebSocketSyncBackend = DefaultSyncBackend()

    val api = RepositoryAPI(c.getConfig("repository.private.office").as[Config].toTry.get)
    var images = List.empty[String]
    localRun { implicit session =>
      args.foreach { name =>
        val image = name // do nothing....
        val originImage = if (!image.contains(":")) {
          val tags = api.getTags(image)
          val newestTag = tags.flatMap(tag => api.getImageTagDetails(image, tag).toOption).maxBy(_.lastModified).tag
          s"$image:$newestTag"
        } else {
          image
        }
        val oldImage = s"$host/$originImage"
        val newImage = s"$aliHost/$originImage"
        s"docker pull $oldImage".!!
        s"docker tag $oldImage $newImage".!!
        s"docker push $newImage".!!
        images = newImage :: images
      }
    }
    images.reverse

  }

  private def publicImageToPrivate(args: List[String]) = {
    val host = ConfigFactory.load().getString("repository.private.office.host")
    localRun { implicit session =>
      args.foreach { image =>
        s"docker pull ${image}".!!
        val newImageName = image // .replace("quay.io/", "").replace("ghcr.io/","")
        val target = s"${host}/${newImageName}"

        s"docker tag ${image} ${target}".!!
        s"docker push ${target}".!!
        s"docker rmi ${image}".!!
        s"docker rmi ${target}".!!
        println(target)
      }
    }
  }
}
