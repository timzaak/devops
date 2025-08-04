import com.timzaak.devops.k8s.Kubectl
import com.timzaak.devops.shell.config.{SSHClientBuild, SSHClientConfig}
import com.typesafe.config.{ Config, ConfigFactory }

object RenewSSLCert {
  def main(args: Array[String]): Unit = {

    given conf: Config = ConfigFactory.load()

    val basePath = args(0) // path to store cert and key
    val domain = args(1) // example.com
    localRun { implicit shell =>
      s"docker run --rm neilpang/acme.sh -v \"$basePath\":/out --issue --server letsencrypt " +
        s"--cert-file /out/$domain.cer --key-file /out/$domain.key " +
        s"-e Ali_Key=${conf.getString("ssl.AliKey")} -e Ali_Secret=${conf.getString("ssl.AliSecret")} " +
        s"--dns dns_ali -d $domain -d '*.$domain'".!!
    }

    /*
    val cerPath = basePath + s"\\$domain.cer"
    val keyPath = basePath + s"\\$domain.key"

    // update nginx
    for (client <- SSHClientBuild.loadFromConfig("office.servers", _.name == "target")) {
      import com.timzaak.devops.extra.ShellExtra.*
      val scpClient = client.scpClient
      scpClient.put(cerPath, "/server/conf/nginx/ssl/", false)
      scpClient.put(keyPath, "/server/conf/nginx/ssl/", false)
      client.run { implicit shell =>
        """docker restart nginx""".!!
      }
    }

    // update k8s
    Seq("namespace").foreach { namespace =>
      Kubectl(namespace).upgradeTLS(domain, cerPath, keyPath)
    }
     */
  }
}
