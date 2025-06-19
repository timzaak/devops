import com.timzaak.devops.scripts.ConfigShellRun


object RunLocalScript {
  def main(args: Array[String]): Unit = {
    ConfigShellRun.localRun(args(0))
  }
}
