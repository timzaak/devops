import com.timzaak.devops.k8s.Kubectl
import com.timzaak.devops.shell.config.{ SSHClientBuild, SSHClientConfig }
import com.typesafe.config.{ Config, ConfigFactory }
import com.timzaak.devops.shell.extra.LocalProcessExtra.*

import java.net.InetSocketAddress
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.net.ssl.{ SNIHostName, SSLContext, SSLSocket }

object RenewSSLCert {
  def main(args: Array[String]): Unit = {

    given conf: Config = ConfigFactory.load()

    val basePath = args(0) // path to store cert and key
    val domain = args(1) // example.com

    val date = getSSLExpire(domain)
    val duration = ChronoUnit.DAYS.between(
      Instant.now(),
      date.toInstant
    )
    if (duration >= 15) {
      println(s"cert expired after $duration days")
      return
    }

    localRun { implicit shell =>
      s"""docker run --rm -v "$basePath":/out 
         |-e Ali_Key=${conf.getString("ssl.AliKey")} -e Ali_Secret=${conf.getString("ssl.AliSecret")} 
         |neilpang/acme.sh --issue --server letsencrypt --fullchain-file /out/$domain.cer --key-file /out/$domain.key --dns dns_ali -d $domain -d '*.$domain'
         |""".stripMargin.!!
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

  private def getSSLExpire(host: String, port: Int = 443): java.util.Date = {
    val context = SSLContext.getInstance("TLS")
    context.init(null, null, null)

    val socket = context.getSocketFactory.createSocket().asInstanceOf[SSLSocket]
    val params = socket.getSSLParameters
    params.setServerNames(java.util.List.of(new SNIHostName(host)))
    socket.setSSLParameters(params)

    socket.connect(new InetSocketAddress(host, port), 5000)
    socket.startHandshake()

    val cert = socket.getSession
      .getPeerCertificates()(0)
      .asInstanceOf[java.security.cert.X509Certificate]

    cert.getNotAfter
  }

}
