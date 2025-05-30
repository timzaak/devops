package com.timzaak.devops.backup

import org.bouncycastle.jce.provider.BouncyCastleProvider

import java.io.{ FileInputStream, FileOutputStream }
import java.security.{ SecureRandom, Security }
import javax.crypto.spec.{ IvParameterSpec, SecretKeySpec }
import javax.crypto.{ Cipher, CipherInputStream, CipherOutputStream }

object FileCrypto {
  Security.addProvider(new BouncyCastleProvider())

  // 加密算法参数
  private val ALGORITHM = "AES"
  private val TRANSFORMATION = "AES/CBC/PKCS5Padding"
  private val KEY_SIZE = 256
  private val IV_SIZE = 16

  /**
   * 生成随机密钥
   * @return 生成的密钥字节数组
   */
  def generateKey(): Array[Byte] = {
    val key = new Array[Byte](KEY_SIZE / 8)
    new SecureRandom().nextBytes(key)
    key
  }

  /**
   * 生成随机初始化向量(IV)
   * @return 生成的IV字节数组
   */
  def generateIV(): Array[Byte] = {
    val iv = new Array[Byte](IV_SIZE)
    new SecureRandom().nextBytes(iv)
    iv
  }

  def encryptFile(inputFile: String, outputFile: String, key: String): Unit = {
    val keyBytes = key.getBytes("UTF-8")
    encryptFile(inputFile, outputFile, keyBytes)
  }

  /**
   * 加密文件
   * @param inputFile 输入文件路径
   * @param outputFile 输出文件路径
   * @param key 加密密钥
   * @param iv 初始化向量
   */
  def encryptFile(inputFile: String, outputFile: String, key: Array[Byte], iv: Array[Byte] = generateIV()): Unit = {
    val secretKey = new SecretKeySpec(key, ALGORITHM)
    val ivSpec = new IvParameterSpec(iv)

    val cipher = Cipher.getInstance(TRANSFORMATION, "BC")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)

    val fis = new FileInputStream(inputFile)
    val fos = new FileOutputStream(outputFile)

    // 首先写入IV到输出文件
    fos.write(iv)

    // 然后写入加密内容
    val cos = new CipherOutputStream(fos, cipher)

    val buffer = new Array[Byte](4096)
    var bytesRead = fis.read(buffer)

    while (bytesRead != -1) {
      cos.write(buffer, 0, bytesRead)
      bytesRead = fis.read(buffer)
    }

    cos.close()
    fis.close()
  }

  /**
   * 解密文件
   * @param inputFile 输入文件路径
   * @param outputFile 输出文件路径
   * @param key 解密密钥
   * @return 使用的IV
   */
  def decryptFile(inputFile: String, outputFile: String, key: Array[Byte]): Array[Byte] = {
    val fis = new FileInputStream(inputFile)

    // 从输入文件读取IV (前16字节)
    val iv = new Array[Byte](IV_SIZE)
    fis.read(iv)

    val secretKey = new SecretKeySpec(key, ALGORITHM)
    val ivSpec = new IvParameterSpec(iv)

    val cipher = Cipher.getInstance(TRANSFORMATION, "BC")
    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

    val cis = new CipherInputStream(fis, cipher)
    val fos = new FileOutputStream(outputFile)

    val buffer = new Array[Byte](4096)
    var bytesRead = cis.read(buffer)

    while (bytesRead != -1) {
      fos.write(buffer, 0, bytesRead)
      bytesRead = cis.read(buffer)
    }

    cis.close()
    fos.close()

    iv
  }
}