package com.timzaak.devops.webhook

import io.circe.*
import io.circe.generic.semiauto.*
import io.circe.syntax.*
import sttp.client4.*
import sttp.client4.circe.*

import scala.util.Try

// 企业微信消息类型
sealed trait WeChatWorkMessage

// 文本消息
case class TextMessage(content: String) extends WeChatWorkMessage

// Markdown消息
case class MarkdownMessage(content: String) extends WeChatWorkMessage

// 图片消息
case class ImageMessage(base64: String, md5: String) extends WeChatWorkMessage

// 新闻消息
case class NewsArticle(
  title: String,
  description: Option[String] = None,
  url: Option[String] = None,
  picurl: Option[String] = None
)

case class NewsMessage(articles: List[NewsArticle]) extends WeChatWorkMessage

// 企业微信API请求体
case class WeChatWorkRequest(
  msgtype: String,
  text: Option[Map[String, String]] = None,
  markdown: Option[Map[String, String]] = None,
  image: Option[Map[String, String]] = None,
  news: Option[Map[String, List[NewsArticle]]] = None
)

// JSON编码器
given Encoder[NewsArticle] = deriveEncoder[NewsArticle]
given Encoder[WeChatWorkRequest] = deriveEncoder[WeChatWorkRequest]

// 企业微信Webhook客户端
class WeChatWorkWebhook(webhookUrl: String)(using backend: SyncBackend) {

  def sendMessage(message: WeChatWorkMessage): Try[Unit] = {
    val request = buildRequest(message)

    val httpRequest = basicRequest
      .post(uri"$webhookUrl")
      .header("Content-Type", "application/json")
      .body(request.asJson.noSpaces)
      .response(asJson[Json])

    Try {
      val response = httpRequest.send(backend)

      response.body match {
        case Right(json) =>
          val errcode = json.hcursor.get[Int]("errcode").getOrElse(-1)
          if (errcode == 0) {
            println("消息发送成功")
          } else {
            val errmsg = json.hcursor.get[String]("errmsg").getOrElse("未知错误")
            throw new RuntimeException(s"发送失败: errcode=$errcode, errmsg=$errmsg")
          }
        case Left(error) =>
          throw new RuntimeException(s"请求失败: $error")
      }
    }
  }

  def sendText(content: String): Try[Unit] = {
    sendMessage(TextMessage(content))
  }

  def sendMarkdown(content: String): Try[Unit] = {
    sendMessage(MarkdownMessage(content))
  }

  def sendNews(articles: List[NewsArticle]): Try[Unit] = {
    sendMessage(NewsMessage(articles))
  }

  private def buildRequest(message: WeChatWorkMessage): WeChatWorkRequest = {
    message match {
      case TextMessage(content) =>
        WeChatWorkRequest(
          msgtype = "text",
          text = Some(Map("content" -> content))
        )

      case MarkdownMessage(content) =>
        WeChatWorkRequest(
          msgtype = "markdown",
          markdown = Some(Map("content" -> content))
        )

      case ImageMessage(base64, md5) =>
        WeChatWorkRequest(
          msgtype = "image",
          image = Some(Map("base64" -> base64, "md5" -> md5))
        )

      case NewsMessage(articles) =>
        WeChatWorkRequest(
          msgtype = "news",
          news = Some(Map("articles" -> articles))
        )
    }
  }
}
