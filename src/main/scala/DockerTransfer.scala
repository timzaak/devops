import com.timzaak.devops.extra.LocalProcessExtra.*
import com.typesafe.config.ConfigFactory



//runMain DockerTransfer qingpan/rnacos:v0.6.14

object DockerTransfer {
  def main(args: Array[String]) = {
    val host = ConfigFactory.load().getString("repository.docker.host")
    val image = args(0)

    localRun { implicit session =>
      s"docker pull ${image}".!!
      s"docker tag ${image} ${host}/${image}".!!
      s"docker push ${host}/${image}".!!
    }

  }
}