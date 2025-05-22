package com.timzaak.devops.extra

import java.time.format.DateTimeFormatter
import java.time.{ LocalDate, LocalDateTime }

object TimeExtra {
  def todayString: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))

  def nowMinuteString: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))

}


