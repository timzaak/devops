package com.timzaak.devops.extra

import com.sshtools.client.shell.ShellProcess
import com.timzaak.devops.client.ExpectShell
import com.typesafe.scalalogging.Logger

import scala.io.Source

object ShellExtra {
  private val outputLog: Logger = Logger("output")
  private val commandLog: Logger = Logger("command")

  extension (command: String) {
    inline def !(using shell: ExpectShell): CodeWrap = {
      commandLog.info(command)
      shell.run(command)
    }
    inline def !!(using shell: ExpectShell): Unit = {
      mustOK(command.!)
    }

    inline def sudo(using shell: ExpectShell): CodeWrap = {
      commandLog.info(s"sudo $command")
      shell.sudoRun(command)
    }

    inline def `sudo!`(using shell: ExpectShell): Unit = {
      mustOK(command.sudo)
    }

  }

  extension (shell: ExpectShell) {
    def run(command: String): CodeWrap = {
      val process = shell.shell.executeCommand(command)
      process.output()
      process.drain()
      CodeWrap(process.getExitCode)
    }
    def sudoRun(command: String): CodeWrap = {
      val password = shell.rootPassword.get
      val process = shell.shell.sudo(command, password)
      // process.output() this would blocked, don't know why
      process.drain()
      outputLog.info(process.getCommandOutput)
      CodeWrap(process.getExitCode)
    }

    def setEnv(env: Map[String, String], outputContent: Boolean = true): Unit = {
      if (!outputContent) {
        commandLog.info(s"Load environments: ${env.keys.mkString(",")}")
      } else {
        commandLog.info(s"Load environments:\n${env.map((k, v) => s"$k=$v").mkString("\n")}")
      }
      for ((k, v) <- env) {
        shell.shell.execute(s"""export $k=$v""")
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
