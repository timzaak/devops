package com.timzaak.devops.wikijs

import sttp.client3.*
import WikiJSSchema.*
import caliban.client.SelectionBuilder

import scala.concurrent.{ ExecutionContext, Future }

class WikiJSClient(url: String, authorization: String)(using
  ex: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
) {

  private val backend = HttpClientFutureBackend()

  extension [A](query: SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]]) {
    def send(): Future[Option[A]] = query
      .toRequest(uri"$url")
      .header("Authorization", s"Bearer $authorization")
      .send(backend)
      .map(_.body.toOption.flatten)
  }

  def getPageList(locale: Option[String] = None) = {
    Query
      .pages(
        PageQuery.list(limit = Some(1000), locale = locale)(
          PageListItem.id ~ PageListItem.isPrivate ~ PageListItem.isPublished ~ PageListItem.locale ~ PageListItem.title ~ PageListItem.path
        )
      )
      .send()
      .map(
        _.getOrElse(List.empty).map(
          identity[
            (id: Int, isPrivate: Boolean, isPublished: Boolean, locale: String, title: Option[String], path: String)
          ]
        )
      )
  }
  def getSinglePage(id: Int) = {
    Query
      .pages(
        PageQuery.single(id)(
          Page.id ~ Page.path ~ Page.title ~ Page.content ~ Page.contentType ~ Page.render ~ Page.isPublished ~ Page.toc
            ~ Page.locale
        )
      )
      .send()
      .map(
        _.flatten.map(
          identity[
            (
              id: Int,
              path: String,
              title: String,
              content: String,
              contentType: String,
              render: Option[String],
              isPublished: Boolean,
              toc: Option[String],
              locale: String
            )
          ]
        )
      )
  }

  def getNavigation() = {
    Query
      .navigation(
        NavigationQuery.tree(
          NavigationTree.locale ~ NavigationTree.items(
            NavigationItem.id ~ NavigationItem.kind ~ NavigationItem.target ~ NavigationItem.targetType ~ NavigationItem.visibilityMode
          )
        )
      )
      .send()
      .map(_.getOrElse(List.empty))
  }

}
