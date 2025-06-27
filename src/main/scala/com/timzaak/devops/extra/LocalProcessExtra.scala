package com.timzaak.devops.extra

import com.timzaak.devops.extra.LocalSession.isWindows

import java.io.{ File, OutputStream }
import sys.process.Process

object LocalSession {
  lazy val isWindows: Boolean = {
    System.getProperty("os.name").toLowerCase.contains("win")
  }
}

class LocalSession(val env: (String, String)*)(val log: ProcessOutput = LocalProcessExtra.defaultProcessOutput) {
  var cwd: Option[File] = None

  def cd(path: String): Unit = {
    log.command(s"cd $path")
    val newCwd = new File(path)
    assert(newCwd.isDirectory, s"path does not exists")
    cwd = Some(new File(path))
  }
  def runScripts(filePath: String): Unit = {
    given LocalSession = this
    import LocalProcessExtra.!!
    if (isWindows) {
      s"""powershell -WindowStyle Hidden -File "${filePath}" """.!!
    } else {
      // TODO:support sh 、bash、fish ?
      s"bash \"${filePath}\"".!!
    }
  }

}
object LocalProcessExtra {
  val defaultProcessOutput = LogProcessOutput()
  def localRun(
    env: (String, String)*
  )(function: LocalSession => Unit, log: ProcessOutput = defaultProcessOutput): Unit = {
    function(LocalSession(env*)(log))
  }
  def localRun(function: LocalSession => Unit): Unit = {
    function(LocalSession()(defaultProcessOutput))
  }

  inline def cd(path: String)(using localHost: LocalSession): Unit = localHost.cd(path)

  inline def runScripts(scriptContent: String)(using localHost: LocalSession): Unit =
    localHost.runScripts(scriptContent)

  extension (command: String)(using localHost: LocalSession) {
    def ! : CodeWrap = {
      localHost.log.command(command)
      CodeWrap(Process(command, localHost.cwd, localHost.env*).!(localHost.log.process))
    }

    def !! : Unit = {
      localHost.log.command(command)
      mustOK(CodeWrap(Process(command, localHost.cwd, localHost.env*).!(localHost.log.process)))
    }

    def #>(file: File) = {
      localHost.log.command(command + " > " + file.getAbsolutePath)
      Process(command, localHost.cwd, localHost.env*).#>(file)
    }

    def #>(out: => OutputStream) = {
      localHost.log.command(command + s" > $${param}")
      Process(command, localHost.cwd, localHost.env*).#>(out)
    }

    def &&(other: String): CodeWrap = {
      localHost.log.command(s"$command && $other")
      CodeWrap(Process(command, localHost.cwd, localHost.env*).!(localHost.log.process)) && CodeWrap(
        Process(other, localHost.cwd, localHost.env*).!(localHost.log.process)
      )
    }

    def c: CodeWrap = {
      if (isWindows) {
        localHost.log.command(s"powershell -c $command")
        CodeWrap(Process(Seq("powershell", "-c", command), localHost.cwd, localHost.env*).!(localHost.log.process))
      } else {
        localHost.log.command(s"$command")
        CodeWrap(Process(command, localHost.cwd, localHost.env*).!(localHost.log.process))
      }
    }
    def `c!`: Unit = {
      mustOK(c)
    }

  }
}
