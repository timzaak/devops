import com.timzaak.devops.wikijs.WikiJSClient
import com.typesafe.config.ConfigFactory
import better.files.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import java.io.FileOutputStream
import java.net.URLEncoder
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration.*
import scala.concurrent.ExecutionContext.Implicits.global

// sbt "runMain WikiDocExporter https://wiki.example.com"
object WikiDocExporter {
  def main(args: Array[String]): Unit = {
    val urlPrefix = args(0)
    // val urlPrefix = "https://wiki.example.com"
    val conf = ConfigFactory.load()
    val client = new WikiJSClient(conf.getString("wiki.url"), conf.getString("wiki.auth"))
    val pages = Await.result(client.getPageList(), 10.seconds).filter(v => v.isPublished && !v.isPrivate)
    // val tmp = File("tmp")
    // tmp.delete()
    // tmp.createDirectory()
    val data = pages.map { page =>
      Await.result(client.getSinglePage(page.id).map(_.get), 10.seconds)
    }
    val p = File("_tmp/wiki")
    if (p.notExists) {
      p.createDirectories()
    }
    val d = data.collect {
      case page if page.content.trim.linesIterator.count(_.trim.nonEmpty) > 5 =>
        val link = s"$urlPrefix/${page.path.split('/').map(URLEncoder.encode(_, "utf-8")).mkString("/")}"
        val name = page.title
        val locale = page.locale
        (name, locale, link, page.content)
    }
    val workbook = getWorkbook(d)
    val file = (p / "wiki.xlsx")
    val out = file.newFileOutputStream()
    workbook.write(out)
    workbook.close()
    out.close()

  }

  private def convertToFile(
    title: String,
    path: String,
    contentType: String,
    content: String,
    locale: String,
    urlPrefix: String
  ) = {
    val ext = contentType match {
      case "markdown" => "md"
      case "html"     => "html"
      case _          => throw new IllegalArgumentException(s"unknown content type:$contentType")
    }
    val p = File("_tmp/wiki")
    if (p.notExists) {
      p.createDirectories()
    }
    println(s"file path: _tmp/wiki/${locale}_${path.replaceAll("/", "_")}.$ext")
    // val file = File(s"_tmp/wiki/${locale}_${path.replaceAll("/", "_")}.$ext")
    val uri = path.split('/').map(URLEncoder.encode(_, "utf-8")).mkString("/")
    //    file.writeText(s"""---
    //         |link: $urlPrefix/$uri
    //         |locale: $locale
    //         |---
    //         |
    //         |$content""".stripMargin)

  }

  def getWorkbook(data: List[(String, String, String, String)]) = {
    val workbook = new XSSFWorkbook()
    val sheet = workbook.createSheet("Sheet1")

    val row = sheet.createRow(0)
    row.createCell(0).setCellValue("Title")
    row.createCell(1).setCellValue("Locale")
    row.createCell(2).setCellValue("Link")
    row.createCell(3).setCellValue("Markdown Content")

    data.zipWithIndex.foreach { case ((title, locale, link, content), index) =>
      val row = sheet.createRow(index + 1)
      row.createCell(0).setCellValue(title)
      row.createCell(1).setCellValue(locale)
      row.createCell(2).setCellValue(link)
      row.createCell(3).setCellValue(content)
    }
    workbook
  }
}
