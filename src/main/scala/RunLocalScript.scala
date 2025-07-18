import com.timzaak.devops.scripts.ConfigShellRun

object RunLocalScript {
  def main(args: Array[String]): Unit = {
    val env = args.tail.map { v =>
      val Array(key, value) = v.split("=", 2)
      key -> value
    }.toMap
    ConfigShellRun.localRun(args(0), env)
  }
}
