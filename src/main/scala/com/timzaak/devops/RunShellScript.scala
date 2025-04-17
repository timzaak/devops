package com.timzaak.devops

import better.files.File
import com.timzaak.devops.config.{SSHClientBuild, SSHClientConfig}
import com.typesafe.config.{Config, ConfigFactory}

import scala.jdk.CollectionConverters.*


@main def runShellScript() = {
  given conf: Config = ConfigFactory.load()

  val scriptConf = conf.getConfig("scripts.test")
  val filters = (v: SSHClientConfig) => v.name == "monitor"


  val scriptContent = File(scriptConf.getString("file")).contentAsString
  val env = scriptConf.getConfig("env").entrySet().asScala.map { entry =>
    entry.getKey -> entry.getValue.unwrapped().toString
  }.toMap

  for (client <- SSHClientBuild.loadFromConfig("office.servers", filters)) {
    client.runScript(scriptContent, env)
  }

}