package com.timzaak.devops.safe

import com.timzaak.devops.shell.extra.LocalProcessExtra.*

class TrivyScan(cacheLayer: String, trivyImage: String = "aquasec/trivy:0.68.1") {
  def fsScan(path: String): Unit = {
    localRun { implicit shell =>
      s"""docker run --rm  -v $cacheLayer:/root/.cache/ -v $path:/data $trivyImage fs /data""".!!
    }
  }
  def imageScan(image: String): Unit = {
    localRun { implicit shell =>
      s"""docker run --rm  -v $cacheLayer:/root/.cache/ $trivyImage image $image""".!!
    }
  }
}
