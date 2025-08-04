package com.timzaak.devops.shell.scripts

import better.files.File
import com.timzaak.devops.shell.config.{SSHClientBuild, SSHClientConfig}
import com.timzaak.devops.shell.extra.LocalProcessExtra.*
import com.timzaak.devops.shell.extra.LocalProcessExtra
import com.typesafe.config.{ Config, ConfigFactory }

import scala.jdk.CollectionConverters.*

object ConfigShellRun {

  /**
   * config would like:
   * {
   *   host: "test_net"
   *   file: "scripts/deploy_backend.sh"
   *   env: {
   *     VERSION = 1.1.8
   *   }  
   * }
   */
  def remoteRun(prefix: String, extraEnv: Map[String, String] = Map.empty)(using
    conf: Config = ConfigFactory.load()
  ): Unit = {
    val scriptConf = conf.getConfig(prefix)
    val hostName = scriptConf.getString("host")
    val filters = (v: SSHClientConfig) => v.name == hostName
    val scriptContent = File(scriptConf.getString("file")).contentAsString
    val env = if (scriptConf.hasPath("env")) {
      scriptConf
        .getConfig("env")
        .entrySet()
        .asScala
        .map { entry =>
          entry.getKey -> entry.getValue.unwrapped().toString
        }
        .toMap ++ extraEnv
    } else {
      Map.empty
    }
    for (client <- SSHClientBuild.loadFromConfig("office.servers", filters)) {
      client.runScript(scriptContent, env)
    }
  }

  def localRun(prefix: String, extraEnv: Map[String, String] = Map.empty)(using
    conf: Config = ConfigFactory.load()
  ): Unit = {
    val scriptConf = conf.getConfig(prefix)
    val path = File(scriptConf.getString("file")).path.toAbsolutePath.toString
    val env = if (scriptConf.hasPath("env")) {
      scriptConf
        .getConfig("env")
        .entrySet()
        .asScala
        .map { entry =>
          entry.getKey -> entry.getValue.unwrapped().toString
        }
        .toMap ++ extraEnv
    } else {
      Map.empty
    }
    val workDir = scriptConf.getString("workDir")
    LocalProcessExtra.localRun(env.toSeq*) { implicit session =>
      cd(workDir)
      runScripts(path)
    }
  }
}
