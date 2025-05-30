package com.timzaak.devops.backup

import better.files.*
import munit.FunSuite

class FileCryptoSuite extends FunSuite {
  test("encrypt and decrypt") {
    val key = FileCrypto.generateKey()
    val targetFile = File.newTemporaryFile()

    File.usingTemporaryFile() { tempFile =>
      tempFile.writeText("hello world xx123")
      val source = tempFile.pathAsString
      val target = targetFile.pathAsString
      FileCrypto.encryptFile(source, target, key)
      File.usingTemporaryFile() { endFile =>
        FileCrypto.decryptFile(target, endFile.pathAsString, key)
        targetFile.delete()
        assertEquals(endFile.contentAsString(), "hello world xx123")
      }
    }
  }

}
