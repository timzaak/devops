package com.timzaak.devops.parser

import scala.jdk.CollectionConverters.*

object SimpleShellParser {
  def parser(script:String): List[String] = {
    val lines = script.lines().toList.asScala.toList.map(_.trim).filter { str =>
      str.nonEmpty && !str.startsWith("#")
    }
    assert(lines.nonEmpty)

    var result = List.empty[String]
    var buffer = ""
    for (line <- lines) {
      if(lines.endsWith("\\")) {
        buffer += " " + line.dropRight(1)
      } else {
        if(buffer.nonEmpty) {
          result = buffer::result
          buffer = ""
        }
         result = line::result
      }
    }
    result.reverse
  }
}


