package com.timzaak.devops.client

import com.sshtools.client.SshClient
import com.sshtools.client.scp.ScpClient
import com.sshtools.client.tasks.ShellTask
import com.typesafe.scalalogging.Logger
import com.timzaak.devops.config.SSHClientConfig
import com.timzaak.devops.extra.ShellExtra.*
import com.timzaak.devops.parser.SimpleShellParser

case class ExpectShell(shell: com.sshtools.client.shell.ExpectShell, rootPassword: Option[String])

case class SSHClient(client: SshClient, info: SSHClientConfig) {

  private val commandLog = Logger("command")

  def run(func: ExpectShell => Unit): Unit = {
    client.runTask(
      ShellTask.ShellTaskBuilder
        .create()
        .withClient(client)
        .onTask((t, session) => {
          val shell =
            ExpectShell(new com.sshtools.client.shell.ExpectShell(t), info.rootPassword.orElse(Some(info.password)))
          try {
            commandLog.info(s"Server: ${info.name} connected")
            func(shell)
          } catch {
            case e: Throwable =>
              client.close()
              throw e
          }
        })
        .build()
    )
    client.close()
  }

  def scpClient = new ScpClient(client)

  def runScript(scriptContent: String, env: Map[String, String] = Map.empty): Unit = {
    run { implicit shell =>
      val commands = SimpleShellParser.parser(scriptContent)
      commandLog.debug(s"shell scripts parse result:\n + ${commands.mkString("\n")}")
      if (env.nonEmpty) {
        shell.setEnv(env)
      }
      for (command <- commands) {
        if (command.startsWith("sudo")) {
          command.`sudo!`
        } else {
          command.!!
        }
      }
    }
  }

  export client.*
}
