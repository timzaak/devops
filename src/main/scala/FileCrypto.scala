import com.timzaak.devops.backup.FileCrypto as FileCryptTool
import better.files.*
import mainargs.{ ParserForMethods, arg, main }

import java.util.Base64

type CompressType = "none" | "zip" | "tar"
type Mode = "decrypt"| "encrypt"

object FileCrypto {
  @main
  def argsRun(
    @arg(name = "source") _source: String,
    @arg(name = "target") _target: Option[String],
    @arg(name = "key") _key: Option[String],
    @arg(name = "mode", doc = "encrypt or decrypt, default is encrypt") _mode: Option[String],
  ): Unit = {
    val source = File(_source)
    assert(source.exists, s"source: ${_source} does not exists")

    val mode = _mode.getOrElse("encrypt") match {
      case v :Mode => v
      case _ => throw IllegalArgumentException("mode should be one of encrypt or decrypt")
    }

    val targetPath = mode match {
      case "decrypt" =>
        _target.getOrElse(_source.take(_source.lastIndexOf(".enc")))
      case "encrypt" =>
        _target.getOrElse(_source + ".enc")

    }

    assert(targetPath != _source, "target path should be set")
    val target = File(targetPath)
    assert(target.notExists, s"target: ${_target} already exists")
    assert(target.parent.exists, s"target parent path ${target.parent} does not exists")

    val key64 = _key.map(Base64.getDecoder.decode)

    mode match {
      case "decrypt" =>
        decrypt(source, target, key64.getOrElse(throw IllegalArgumentException("key should not be empty")))
      case "encrypt" =>
        val key = key64.getOrElse {
          val newKey = FileCryptTool.generateKey()
          println(s"generate key: ${Base64.getEncoder.encodeToString(newKey)}\nplease save it.")
          newKey
        }
        encrypt(source, target, key)
    }


  }

  private def encrypt(source: File, target: File, key: Array[Byte]): Unit = {
    FileCryptTool.encryptFile(source.pathAsString, target.pathAsString, key)

  }

  private def decrypt(source: File, target: File, key: Array[Byte]): Unit = {
    FileCryptTool.decryptFile(source.pathAsString, target.pathAsString, key)
  }

  def main(args: Array[String]): Unit = {
    ParserForMethods(this).runOrExit(args)
  }

}
