package com.timzaak.devops

import com.sshtools.client.SshClient.SshClientBuilder
import com.sshtools.client.shell.ExpectShell
import com.sshtools.client.tasks.ShellTask

import scala.io.Source


@main def localSSH() = {
  val ssh = SshClientBuilder.create()
    .withHostname("localhost")
    .withPort(22)
    .withUsername("timzaak")
    .withPassword("")
    .build()

  //println(ssh.executeCommand("docker ps"))

  ssh.runTask(ShellTask.ShellTaskBuilder.create()
    .withClient(ssh)
    .onTask((t,session) => {
      val shell = new ExpectShell(t)
      var process = shell.executeCommand("docker ps")
      Source.fromInputStream(process.getInputStream).getLines().foreach(println)
      process = shell.sudo("sudo ls", "")
      Source.fromInputStream(process.getInputStream).getLines().foreach(println)
      //process.drain()

    })
    .build())
}
