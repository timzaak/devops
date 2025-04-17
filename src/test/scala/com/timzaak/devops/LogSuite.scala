package com.timzaak.devops

import munit.FunSuite
import com.typesafe.scalalogging.Logger


class LogSuite extends FunSuite {

  val output = Logger("output")
  val command = Logger("command")

  test("log output format") {
    command.info("echo 'hello world'")
    output.info("hello world")


  }
}
