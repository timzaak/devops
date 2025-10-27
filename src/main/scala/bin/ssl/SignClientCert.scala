package bin.ssl


import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cert.jcajce.{JcaX509CertificateConverter, JcaX509v3CertificateBuilder}
import org.bouncycastle.openssl.jcajce.{JcaPEMWriter, JcaPEMKeyConverter}
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.bouncycastle.openssl.PEMParser

import java.io.*
import java.math.BigInteger
import java.security.*
import java.security.cert.*
import java.util.Date

///  sbt "runMain bin.ssl.SignClientCert $path"
object SignClientCert {
  def main(args: Array[String]): Unit = {
    Security.addProvider(new BouncyCastleProvider())
    val parentPath = args(0)
    val caPem = parentPath + "\\ca.pem"
    val caKey = parentPath + "\\ca.key"

    // 签发客户端证书
    val (certPem, keyPem) = issueCert(
      caCertPemPath = caPem,
      caKeyPemPath = caKey,
      startAt = new Date(System.currentTimeMillis()),
      endAt = new Date(System.currentTimeMillis() + 100L * 24 * 3600 * 1000)
    )
    println(s"$certPem$keyPem")
  }

  def issueCert(
                 caCertPemPath: String,
                 caKeyPemPath: String,
                 startAt: Date,
                 endAt: Date
               ): (String, String) = {

    // === 加载 CA 证书 ===
    val caCert = loadCertificateFromPem(caCertPemPath)
    val caKeyPair = loadPrivateKeyFromPem(caKeyPemPath)

    // === 生成客户端密钥对 ===
    val keyGen = KeyPairGenerator.getInstance("RSA", "BC")
    keyGen.initialize(2048)
    val clientKeyPair = keyGen.generateKeyPair()

    // === 构造证书颁发者和主题 ===
    val issuer = new X500Name(caCert.getSubjectX500Principal.getName)
    val subject = new X500Name(s"CN=AliencellDevice")

    // === 生成序列号 ===
    val serial = BigInteger.valueOf(System.currentTimeMillis())

    // === 构造证书 ===
    val certBuilder = new JcaX509v3CertificateBuilder(
      issuer,
      serial,
      startAt,
      endAt,
      subject,
      clientKeyPair.getPublic
    )


    val contentSigner = new JcaContentSignerBuilder("SHA256withECDSA")
      .setProvider("BC")
      .build(caKeyPair.getPrivate)

    val certHolder = certBuilder.build(contentSigner)
    val certificate = new JcaX509CertificateConverter()
      .setProvider("BC")
      .getCertificate(certHolder)

    // === PEM 输出 ===
    val certPem = toPem("CERTIFICATE", certificate.getEncoded)
    val keyPem = toPem("PRIVATE KEY", clientKeyPair.getPrivate.getEncoded)

    (certPem, keyPem)
  }

  /** 读取 PEM 格式证书 */
  private def loadCertificateFromPem(path: String): X509Certificate = {
    val parser = new PEMParser(new FileReader(path))
    val holder = parser.readObject().asInstanceOf[X509CertificateHolder]
    parser.close()
    if(holder.isInstanceOf[X509CertificateHolder]) {

    }
    new JcaX509CertificateConverter().setProvider("BC").getCertificate(holder)
  }

  /** 读取 PEM 格式私钥 */
  private def loadPrivateKeyFromPem(path: String): KeyPair = {
    val parser = new PEMParser(new FileReader(path))
    val obj = parser.readObject()
    parser.close()

    val converter = new JcaPEMKeyConverter().setProvider("BC")
    obj match {
      case kp: org.bouncycastle.openssl.PEMKeyPair => converter.getKeyPair(kp)
      case pk: org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter => null
      case pk: org.bouncycastle.asn1.pkcs.PrivateKeyInfo =>
        new KeyPair(null, converter.getPrivateKey(pk))
      case _ => throw new IllegalArgumentException("Unsupported key format")
    }
  }

  /** 将二进制数据转为 PEM 字符串 */
  private def toPem(label: String, data: Array[Byte]): String = {
    val sw = new StringWriter()
    val pw = new JcaPEMWriter(sw)
    pw.writeObject(new org.bouncycastle.util.io.pem.PemObject(label, data))
    pw.close()
    sw.toString
  }
}
