import com.timzaak.devops.scripts.ConfigShellRun

object RunRemoteScript {

  def main(args: Array[String]): Unit = {
    ConfigShellRun.remoteRun(args(0))
  }
}