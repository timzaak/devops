package com.timzaak.devops.extra

import com.typesafe.scalalogging.Logger

import java.io.File
import sys.process.{Process, ProcessLogger}


class ProcessLoggerWrap extends ProcessLogger {
  private val outputLog: Logger = Logger("output")
  override def out(s: => String): Unit = outputLog.info(s)

  override def err(s: => String): Unit = outputLog.error(s)

  override def buffer[T](f: => T): T = f
}

private val processLogWrap: ProcessLogger = new ProcessLoggerWrap()
private val commandLog: Logger = Logger("command")

object LocalSession {
  lazy val isWindows: Boolean = {
    System.getProperty("os.name").toLowerCase.contains("win")
  }
}

class LocalSession(val env:(String,String)*) {
  var cwd :Option[File] = None


  def cd(path:String): Unit = {
    commandLog.info(s"cd $path")
    val newCwd = new File(path)
    assert(newCwd.isDirectory, s"path does not exists")
    cwd = Some(new File(path))
  }
  def runScripts(filePath:String): Unit = {
    given LocalSession = this
    import LocalProcessExtra.!!

    if (LocalSession.isWindows) {
      mustOK(s"""powershell -WindowStyle Hidden -File "$filePath" """.!!)
    } else {
      // TODO:support sh 、bash、fish ?
      mustOK(s"bash \"$filePath\"".!!)
    }
    
  }

}
object LocalProcessExtra {
  def localRun(env: (String, String)*)(function: LocalSession => Unit): Unit = {
    function(LocalSession(env*))
  }
  def localRun(function: LocalSession => Unit):Unit = {
    function(LocalSession())
  }

  inline def cd(path: String)(using localHost: LocalSession): Unit = localHost.cd(path)

  inline def runScripts(scriptContent: String)(using localHost: LocalSession): Unit =
    localHost.runScripts(scriptContent)


  extension (command: String)(using localHost: LocalSession) {
    def ! : CodeWrap = {
      commandLog.info(command)
      CodeWrap(Process(command, localHost.cwd, localHost.env*).!(processLogWrap))
    }

    def !! : CodeWrap = {
      commandLog.info(command)

      CodeWrap(Process(command, localHost.cwd, localHost.env*).!(processLogWrap))
    }
    
    def &&(other:String): CodeWrap = {
      commandLog.info(s"$command && $other")
      CodeWrap(Process(command, localHost.cwd, localHost.env*).!(processLogWrap)) && CodeWrap(Process(other, localHost.cwd, localHost.env*).!(processLogWrap))
    }


  }
}