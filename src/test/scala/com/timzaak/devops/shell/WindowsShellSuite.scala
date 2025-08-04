package com.timzaak.devops.shell

import com.timzaak.devops.shell.scripts.ConfigShellRun
import munit.FunSuite

class WindowsShellSuite extends FunSuite {
  test("stop when error happen".ignore) {

    ConfigShellRun.localRun("windows.test1")
  }

}
