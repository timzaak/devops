package com.timzaak.devops.extra

import com.typesafe.scalalogging.Logger
import scala.sys.process.ProcessLogger

trait ProcessOutput {
  def process: ProcessLogger
  def command: String => Unit
}


class LogProcessOutput extends ProcessOutput {

  private val commandLog = Logger("command")

  private class ProcessLoggerWrap extends ProcessLogger {
    private val processLog: Logger = Logger("output")

    override def out(s: => String): Unit = processLog.info(s)

    override def err(s: => String): Unit = processLog.error(s)

    override def buffer[T](f: => T): T = f
  }

  val process: ProcessLogger = new ProcessLoggerWrap()

  val command: String => Unit = (s: String) => commandLog.info(s)
}
