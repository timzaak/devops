package com.timzaak.devops.extra

import com.sshtools.client.shell.{ExpectShell, ShellProcess}
import com.typesafe.scalalogging.Logger

import scala.io.Source


object ShellExtra {
  private val outputLog: Logger = Logger("output")
  private val commandLog: Logger = Logger("command")
  
  extension (command: String) {
    inline def !(using shell:ExpectShell): CodeWrap = {
      commandLog.info(command)
      shell.run(command)
    }
    inline def !!(using shell: ExpectShell): Unit = {
      mustOK(command.!)
    }
  }
  
  
  
  extension (shell:ExpectShell) {
    def run(command: String): CodeWrap = {
      val process = shell.executeCommand(command)
      process.output()
      process.drain()
      CodeWrap(process.getExitCode)
    }
  
    def setEnv(env:Map[String,String],outputContent:Boolean = true): Unit = {
      if(!outputContent){
        commandLog.info(s"Load environments: ${env.keys.mkString(",")}")
      } else {
        commandLog.info(s"Load environments:\n${env.map((k,v)=> s"$k=$v").mkString("\n")}")
      }
      for((k,v)<-env) {
        shell.execute(s"""export $k=$v""")
      }
      commandLog.info("Load environments done")
    }
  }
  
  extension (process: ShellProcess) {
    def output(): Unit = {
      Source.fromInputStream(process.getInputStream).getLines().foreach(outputLog.info)
    }
  }
}