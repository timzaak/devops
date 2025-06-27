package com.timzaak.devops.k8s

import better.files.File
import com.timzaak.devops.extra.LocalProcessExtra.*

import java.io.ByteArrayOutputStream
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
  def changeImage(service: String, image: String, `type`: "deployment" | "statefulSet" = "deployment"): Unit = {
    localRun { implicit session =>
      s"kubectl set image ${`type`}/$service $service=$image -n=$namespace".!!
    }
  }

  def rolloutStatus(service: String, `type`: "deployment" | "statefulSet" = "deployment"): Unit = {
    localRun { implicit session =>
      s"kubectl rollout status ${`type`}/$service".!!
    }
  }

  def upgradeTLS(name: String, certPath: String, keyPath: String): Unit = {
    localRun { implicit shell =>
      File.temporaryFile(suffix = ".yaml").foreach { f =>
        s"""kubectl create secret tls $name --cert="${certPath}" --key="${keyPath}" -n=$namespace --dry-run=client -o yaml"""
          .#>(f.toJava)
          .!!
        s"kubectl apply -f ${f.pathAsString}".!!
      }
      s"kubectl get secret $name -o yaml -n=$namespace".!!
    }
  }

  def scaleAllDeployments(num: Int = 0) = {
    localRun { implicit session =>
      val baos = ByteArrayOutputStream()
      "kubectl get deploy -o name".#>(baos).!!
      baos.toString.split("\n").foreach(name => s"kubectl scale $name --replicas=$num".!!)
    }
  }

}
