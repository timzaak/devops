package com.timzaak.devops

import com.timzaak.devops.scripts.ConfigShellRun
import munit.FunSuite

class WindowsShellSuite extends FunSuite {
  test("stop when error happen") {

    ConfigShellRun.localRun("windows.test1")
  }

}
