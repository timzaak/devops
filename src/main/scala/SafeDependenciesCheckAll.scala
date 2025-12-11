import com.timzaak.devops.safe.TrivyScan
import com.typesafe.config.{ Config, ConfigFactory }

import scala.jdk.CollectionConverters.given

object SafeDependenciesCheckAll {

  def main(args: Array[String]): Unit = {
    val ignoreProject = args.toSet


    val config: Config = ConfigFactory.load()
    //scan project
    for (prefix <- config.getObject(s"scripts.deploy").keySet().asScala) {
      if (!ignoreProject.contains(prefix)) {
        println(s"=============Checking $prefix")
        TrivyScan(config.getString("scripts.cache.trivy")).fsScan(config.getString(s"scripts.deploy.$prefix.local.workDir"))
      } else {
        println(s"=============Ignoring $prefix")
      }
    }
    //scan images


  }
}
