package com.timzaak.devops.config

import com.typesafe.config.{ Config, ConfigFactory }
import io.circe.config.syntax.*
import com.sshtools.client.SshClient.SshClientBuilder as Builder
import com.timzaak.devops.client.SSHClient
import io.circe.derivation.{ Configuration, ConfiguredCodec }

import scala.jdk.CollectionConverters.*

given config: Configuration = Configuration.default.copy(
  useDefaults = true
)

case class SSHClientConfig(
  host: String,
  password: String,
  name: String,
  user: String = "root",
  port: Int = 22,
  rootPassword: Option[String] = None,
) derives ConfiguredCodec

object SSHClientConfig {
  def loadConfig(prefixKey: String, filter: SSHClientConfig => Boolean = _ => true)(using
    config: Config = ConfigFactory.load()
  ): List[SSHClientConfig] = {
    config
      .getConfigList(prefixKey)
      .asScala
      .map(_.as[SSHClientConfig])
      .collect {
        case Right(conf) if filter(conf) => conf
        case Left(error)                 => throw error
      }
      .toList
  }
}

object SSHClientBuild {
  def loadFromConfig(prefixKey: String, filter: SSHClientConfig => Boolean = _ => true)(using
    config: Config = ConfigFactory.load()
  ): List[SSHClient] = {
    SSHClientConfig.loadConfig(prefixKey, filter).map { conf =>
      import conf.*
      SSHClient(
        Builder
          .create()
          .withHostname(host)
          .withPort(port)
          .withPassword(password)
          .withUsername(user)
          .build(),
        conf
      )
    }
  }

}
