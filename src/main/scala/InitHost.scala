import com.timzaak.devops.shell.extra.ShellExtra.*
import com.timzaak.devops.shell.config.{ SSHClientBuild, SSHClientConfig }
import com.timzaak.devops.shell.extra.mustOK
import com.typesafe.config.{ Config, ConfigFactory }

@main def initHost(): Unit = {
  given conf: Config = ConfigFactory.load()

  val publicKey = conf.getString("office.devops.public")
  def filters = (v: SSHClientConfig) => v.name != "monitor" && v.name != "jenkins"

  for (client <- SSHClientBuild.loadFromConfig("office.servers", filters)) {
    client.run { implicit shell =>
      // set ssh public keys
      mustOK("mkdir -p ~/.ssh".! && s"""echo "$publicKey" >> ~/.ssh/authorized_keys""".!)
      "cat ~/.ssh/authorized_keys".!!
    }
  }
}
