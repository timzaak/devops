package com.timzaak.devops.backup

import better.files.*
import sttp.client4.*

class RNacosBackup(url: String, token: String) {

  private val client = DefaultSyncBackend()

  def backRNacos(storage: File) = {
    val result = basicRequest
      .get(uri"$url/rnacos/backup?token=$token")
      .response(asByteArrayAlways)
      .send(client)
    if (result.isSuccess) {
      storage.createFileIfNotExists(true)
      storage.writeByteArray(result.body)

    }
    result
  }
}
