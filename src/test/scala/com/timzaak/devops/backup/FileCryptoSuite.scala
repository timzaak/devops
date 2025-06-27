package com.timzaak.devops.backup

import better.files.*
import munit.FunSuite

import java.util.Base64

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

  test("generate key".ignore) {
    val key = FileCrypto.generateKey()
    // "40VjYwTbqyg4Oy7ViTnDYuTAgcBG60yHgWgD+v3QXz0="
    println(Base64.getEncoder.encodeToString(key))
  }

}
