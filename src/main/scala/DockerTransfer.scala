import com.typesafe.config.ConfigFactory

//runMain DockerTransfer qingpan/rnacos:v0.6.14

object DockerTransfer {
  def main(args: Array[String]) = {
    val host = ConfigFactory.load().getString("repository.docker.host")

    localRun { implicit session =>
      args.foreach { image =>
        s"docker pull ${image}".!!
        val newImageName = image.replace("quay.io/", "")
        val target = s"${host}/${newImageName}"

        s"docker tag ${image} ${target}".!!
        s"docker push ${target}".!!
        s"docker rmi ${image}".!!
        s"docker rmi ${target}".!!
      }
    }
  }
}
