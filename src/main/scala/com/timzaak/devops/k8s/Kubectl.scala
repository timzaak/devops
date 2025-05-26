package com.timzaak.devops.k8s

import com.timzaak.devops.extra.LocalProcessExtra.*
// PS: local host should install kubectl and config correctly
class Kubectl(namespace: String, context: String = "default") {

  initContext()

  private def initContext(): Unit = {
    localRun { implicit session =>
      s"kubectl config use-context $context".!!
      s"kubectl config set-context --current --namespace=$namespace".!!
    }
  }

  // kubectl set image deployment/<deployment-name> <container-name>=<new-image>:<tag> -n=<namespace>
  def changeImage(service:String, image:String, `type`:"deployment"|"statefulSet" = "deployment"): Unit = {
    localRun { implicit session =>
      s"kubectl set image ${`type`}/$service $service=$image -n=$namespace".!!
    }
  }

  def rolloutStatus(service:String, `type`:"deployment"|"statefulSet" = "deployment"):Unit = {
    localRun{ implicit session =>
      s"kubectl rollout status ${`type`}/$service"
    }
  }

}
