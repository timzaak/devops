package com.timzaak.devops.wikijs

import caliban.client.*
import caliban.client.CalibanClientError.DecodingError
import caliban.client.FieldBuilder.*
import caliban.client.__Value.*

object WikiJSSchema {

  type Date = String

  type Upload = String

  sealed trait AssetKind extends scala.Product with scala.Serializable { def value: String }
  object AssetKind {
    case object IMAGE extends AssetKind { val value: String = "IMAGE" }
    case object BINARY extends AssetKind { val value: String = "BINARY" }
    case object ALL extends AssetKind { val value: String = "ALL" }

    implicit val decoder: ScalarDecoder[AssetKind] = {
      case __StringValue("IMAGE")  => Right(AssetKind.IMAGE)
      case __StringValue("BINARY") => Right(AssetKind.BINARY)
      case __StringValue("ALL")    => Right(AssetKind.ALL)
      case other                   => Left(DecodingError(s"Can't build AssetKind from input $other"))
    }
    implicit val encoder: ArgEncoder[AssetKind] = {
      case AssetKind.IMAGE  => __EnumValue("IMAGE")
      case AssetKind.BINARY => __EnumValue("BINARY")
      case AssetKind.ALL    => __EnumValue("ALL")
    }

    val values: scala.collection.immutable.Vector[AssetKind] = scala.collection.immutable.Vector(IMAGE, BINARY, ALL)
  }

  sealed trait PageRuleMatch extends scala.Product with scala.Serializable { def value: String }
  object PageRuleMatch {
    case object START extends PageRuleMatch { val value: String = "START" }
    case object EXACT extends PageRuleMatch { val value: String = "EXACT" }
    case object END extends PageRuleMatch { val value: String = "END" }
    case object REGEX extends PageRuleMatch { val value: String = "REGEX" }
    case object TAG extends PageRuleMatch { val value: String = "TAG" }

    implicit val decoder: ScalarDecoder[PageRuleMatch] = {
      case __StringValue("START") => Right(PageRuleMatch.START)
      case __StringValue("EXACT") => Right(PageRuleMatch.EXACT)
      case __StringValue("END")   => Right(PageRuleMatch.END)
      case __StringValue("REGEX") => Right(PageRuleMatch.REGEX)
      case __StringValue("TAG")   => Right(PageRuleMatch.TAG)
      case other                  => Left(DecodingError(s"Can't build PageRuleMatch from input $other"))
    }
    implicit val encoder: ArgEncoder[PageRuleMatch] = {
      case PageRuleMatch.START => __EnumValue("START")
      case PageRuleMatch.EXACT => __EnumValue("EXACT")
      case PageRuleMatch.END   => __EnumValue("END")
      case PageRuleMatch.REGEX => __EnumValue("REGEX")
      case PageRuleMatch.TAG   => __EnumValue("TAG")
    }

    val values: scala.collection.immutable.Vector[PageRuleMatch] =
      scala.collection.immutable.Vector(START, EXACT, END, REGEX, TAG)
  }

  sealed trait NavigationMode extends scala.Product with scala.Serializable { def value: String }
  object NavigationMode {
    case object NONE extends NavigationMode { val value: String = "NONE" }
    case object TREE extends NavigationMode { val value: String = "TREE" }
    case object MIXED extends NavigationMode { val value: String = "MIXED" }
    case object STATIC extends NavigationMode { val value: String = "STATIC" }

    implicit val decoder: ScalarDecoder[NavigationMode] = {
      case __StringValue("NONE")   => Right(NavigationMode.NONE)
      case __StringValue("TREE")   => Right(NavigationMode.TREE)
      case __StringValue("MIXED")  => Right(NavigationMode.MIXED)
      case __StringValue("STATIC") => Right(NavigationMode.STATIC)
      case other                   => Left(DecodingError(s"Can't build NavigationMode from input $other"))
    }
    implicit val encoder: ArgEncoder[NavigationMode] = {
      case NavigationMode.NONE   => __EnumValue("NONE")
      case NavigationMode.TREE   => __EnumValue("TREE")
      case NavigationMode.MIXED  => __EnumValue("MIXED")
      case NavigationMode.STATIC => __EnumValue("STATIC")
    }

    val values: scala.collection.immutable.Vector[NavigationMode] =
      scala.collection.immutable.Vector(NONE, TREE, MIXED, STATIC)
  }

  sealed trait PageOrderBy extends scala.Product with scala.Serializable { def value: String }
  object PageOrderBy {
    case object CREATED extends PageOrderBy { val value: String = "CREATED" }
    case object ID extends PageOrderBy { val value: String = "ID" }
    case object PATH extends PageOrderBy { val value: String = "PATH" }
    case object TITLE extends PageOrderBy { val value: String = "TITLE" }
    case object UPDATED extends PageOrderBy { val value: String = "UPDATED" }

    implicit val decoder: ScalarDecoder[PageOrderBy] = {
      case __StringValue("CREATED") => Right(PageOrderBy.CREATED)
      case __StringValue("ID")      => Right(PageOrderBy.ID)
      case __StringValue("PATH")    => Right(PageOrderBy.PATH)
      case __StringValue("TITLE")   => Right(PageOrderBy.TITLE)
      case __StringValue("UPDATED") => Right(PageOrderBy.UPDATED)
      case other                    => Left(DecodingError(s"Can't build PageOrderBy from input $other"))
    }
    implicit val encoder: ArgEncoder[PageOrderBy] = {
      case PageOrderBy.CREATED => __EnumValue("CREATED")
      case PageOrderBy.ID      => __EnumValue("ID")
      case PageOrderBy.PATH    => __EnumValue("PATH")
      case PageOrderBy.TITLE   => __EnumValue("TITLE")
      case PageOrderBy.UPDATED => __EnumValue("UPDATED")
    }

    val values: scala.collection.immutable.Vector[PageOrderBy] =
      scala.collection.immutable.Vector(CREATED, ID, PATH, TITLE, UPDATED)
  }

  sealed trait PageOrderByDirection extends scala.Product with scala.Serializable { def value: String }
  object PageOrderByDirection {
    case object ASC extends PageOrderByDirection { val value: String = "ASC" }
    case object DESC extends PageOrderByDirection { val value: String = "DESC" }

    implicit val decoder: ScalarDecoder[PageOrderByDirection] = {
      case __StringValue("ASC")  => Right(PageOrderByDirection.ASC)
      case __StringValue("DESC") => Right(PageOrderByDirection.DESC)
      case other                 => Left(DecodingError(s"Can't build PageOrderByDirection from input $other"))
    }
    implicit val encoder: ArgEncoder[PageOrderByDirection] = {
      case PageOrderByDirection.ASC  => __EnumValue("ASC")
      case PageOrderByDirection.DESC => __EnumValue("DESC")
    }

    val values: scala.collection.immutable.Vector[PageOrderByDirection] = scala.collection.immutable.Vector(ASC, DESC)
  }

  sealed trait PageTreeMode extends scala.Product with scala.Serializable { def value: String }
  object PageTreeMode {
    case object FOLDERS extends PageTreeMode { val value: String = "FOLDERS" }
    case object PAGES extends PageTreeMode { val value: String = "PAGES" }
    case object ALL extends PageTreeMode { val value: String = "ALL" }

    implicit val decoder: ScalarDecoder[PageTreeMode] = {
      case __StringValue("FOLDERS") => Right(PageTreeMode.FOLDERS)
      case __StringValue("PAGES")   => Right(PageTreeMode.PAGES)
      case __StringValue("ALL")     => Right(PageTreeMode.ALL)
      case other                    => Left(DecodingError(s"Can't build PageTreeMode from input $other"))
    }
    implicit val encoder: ArgEncoder[PageTreeMode] = {
      case PageTreeMode.FOLDERS => __EnumValue("FOLDERS")
      case PageTreeMode.PAGES   => __EnumValue("PAGES")
      case PageTreeMode.ALL     => __EnumValue("ALL")
    }

    val values: scala.collection.immutable.Vector[PageTreeMode] = scala.collection.immutable.Vector(FOLDERS, PAGES, ALL)
  }

  sealed trait SystemImportUsersGroupMode extends scala.Product with scala.Serializable { def value: String }
  object SystemImportUsersGroupMode {
    case object MULTI extends SystemImportUsersGroupMode { val value: String = "MULTI" }
    case object SINGLE extends SystemImportUsersGroupMode { val value: String = "SINGLE" }
    case object NONE extends SystemImportUsersGroupMode { val value: String = "NONE" }

    implicit val decoder: ScalarDecoder[SystemImportUsersGroupMode] = {
      case __StringValue("MULTI")  => Right(SystemImportUsersGroupMode.MULTI)
      case __StringValue("SINGLE") => Right(SystemImportUsersGroupMode.SINGLE)
      case __StringValue("NONE")   => Right(SystemImportUsersGroupMode.NONE)
      case other                   => Left(DecodingError(s"Can't build SystemImportUsersGroupMode from input $other"))
    }
    implicit val encoder: ArgEncoder[SystemImportUsersGroupMode] = {
      case SystemImportUsersGroupMode.MULTI  => __EnumValue("MULTI")
      case SystemImportUsersGroupMode.SINGLE => __EnumValue("SINGLE")
      case SystemImportUsersGroupMode.NONE   => __EnumValue("NONE")
    }

    val values: scala.collection.immutable.Vector[SystemImportUsersGroupMode] =
      scala.collection.immutable.Vector(MULTI, SINGLE, NONE)
  }

  sealed trait CacheControlScope extends scala.Product with scala.Serializable { def value: String }
  object CacheControlScope {
    case object PUBLIC extends CacheControlScope { val value: String = "PUBLIC" }
    case object PRIVATE extends CacheControlScope { val value: String = "PRIVATE" }

    implicit val decoder: ScalarDecoder[CacheControlScope] = {
      case __StringValue("PUBLIC")  => Right(CacheControlScope.PUBLIC)
      case __StringValue("PRIVATE") => Right(CacheControlScope.PRIVATE)
      case other                    => Left(DecodingError(s"Can't build CacheControlScope from input $other"))
    }
    implicit val encoder: ArgEncoder[CacheControlScope] = {
      case CacheControlScope.PUBLIC  => __EnumValue("PUBLIC")
      case CacheControlScope.PRIVATE => __EnumValue("PRIVATE")
    }

    val values: scala.collection.immutable.Vector[CacheControlScope] =
      scala.collection.immutable.Vector(PUBLIC, PRIVATE)
  }

  type AnalyticsQuery
  object AnalyticsQuery {
    def providers[A](isEnabled: scala.Option[Boolean] = None)(innerSelection: SelectionBuilder[AnalyticsProvider, A])(
      implicit encoder0: ArgEncoder[scala.Option[Boolean]]
    ): SelectionBuilder[AnalyticsQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field(
        "providers",
        OptionOf(ListOf(OptionOf(Obj(innerSelection)))),
        arguments = List(Argument("isEnabled", isEnabled, "Boolean")(encoder0))
      )
  }

  type AnalyticsMutation
  object AnalyticsMutation {
    def updateProviders[A](
      providers: List[scala.Option[AnalyticsProviderInput]] = Nil
    )(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[List[scala.Option[AnalyticsProviderInput]]]
    ): SelectionBuilder[AnalyticsMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "updateProviders",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("providers", providers, "[AnalyticsProviderInput]!")(encoder0))
    )
  }

  type AnalyticsProvider
  object AnalyticsProvider {
    def isEnabled: SelectionBuilder[AnalyticsProvider, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isEnabled", Scalar())
    def key: SelectionBuilder[AnalyticsProvider, String] = _root_.caliban.client.SelectionBuilder.Field("key", Scalar())
    def props: SelectionBuilder[AnalyticsProvider, scala.Option[List[scala.Option[String]]]] =
      _root_.caliban.client.SelectionBuilder.Field("props", OptionOf(ListOf(OptionOf(Scalar()))))
    def title: SelectionBuilder[AnalyticsProvider, String] =
      _root_.caliban.client.SelectionBuilder.Field("title", Scalar())
    def description: SelectionBuilder[AnalyticsProvider, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("description", OptionOf(Scalar()))
    def isAvailable: SelectionBuilder[AnalyticsProvider, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("isAvailable", OptionOf(Scalar()))
    def logo: SelectionBuilder[AnalyticsProvider, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("logo", OptionOf(Scalar()))
    def website: SelectionBuilder[AnalyticsProvider, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("website", OptionOf(Scalar()))
    def config[A](
      innerSelection: SelectionBuilder[KeyValuePair, A]
    ): SelectionBuilder[AnalyticsProvider, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("config", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
  }

  type AssetQuery
  object AssetQuery {
    def list[A](folderId: Int, kind: AssetKind)(innerSelection: SelectionBuilder[AssetItem, A])(implicit
      encoder0: ArgEncoder[Int],
      encoder1: ArgEncoder[AssetKind]
    ): SelectionBuilder[AssetQuery, scala.Option[List[scala.Option[A]]]] = _root_.caliban.client.SelectionBuilder.Field(
      "list",
      OptionOf(ListOf(OptionOf(Obj(innerSelection)))),
      arguments = List(Argument("folderId", folderId, "Int!")(encoder0), Argument("kind", kind, "AssetKind!")(encoder1))
    )
    def folders[A](parentFolderId: Int)(innerSelection: SelectionBuilder[AssetFolder, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[AssetQuery, scala.Option[List[scala.Option[A]]]] = _root_.caliban.client.SelectionBuilder.Field(
      "folders",
      OptionOf(ListOf(OptionOf(Obj(innerSelection)))),
      arguments = List(Argument("parentFolderId", parentFolderId, "Int!")(encoder0))
    )
  }

  type AssetMutation
  object AssetMutation {
    def createFolder[A](parentFolderId: Int, slug: String, name: scala.Option[String] = None)(
      innerSelection: SelectionBuilder[DefaultResponse, A]
    )(implicit
      encoder0: ArgEncoder[Int],
      encoder1: ArgEncoder[String],
      encoder2: ArgEncoder[scala.Option[String]]
    ): SelectionBuilder[AssetMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "createFolder",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("parentFolderId", parentFolderId, "Int!")(encoder0),
        Argument("slug", slug, "String!")(encoder1),
        Argument("name", name, "String")(encoder2)
      )
    )
    def renameAsset[A](id: Int, filename: String)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int],
      encoder1: ArgEncoder[String]
    ): SelectionBuilder[AssetMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "renameAsset",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("id", id, "Int!")(encoder0), Argument("filename", filename, "String!")(encoder1))
    )
    def deleteAsset[A](id: Int)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[AssetMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("deleteAsset", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
    def flushTempUploads[A](
      innerSelection: SelectionBuilder[DefaultResponse, A]
    ): SelectionBuilder[AssetMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("flushTempUploads", OptionOf(Obj(innerSelection)))
  }

  type AssetItem
  object AssetItem {
    def id: SelectionBuilder[AssetItem, Int] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def filename: SelectionBuilder[AssetItem, String] =
      _root_.caliban.client.SelectionBuilder.Field("filename", Scalar())
    def ext: SelectionBuilder[AssetItem, String] = _root_.caliban.client.SelectionBuilder.Field("ext", Scalar())
    def kind: SelectionBuilder[AssetItem, AssetKind] = _root_.caliban.client.SelectionBuilder.Field("kind", Scalar())
    def mime: SelectionBuilder[AssetItem, String] = _root_.caliban.client.SelectionBuilder.Field("mime", Scalar())
    def fileSize: SelectionBuilder[AssetItem, Int] = _root_.caliban.client.SelectionBuilder.Field("fileSize", Scalar())
    def metadata: SelectionBuilder[AssetItem, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("metadata", OptionOf(Scalar()))
    def createdAt: SelectionBuilder[AssetItem, Date] =
      _root_.caliban.client.SelectionBuilder.Field("createdAt", Scalar())
    def updatedAt: SelectionBuilder[AssetItem, Date] =
      _root_.caliban.client.SelectionBuilder.Field("updatedAt", Scalar())
    def folder[A](innerSelection: SelectionBuilder[AssetFolder, A]): SelectionBuilder[AssetItem, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("folder", OptionOf(Obj(innerSelection)))
    def author[A](innerSelection: SelectionBuilder[User, A]): SelectionBuilder[AssetItem, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("author", OptionOf(Obj(innerSelection)))
  }

  type AssetFolder
  object AssetFolder {
    def id: SelectionBuilder[AssetFolder, Int] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def slug: SelectionBuilder[AssetFolder, String] = _root_.caliban.client.SelectionBuilder.Field("slug", Scalar())
    def name: SelectionBuilder[AssetFolder, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("name", OptionOf(Scalar()))
  }

  type AuthenticationQuery
  object AuthenticationQuery {
    def apiKeys[A](
      innerSelection: SelectionBuilder[AuthenticationApiKey, A]
    ): SelectionBuilder[AuthenticationQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("apiKeys", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
    def apiState: SelectionBuilder[AuthenticationQuery, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("apiState", Scalar())
    def strategies[A](
      innerSelection: SelectionBuilder[AuthenticationStrategy, A]
    ): SelectionBuilder[AuthenticationQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("strategies", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
    def activeStrategies[A](
      enabledOnly: scala.Option[Boolean] = None
    )(innerSelection: SelectionBuilder[AuthenticationActiveStrategy, A])(implicit
      encoder0: ArgEncoder[scala.Option[Boolean]]
    ): SelectionBuilder[AuthenticationQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field(
        "activeStrategies",
        OptionOf(ListOf(OptionOf(Obj(innerSelection)))),
        arguments = List(Argument("enabledOnly", enabledOnly, "Boolean")(encoder0))
      )
  }

  type AuthenticationMutation
  object AuthenticationMutation {
    def createApiKey[A](name: String, expiration: String, fullAccess: Boolean, group: scala.Option[Int] = None)(
      innerSelection: SelectionBuilder[AuthenticationCreateApiKeyResponse, A]
    )(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[String],
      encoder2: ArgEncoder[Boolean],
      encoder3: ArgEncoder[scala.Option[Int]]
    ): SelectionBuilder[AuthenticationMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "createApiKey",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("name", name, "String!")(encoder0),
        Argument("expiration", expiration, "String!")(encoder1),
        Argument("fullAccess", fullAccess, "Boolean!")(encoder2),
        Argument("group", group, "Int")(encoder3)
      )
    )
    def login[A](username: String, password: String, strategy: String)(
      innerSelection: SelectionBuilder[AuthenticationLoginResponse, A]
    )(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[String],
      encoder2: ArgEncoder[String]
    ): SelectionBuilder[AuthenticationMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "login",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("username", username, "String!")(encoder0),
        Argument("password", password, "String!")(encoder1),
        Argument("strategy", strategy, "String!")(encoder2)
      )
    )
    def loginTFA[A](continuationToken: String, securityCode: String, setup: scala.Option[Boolean] = None)(
      innerSelection: SelectionBuilder[AuthenticationLoginResponse, A]
    )(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[String],
      encoder2: ArgEncoder[scala.Option[Boolean]]
    ): SelectionBuilder[AuthenticationMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "loginTFA",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("continuationToken", continuationToken, "String!")(encoder0),
        Argument("securityCode", securityCode, "String!")(encoder1),
        Argument("setup", setup, "Boolean")(encoder2)
      )
    )
    def loginChangePassword[A](continuationToken: String, newPassword: String)(
      innerSelection: SelectionBuilder[AuthenticationLoginResponse, A]
    )(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[String]
    ): SelectionBuilder[AuthenticationMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "loginChangePassword",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("continuationToken", continuationToken, "String!")(encoder0),
        Argument("newPassword", newPassword, "String!")(encoder1)
      )
    )
    def forgotPassword[A](email: String)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[String]
    ): SelectionBuilder[AuthenticationMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "forgotPassword",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("email", email, "String!")(encoder0))
    )
    def register[A](email: String, password: String, name: String)(
      innerSelection: SelectionBuilder[AuthenticationRegisterResponse, A]
    )(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[String],
      encoder2: ArgEncoder[String]
    ): SelectionBuilder[AuthenticationMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "register",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("email", email, "String!")(encoder0),
        Argument("password", password, "String!")(encoder1),
        Argument("name", name, "String!")(encoder2)
      )
    )
    def revokeApiKey[A](id: Int)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[AuthenticationMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("revokeApiKey", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
    def setApiState[A](enabled: Boolean)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Boolean]
    ): SelectionBuilder[AuthenticationMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "setApiState",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("enabled", enabled, "Boolean!")(encoder0))
    )
    def updateStrategies[A](
      strategies: List[scala.Option[AuthenticationStrategyInput]] = Nil
    )(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[List[scala.Option[AuthenticationStrategyInput]]]
    ): SelectionBuilder[AuthenticationMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "updateStrategies",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("strategies", strategies, "[AuthenticationStrategyInput]!")(encoder0))
    )
    def regenerateCertificates[A](
      innerSelection: SelectionBuilder[DefaultResponse, A]
    ): SelectionBuilder[AuthenticationMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("regenerateCertificates", OptionOf(Obj(innerSelection)))
    def resetGuestUser[A](
      innerSelection: SelectionBuilder[DefaultResponse, A]
    ): SelectionBuilder[AuthenticationMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("resetGuestUser", OptionOf(Obj(innerSelection)))
  }

  type AuthenticationStrategy
  object AuthenticationStrategy {
    def key: SelectionBuilder[AuthenticationStrategy, String] =
      _root_.caliban.client.SelectionBuilder.Field("key", Scalar())
    def props[A](
      innerSelection: SelectionBuilder[KeyValuePair, A]
    ): SelectionBuilder[AuthenticationStrategy, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("props", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
    def title: SelectionBuilder[AuthenticationStrategy, String] =
      _root_.caliban.client.SelectionBuilder.Field("title", Scalar())
    def description: SelectionBuilder[AuthenticationStrategy, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("description", OptionOf(Scalar()))
    def isAvailable: SelectionBuilder[AuthenticationStrategy, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("isAvailable", OptionOf(Scalar()))
    def useForm: SelectionBuilder[AuthenticationStrategy, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("useForm", Scalar())
    def usernameType: SelectionBuilder[AuthenticationStrategy, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("usernameType", OptionOf(Scalar()))
    def logo: SelectionBuilder[AuthenticationStrategy, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("logo", OptionOf(Scalar()))
    def color: SelectionBuilder[AuthenticationStrategy, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("color", OptionOf(Scalar()))
    def website: SelectionBuilder[AuthenticationStrategy, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("website", OptionOf(Scalar()))
    def icon: SelectionBuilder[AuthenticationStrategy, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("icon", OptionOf(Scalar()))
  }

  type AuthenticationActiveStrategy
  object AuthenticationActiveStrategy {
    def key: SelectionBuilder[AuthenticationActiveStrategy, String] =
      _root_.caliban.client.SelectionBuilder.Field("key", Scalar())
    def strategy[A](
      innerSelection: SelectionBuilder[AuthenticationStrategy, A]
    ): SelectionBuilder[AuthenticationActiveStrategy, A] =
      _root_.caliban.client.SelectionBuilder.Field("strategy", Obj(innerSelection))
    def displayName: SelectionBuilder[AuthenticationActiveStrategy, String] =
      _root_.caliban.client.SelectionBuilder.Field("displayName", Scalar())
    def order: SelectionBuilder[AuthenticationActiveStrategy, Int] =
      _root_.caliban.client.SelectionBuilder.Field("order", Scalar())
    def isEnabled: SelectionBuilder[AuthenticationActiveStrategy, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isEnabled", Scalar())
    def config[A](
      innerSelection: SelectionBuilder[KeyValuePair, A]
    ): SelectionBuilder[AuthenticationActiveStrategy, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("config", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
    def selfRegistration: SelectionBuilder[AuthenticationActiveStrategy, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("selfRegistration", Scalar())
    def domainWhitelist: SelectionBuilder[AuthenticationActiveStrategy, List[scala.Option[String]]] =
      _root_.caliban.client.SelectionBuilder.Field("domainWhitelist", ListOf(OptionOf(Scalar())))
    def autoEnrollGroups: SelectionBuilder[AuthenticationActiveStrategy, List[scala.Option[Int]]] =
      _root_.caliban.client.SelectionBuilder.Field("autoEnrollGroups", ListOf(OptionOf(Scalar())))
  }

  type AuthenticationLoginResponse
  object AuthenticationLoginResponse {
    def responseResult[A](
      innerSelection: SelectionBuilder[ResponseStatus, A]
    ): SelectionBuilder[AuthenticationLoginResponse, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("responseResult", OptionOf(Obj(innerSelection)))
    def jwt: SelectionBuilder[AuthenticationLoginResponse, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("jwt", OptionOf(Scalar()))
    def mustChangePwd: SelectionBuilder[AuthenticationLoginResponse, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("mustChangePwd", OptionOf(Scalar()))
    def mustProvideTFA: SelectionBuilder[AuthenticationLoginResponse, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("mustProvideTFA", OptionOf(Scalar()))
    def mustSetupTFA: SelectionBuilder[AuthenticationLoginResponse, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("mustSetupTFA", OptionOf(Scalar()))
    def continuationToken: SelectionBuilder[AuthenticationLoginResponse, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("continuationToken", OptionOf(Scalar()))
    def redirect: SelectionBuilder[AuthenticationLoginResponse, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("redirect", OptionOf(Scalar()))
    def tfaQRImage: SelectionBuilder[AuthenticationLoginResponse, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("tfaQRImage", OptionOf(Scalar()))
  }

  type AuthenticationRegisterResponse
  object AuthenticationRegisterResponse {
    def responseResult[A](
      innerSelection: SelectionBuilder[ResponseStatus, A]
    ): SelectionBuilder[AuthenticationRegisterResponse, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("responseResult", OptionOf(Obj(innerSelection)))
    def jwt: SelectionBuilder[AuthenticationRegisterResponse, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("jwt", OptionOf(Scalar()))
  }

  type AuthenticationApiKey
  object AuthenticationApiKey {
    def id: SelectionBuilder[AuthenticationApiKey, Int] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def name: SelectionBuilder[AuthenticationApiKey, String] =
      _root_.caliban.client.SelectionBuilder.Field("name", Scalar())
    def keyShort: SelectionBuilder[AuthenticationApiKey, String] =
      _root_.caliban.client.SelectionBuilder.Field("keyShort", Scalar())
    def expiration: SelectionBuilder[AuthenticationApiKey, Date] =
      _root_.caliban.client.SelectionBuilder.Field("expiration", Scalar())
    def createdAt: SelectionBuilder[AuthenticationApiKey, Date] =
      _root_.caliban.client.SelectionBuilder.Field("createdAt", Scalar())
    def updatedAt: SelectionBuilder[AuthenticationApiKey, Date] =
      _root_.caliban.client.SelectionBuilder.Field("updatedAt", Scalar())
    def isRevoked: SelectionBuilder[AuthenticationApiKey, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isRevoked", Scalar())
  }

  type AuthenticationCreateApiKeyResponse
  object AuthenticationCreateApiKeyResponse {
    def responseResult[A](
      innerSelection: SelectionBuilder[ResponseStatus, A]
    ): SelectionBuilder[AuthenticationCreateApiKeyResponse, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("responseResult", OptionOf(Obj(innerSelection)))
    def key: SelectionBuilder[AuthenticationCreateApiKeyResponse, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("key", OptionOf(Scalar()))
  }

  type CommentQuery
  object CommentQuery {
    def providers[A](
      innerSelection: SelectionBuilder[CommentProvider, A]
    ): SelectionBuilder[CommentQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("providers", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
    def list[A](locale: String, path: String)(innerSelection: SelectionBuilder[CommentPost, A])(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[String]
    ): SelectionBuilder[CommentQuery, List[scala.Option[A]]] = _root_.caliban.client.SelectionBuilder.Field(
      "list",
      ListOf(OptionOf(Obj(innerSelection))),
      arguments = List(Argument("locale", locale, "String!")(encoder0), Argument("path", path, "String!")(encoder1))
    )
    def single[A](id: Int)(innerSelection: SelectionBuilder[CommentPost, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[CommentQuery, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("single", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
  }

  type CommentMutation
  object CommentMutation {
    def updateProviders[A](
      providers: scala.Option[List[scala.Option[CommentProviderInput]]] = None
    )(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[scala.Option[List[scala.Option[CommentProviderInput]]]]
    ): SelectionBuilder[CommentMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "updateProviders",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("providers", providers, "[CommentProviderInput]")(encoder0))
    )
    def create[A](
      pageId: Int,
      replyTo: scala.Option[Int] = None,
      content: String,
      guestName: scala.Option[String] = None,
      guestEmail: scala.Option[String] = None
    )(innerSelection: SelectionBuilder[CommentCreateResponse, A])(implicit
      encoder0: ArgEncoder[Int],
      encoder1: ArgEncoder[scala.Option[Int]],
      encoder2: ArgEncoder[String],
      encoder3: ArgEncoder[scala.Option[String]],
      encoder4: ArgEncoder[scala.Option[String]]
    ): SelectionBuilder[CommentMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "create",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("pageId", pageId, "Int!")(encoder0),
        Argument("replyTo", replyTo, "Int")(encoder1),
        Argument("content", content, "String!")(encoder2),
        Argument("guestName", guestName, "String")(encoder3),
        Argument("guestEmail", guestEmail, "String")(encoder4)
      )
    )
    def update[A](id: Int, content: String)(innerSelection: SelectionBuilder[CommentUpdateResponse, A])(implicit
      encoder0: ArgEncoder[Int],
      encoder1: ArgEncoder[String]
    ): SelectionBuilder[CommentMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "update",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("id", id, "Int!")(encoder0), Argument("content", content, "String!")(encoder1))
    )
    def delete[A](id: Int)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[CommentMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("delete", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
  }

  type CommentProvider
  object CommentProvider {
    def isEnabled: SelectionBuilder[CommentProvider, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isEnabled", Scalar())
    def key: SelectionBuilder[CommentProvider, String] = _root_.caliban.client.SelectionBuilder.Field("key", Scalar())
    def title: SelectionBuilder[CommentProvider, String] =
      _root_.caliban.client.SelectionBuilder.Field("title", Scalar())
    def description: SelectionBuilder[CommentProvider, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("description", OptionOf(Scalar()))
    def logo: SelectionBuilder[CommentProvider, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("logo", OptionOf(Scalar()))
    def website: SelectionBuilder[CommentProvider, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("website", OptionOf(Scalar()))
    def isAvailable: SelectionBuilder[CommentProvider, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("isAvailable", OptionOf(Scalar()))
    def config[A](
      innerSelection: SelectionBuilder[KeyValuePair, A]
    ): SelectionBuilder[CommentProvider, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("config", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
  }

  type CommentPost
  object CommentPost {
    def id: SelectionBuilder[CommentPost, Int] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def content: SelectionBuilder[CommentPost, String] =
      _root_.caliban.client.SelectionBuilder.Field("content", Scalar())
    def render: SelectionBuilder[CommentPost, String] = _root_.caliban.client.SelectionBuilder.Field("render", Scalar())
    def authorId: SelectionBuilder[CommentPost, Int] =
      _root_.caliban.client.SelectionBuilder.Field("authorId", Scalar())
    def authorName: SelectionBuilder[CommentPost, String] =
      _root_.caliban.client.SelectionBuilder.Field("authorName", Scalar())
    def authorEmail: SelectionBuilder[CommentPost, String] =
      _root_.caliban.client.SelectionBuilder.Field("authorEmail", Scalar())
    def authorIP: SelectionBuilder[CommentPost, String] =
      _root_.caliban.client.SelectionBuilder.Field("authorIP", Scalar())
    def createdAt: SelectionBuilder[CommentPost, Date] =
      _root_.caliban.client.SelectionBuilder.Field("createdAt", Scalar())
    def updatedAt: SelectionBuilder[CommentPost, Date] =
      _root_.caliban.client.SelectionBuilder.Field("updatedAt", Scalar())
  }

  type CommentCreateResponse
  object CommentCreateResponse {
    def responseResult[A](
      innerSelection: SelectionBuilder[ResponseStatus, A]
    ): SelectionBuilder[CommentCreateResponse, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("responseResult", OptionOf(Obj(innerSelection)))
    def id: SelectionBuilder[CommentCreateResponse, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("id", OptionOf(Scalar()))
  }

  type CommentUpdateResponse
  object CommentUpdateResponse {
    def responseResult[A](
      innerSelection: SelectionBuilder[ResponseStatus, A]
    ): SelectionBuilder[CommentUpdateResponse, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("responseResult", OptionOf(Obj(innerSelection)))
    def render: SelectionBuilder[CommentUpdateResponse, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("render", OptionOf(Scalar()))
  }

  type KeyValuePair
  object KeyValuePair {
    def key: SelectionBuilder[KeyValuePair, String] = _root_.caliban.client.SelectionBuilder.Field("key", Scalar())
    def value: SelectionBuilder[KeyValuePair, String] = _root_.caliban.client.SelectionBuilder.Field("value", Scalar())
  }

  type DefaultResponse
  object DefaultResponse {
    def responseResult[A](
      innerSelection: SelectionBuilder[ResponseStatus, A]
    ): SelectionBuilder[DefaultResponse, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("responseResult", OptionOf(Obj(innerSelection)))
  }

  type ResponseStatus
  object ResponseStatus {
    def succeeded: SelectionBuilder[ResponseStatus, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("succeeded", Scalar())
    def errorCode: SelectionBuilder[ResponseStatus, Int] =
      _root_.caliban.client.SelectionBuilder.Field("errorCode", Scalar())
    def slug: SelectionBuilder[ResponseStatus, String] = _root_.caliban.client.SelectionBuilder.Field("slug", Scalar())
    def message: SelectionBuilder[ResponseStatus, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("message", OptionOf(Scalar()))
  }

  type ContributeQuery
  object ContributeQuery {
    def contributors[A](
      innerSelection: SelectionBuilder[ContributeContributor, A]
    ): SelectionBuilder[ContributeQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("contributors", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
  }

  type ContributeContributor
  object ContributeContributor {
    def id: SelectionBuilder[ContributeContributor, String] =
      _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def source: SelectionBuilder[ContributeContributor, String] =
      _root_.caliban.client.SelectionBuilder.Field("source", Scalar())
    def name: SelectionBuilder[ContributeContributor, String] =
      _root_.caliban.client.SelectionBuilder.Field("name", Scalar())
    def joined: SelectionBuilder[ContributeContributor, Date] =
      _root_.caliban.client.SelectionBuilder.Field("joined", Scalar())
    def website: SelectionBuilder[ContributeContributor, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("website", OptionOf(Scalar()))
    def twitter: SelectionBuilder[ContributeContributor, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("twitter", OptionOf(Scalar()))
    def avatar: SelectionBuilder[ContributeContributor, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("avatar", OptionOf(Scalar()))
  }

  type GroupQuery
  object GroupQuery {
    def list[A](filter: scala.Option[String] = None, orderBy: scala.Option[String] = None)(
      innerSelection: SelectionBuilder[GroupMinimal, A]
    )(implicit
      encoder0: ArgEncoder[scala.Option[String]],
      encoder1: ArgEncoder[scala.Option[String]]
    ): SelectionBuilder[GroupQuery, scala.Option[List[scala.Option[A]]]] = _root_.caliban.client.SelectionBuilder.Field(
      "list",
      OptionOf(ListOf(OptionOf(Obj(innerSelection)))),
      arguments = List(Argument("filter", filter, "String")(encoder0), Argument("orderBy", orderBy, "String")(encoder1))
    )
    def single[A](id: Int)(innerSelection: SelectionBuilder[Group, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[GroupQuery, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("single", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
  }

  type GroupMutation
  object GroupMutation {
    def create[A](name: String)(innerSelection: SelectionBuilder[GroupResponse, A])(implicit
      encoder0: ArgEncoder[String]
    ): SelectionBuilder[GroupMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("create", OptionOf(Obj(innerSelection)), arguments = List(Argument("name", name, "String!")(encoder0)))
    def update[A](
      id: Int,
      name: String,
      redirectOnLogin: String,
      permissions: List[scala.Option[String]] = Nil,
      pageRules: List[scala.Option[PageRuleInput]] = Nil
    )(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int],
      encoder1: ArgEncoder[String],
      encoder2: ArgEncoder[String],
      encoder3: ArgEncoder[List[scala.Option[String]]],
      encoder4: ArgEncoder[List[scala.Option[PageRuleInput]]]
    ): SelectionBuilder[GroupMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "update",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("id", id, "Int!")(encoder0),
        Argument("name", name, "String!")(encoder1),
        Argument("redirectOnLogin", redirectOnLogin, "String!")(encoder2),
        Argument("permissions", permissions, "[String]!")(encoder3),
        Argument("pageRules", pageRules, "[PageRuleInput]!")(encoder4)
      )
    )
    def delete[A](id: Int)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[GroupMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("delete", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
    def assignUser[A](groupId: Int, userId: Int)(
      innerSelection: SelectionBuilder[DefaultResponse, A]
    )(implicit encoder0: ArgEncoder[Int], encoder1: ArgEncoder[Int]): SelectionBuilder[GroupMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field(
        "assignUser",
        OptionOf(Obj(innerSelection)),
        arguments = List(Argument("groupId", groupId, "Int!")(encoder0), Argument("userId", userId, "Int!")(encoder1))
      )
    def unassignUser[A](groupId: Int, userId: Int)(
      innerSelection: SelectionBuilder[DefaultResponse, A]
    )(implicit encoder0: ArgEncoder[Int], encoder1: ArgEncoder[Int]): SelectionBuilder[GroupMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field(
        "unassignUser",
        OptionOf(Obj(innerSelection)),
        arguments = List(Argument("groupId", groupId, "Int!")(encoder0), Argument("userId", userId, "Int!")(encoder1))
      )
  }

  type GroupResponse
  object GroupResponse {
    def responseResult[A](innerSelection: SelectionBuilder[ResponseStatus, A]): SelectionBuilder[GroupResponse, A] =
      _root_.caliban.client.SelectionBuilder.Field("responseResult", Obj(innerSelection))
    def group[A](innerSelection: SelectionBuilder[Group, A]): SelectionBuilder[GroupResponse, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("group", OptionOf(Obj(innerSelection)))
  }

  type GroupMinimal
  object GroupMinimal {
    def id: SelectionBuilder[GroupMinimal, Int] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def name: SelectionBuilder[GroupMinimal, String] = _root_.caliban.client.SelectionBuilder.Field("name", Scalar())
    def isSystem: SelectionBuilder[GroupMinimal, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isSystem", Scalar())
    def userCount: SelectionBuilder[GroupMinimal, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("userCount", OptionOf(Scalar()))
    def createdAt: SelectionBuilder[GroupMinimal, Date] =
      _root_.caliban.client.SelectionBuilder.Field("createdAt", Scalar())
    def updatedAt: SelectionBuilder[GroupMinimal, Date] =
      _root_.caliban.client.SelectionBuilder.Field("updatedAt", Scalar())
  }

  type Group
  object Group {
    def id: SelectionBuilder[Group, Int] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def name: SelectionBuilder[Group, String] = _root_.caliban.client.SelectionBuilder.Field("name", Scalar())
    def isSystem: SelectionBuilder[Group, Boolean] = _root_.caliban.client.SelectionBuilder.Field("isSystem", Scalar())
    def redirectOnLogin: SelectionBuilder[Group, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("redirectOnLogin", OptionOf(Scalar()))
    def permissions: SelectionBuilder[Group, List[scala.Option[String]]] =
      _root_.caliban.client.SelectionBuilder.Field("permissions", ListOf(OptionOf(Scalar())))
    def pageRules[A](
      innerSelection: SelectionBuilder[PageRule, A]
    ): SelectionBuilder[Group, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("pageRules", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
    def users[A](
      innerSelection: SelectionBuilder[UserMinimal, A]
    ): SelectionBuilder[Group, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("users", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
    def createdAt: SelectionBuilder[Group, Date] = _root_.caliban.client.SelectionBuilder.Field("createdAt", Scalar())
    def updatedAt: SelectionBuilder[Group, Date] = _root_.caliban.client.SelectionBuilder.Field("updatedAt", Scalar())
  }

  type PageRule
  object PageRule {
    def id: SelectionBuilder[PageRule, String] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def deny: SelectionBuilder[PageRule, Boolean] = _root_.caliban.client.SelectionBuilder.Field("deny", Scalar())
    def `match`: SelectionBuilder[PageRule, PageRuleMatch] =
      _root_.caliban.client.SelectionBuilder.Field("match", Scalar())
    def roles: SelectionBuilder[PageRule, List[scala.Option[String]]] =
      _root_.caliban.client.SelectionBuilder.Field("roles", ListOf(OptionOf(Scalar())))
    def path: SelectionBuilder[PageRule, String] = _root_.caliban.client.SelectionBuilder.Field("path", Scalar())
    def locales: SelectionBuilder[PageRule, List[scala.Option[String]]] =
      _root_.caliban.client.SelectionBuilder.Field("locales", ListOf(OptionOf(Scalar())))
  }

  type LocalizationQuery
  object LocalizationQuery {
    def locales[A](
      innerSelection: SelectionBuilder[LocalizationLocale, A]
    ): SelectionBuilder[LocalizationQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("locales", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
    def config[A](
      innerSelection: SelectionBuilder[LocalizationConfig, A]
    ): SelectionBuilder[LocalizationQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("config", OptionOf(Obj(innerSelection)))
    def translations[A](locale: String, namespace: String)(innerSelection: SelectionBuilder[Translation, A])(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[String]
    ): SelectionBuilder[LocalizationQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field(
        "translations",
        OptionOf(ListOf(OptionOf(Obj(innerSelection)))),
        arguments =
          List(Argument("locale", locale, "String!")(encoder0), Argument("namespace", namespace, "String!")(encoder1))
      )
  }

  type LocalizationMutation
  object LocalizationMutation {
    def downloadLocale[A](locale: String)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[String]
    ): SelectionBuilder[LocalizationMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "downloadLocale",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("locale", locale, "String!")(encoder0))
    )
    def updateLocale[A](
      locale: String,
      autoUpdate: Boolean,
      namespacing: Boolean,
      namespaces: List[scala.Option[String]] = Nil
    )(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[Boolean],
      encoder2: ArgEncoder[Boolean],
      encoder3: ArgEncoder[List[scala.Option[String]]]
    ): SelectionBuilder[LocalizationMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "updateLocale",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("locale", locale, "String!")(encoder0),
        Argument("autoUpdate", autoUpdate, "Boolean!")(encoder1),
        Argument("namespacing", namespacing, "Boolean!")(encoder2),
        Argument("namespaces", namespaces, "[String]!")(encoder3)
      )
    )
  }

  type LocalizationLocale
  object LocalizationLocale {
    def availability: SelectionBuilder[LocalizationLocale, Int] =
      _root_.caliban.client.SelectionBuilder.Field("availability", Scalar())
    def code: SelectionBuilder[LocalizationLocale, String] =
      _root_.caliban.client.SelectionBuilder.Field("code", Scalar())
    def createdAt: SelectionBuilder[LocalizationLocale, Date] =
      _root_.caliban.client.SelectionBuilder.Field("createdAt", Scalar())
    def installDate: SelectionBuilder[LocalizationLocale, scala.Option[Date]] =
      _root_.caliban.client.SelectionBuilder.Field("installDate", OptionOf(Scalar()))
    def isInstalled: SelectionBuilder[LocalizationLocale, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isInstalled", Scalar())
    def isRTL: SelectionBuilder[LocalizationLocale, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isRTL", Scalar())
    def name: SelectionBuilder[LocalizationLocale, String] =
      _root_.caliban.client.SelectionBuilder.Field("name", Scalar())
    def nativeName: SelectionBuilder[LocalizationLocale, String] =
      _root_.caliban.client.SelectionBuilder.Field("nativeName", Scalar())
    def updatedAt: SelectionBuilder[LocalizationLocale, Date] =
      _root_.caliban.client.SelectionBuilder.Field("updatedAt", Scalar())
  }

  type LocalizationConfig
  object LocalizationConfig {
    def locale: SelectionBuilder[LocalizationConfig, String] =
      _root_.caliban.client.SelectionBuilder.Field("locale", Scalar())
    def autoUpdate: SelectionBuilder[LocalizationConfig, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("autoUpdate", Scalar())
    def namespacing: SelectionBuilder[LocalizationConfig, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("namespacing", Scalar())
    def namespaces: SelectionBuilder[LocalizationConfig, List[scala.Option[String]]] =
      _root_.caliban.client.SelectionBuilder.Field("namespaces", ListOf(OptionOf(Scalar())))
  }

  type Translation
  object Translation {
    def key: SelectionBuilder[Translation, String] = _root_.caliban.client.SelectionBuilder.Field("key", Scalar())
    def value: SelectionBuilder[Translation, String] = _root_.caliban.client.SelectionBuilder.Field("value", Scalar())
  }

  type LoggingQuery
  object LoggingQuery {
    def loggers[A](filter: scala.Option[String] = None, orderBy: scala.Option[String] = None)(
      innerSelection: SelectionBuilder[Logger, A]
    )(implicit
      encoder0: ArgEncoder[scala.Option[String]],
      encoder1: ArgEncoder[scala.Option[String]]
    ): SelectionBuilder[LoggingQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field(
        "loggers",
        OptionOf(ListOf(OptionOf(Obj(innerSelection)))),
        arguments =
          List(Argument("filter", filter, "String")(encoder0), Argument("orderBy", orderBy, "String")(encoder1))
      )
  }

  type LoggingMutation
  object LoggingMutation {
    def updateLoggers[A](
      loggers: scala.Option[List[scala.Option[LoggerInput]]] = None
    )(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[scala.Option[List[scala.Option[LoggerInput]]]]
    ): SelectionBuilder[LoggingMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "updateLoggers",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("loggers", loggers, "[LoggerInput]")(encoder0))
    )
  }

  type Logger
  object Logger {
    def isEnabled: SelectionBuilder[Logger, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isEnabled", Scalar())
    def key: SelectionBuilder[Logger, String] = _root_.caliban.client.SelectionBuilder.Field("key", Scalar())
    def title: SelectionBuilder[Logger, String] = _root_.caliban.client.SelectionBuilder.Field("title", Scalar())
    def description: SelectionBuilder[Logger, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("description", OptionOf(Scalar()))
    def logo: SelectionBuilder[Logger, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("logo", OptionOf(Scalar()))
    def website: SelectionBuilder[Logger, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("website", OptionOf(Scalar()))
    def level: SelectionBuilder[Logger, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("level", OptionOf(Scalar()))
    def config[A](
      innerSelection: SelectionBuilder[KeyValuePair, A]
    ): SelectionBuilder[Logger, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("config", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
  }

  type LoggerTrailLine
  object LoggerTrailLine {
    def level: SelectionBuilder[LoggerTrailLine, String] =
      _root_.caliban.client.SelectionBuilder.Field("level", Scalar())
    def output: SelectionBuilder[LoggerTrailLine, String] =
      _root_.caliban.client.SelectionBuilder.Field("output", Scalar())
    def timestamp: SelectionBuilder[LoggerTrailLine, Date] =
      _root_.caliban.client.SelectionBuilder.Field("timestamp", Scalar())
  }

  type MailQuery
  object MailQuery {
    def config[A](innerSelection: SelectionBuilder[MailConfig, A]): SelectionBuilder[MailQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("config", OptionOf(Obj(innerSelection)))
  }

  type MailMutation
  object MailMutation {
    def sendTest[A](recipientEmail: String)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[String]
    ): SelectionBuilder[MailMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "sendTest",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("recipientEmail", recipientEmail, "String!")(encoder0))
    )
    def updateConfig[A](
      senderName: String,
      senderEmail: String,
      host: String,
      port: Int,
      name: String,
      secure: Boolean,
      verifySSL: Boolean,
      user: String,
      pass: String,
      useDKIM: Boolean,
      dkimDomainName: String,
      dkimKeySelector: String,
      dkimPrivateKey: String
    )(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[String],
      encoder2: ArgEncoder[String],
      encoder3: ArgEncoder[Int],
      encoder4: ArgEncoder[String],
      encoder5: ArgEncoder[Boolean],
      encoder6: ArgEncoder[Boolean],
      encoder7: ArgEncoder[String],
      encoder8: ArgEncoder[String],
      encoder9: ArgEncoder[Boolean],
      encoder10: ArgEncoder[String],
      encoder11: ArgEncoder[String],
      encoder12: ArgEncoder[String]
    ): SelectionBuilder[MailMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "updateConfig",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("senderName", senderName, "String!")(encoder0),
        Argument("senderEmail", senderEmail, "String!")(encoder1),
        Argument("host", host, "String!")(encoder2),
        Argument("port", port, "Int!")(encoder3),
        Argument("name", name, "String!")(encoder4),
        Argument("secure", secure, "Boolean!")(encoder5),
        Argument("verifySSL", verifySSL, "Boolean!")(encoder6),
        Argument("user", user, "String!")(encoder7),
        Argument("pass", pass, "String!")(encoder8),
        Argument("useDKIM", useDKIM, "Boolean!")(encoder9),
        Argument("dkimDomainName", dkimDomainName, "String!")(encoder10),
        Argument("dkimKeySelector", dkimKeySelector, "String!")(encoder11),
        Argument("dkimPrivateKey", dkimPrivateKey, "String!")(encoder12)
      )
    )
  }

  type MailConfig
  object MailConfig {
    def senderName: SelectionBuilder[MailConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("senderName", OptionOf(Scalar()))
    def senderEmail: SelectionBuilder[MailConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("senderEmail", OptionOf(Scalar()))
    def host: SelectionBuilder[MailConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("host", OptionOf(Scalar()))
    def port: SelectionBuilder[MailConfig, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("port", OptionOf(Scalar()))
    def name: SelectionBuilder[MailConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("name", OptionOf(Scalar()))
    def secure: SelectionBuilder[MailConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("secure", OptionOf(Scalar()))
    def verifySSL: SelectionBuilder[MailConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("verifySSL", OptionOf(Scalar()))
    def user: SelectionBuilder[MailConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("user", OptionOf(Scalar()))
    def pass: SelectionBuilder[MailConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("pass", OptionOf(Scalar()))
    def useDKIM: SelectionBuilder[MailConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("useDKIM", OptionOf(Scalar()))
    def dkimDomainName: SelectionBuilder[MailConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("dkimDomainName", OptionOf(Scalar()))
    def dkimKeySelector: SelectionBuilder[MailConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("dkimKeySelector", OptionOf(Scalar()))
    def dkimPrivateKey: SelectionBuilder[MailConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("dkimPrivateKey", OptionOf(Scalar()))
  }

  type NavigationQuery
  object NavigationQuery {
    def tree[A](
      innerSelection: SelectionBuilder[NavigationTree, A]
    ): SelectionBuilder[NavigationQuery, List[scala.Option[A]]] =
      _root_.caliban.client.SelectionBuilder.Field("tree", ListOf(OptionOf(Obj(innerSelection))))
    def config[A](innerSelection: SelectionBuilder[NavigationConfig, A]): SelectionBuilder[NavigationQuery, A] =
      _root_.caliban.client.SelectionBuilder.Field("config", Obj(innerSelection))
  }

  type NavigationMutation
  object NavigationMutation {
    def updateTree[A](
      tree: List[scala.Option[NavigationTreeInput]] = Nil
    )(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[List[scala.Option[NavigationTreeInput]]]
    ): SelectionBuilder[NavigationMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "updateTree",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("tree", tree, "[NavigationTreeInput]!")(encoder0))
    )
    def updateConfig[A](mode: NavigationMode)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[NavigationMode]
    ): SelectionBuilder[NavigationMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "updateConfig",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("mode", mode, "NavigationMode!")(encoder0))
    )
  }

  type NavigationTree
  object NavigationTree {
    def locale: SelectionBuilder[NavigationTree, String] =
      _root_.caliban.client.SelectionBuilder.Field("locale", Scalar())
    def items[A](
      innerSelection: SelectionBuilder[NavigationItem, A]
    ): SelectionBuilder[NavigationTree, List[scala.Option[A]]] =
      _root_.caliban.client.SelectionBuilder.Field("items", ListOf(OptionOf(Obj(innerSelection))))
  }

  type NavigationItem
  object NavigationItem {
    def id: SelectionBuilder[NavigationItem, String] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def kind: SelectionBuilder[NavigationItem, String] = _root_.caliban.client.SelectionBuilder.Field("kind", Scalar())
    def label: SelectionBuilder[NavigationItem, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("label", OptionOf(Scalar()))
    def icon: SelectionBuilder[NavigationItem, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("icon", OptionOf(Scalar()))
    def targetType: SelectionBuilder[NavigationItem, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("targetType", OptionOf(Scalar()))
    def target: SelectionBuilder[NavigationItem, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("target", OptionOf(Scalar()))
    def visibilityMode: SelectionBuilder[NavigationItem, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("visibilityMode", OptionOf(Scalar()))
    def visibilityGroups: SelectionBuilder[NavigationItem, scala.Option[List[scala.Option[Int]]]] =
      _root_.caliban.client.SelectionBuilder.Field("visibilityGroups", OptionOf(ListOf(OptionOf(Scalar()))))
  }

  type NavigationConfig
  object NavigationConfig {
    def mode: SelectionBuilder[NavigationConfig, NavigationMode] =
      _root_.caliban.client.SelectionBuilder.Field("mode", Scalar())
  }

  type PageQuery
  object PageQuery {
    def history[A](id: Int, offsetPage: scala.Option[Int] = None, offsetSize: scala.Option[Int] = None)(
      innerSelection: SelectionBuilder[PageHistoryResult, A]
    )(implicit
      encoder0: ArgEncoder[Int],
      encoder1: ArgEncoder[scala.Option[Int]],
      encoder2: ArgEncoder[scala.Option[Int]]
    ): SelectionBuilder[PageQuery, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "history",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("id", id, "Int!")(encoder0),
        Argument("offsetPage", offsetPage, "Int")(encoder1),
        Argument("offsetSize", offsetSize, "Int")(encoder2)
      )
    )
    def version[A](pageId: Int, versionId: Int)(innerSelection: SelectionBuilder[PageVersion, A])(implicit
      encoder0: ArgEncoder[Int],
      encoder1: ArgEncoder[Int]
    ): SelectionBuilder[PageQuery, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "version",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("pageId", pageId, "Int!")(encoder0), Argument("versionId", versionId, "Int!")(encoder1))
    )
    def search[A](query: String, path: scala.Option[String] = None, locale: scala.Option[String] = None)(
      innerSelection: SelectionBuilder[PageSearchResponse, A]
    )(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[scala.Option[String]],
      encoder2: ArgEncoder[scala.Option[String]]
    ): SelectionBuilder[PageQuery, A] = _root_.caliban.client.SelectionBuilder.Field(
      "search",
      Obj(innerSelection),
      arguments = List(
        Argument("query", query, "String!")(encoder0),
        Argument("path", path, "String")(encoder1),
        Argument("locale", locale, "String")(encoder2)
      )
    )
    def list[A](
      limit: scala.Option[Int] = None,
      orderBy: scala.Option[PageOrderBy] = None,
      orderByDirection: scala.Option[PageOrderByDirection] = None,
      tags: scala.Option[List[String]] = None,
      locale: scala.Option[String] = None,
      creatorId: scala.Option[Int] = None,
      authorId: scala.Option[Int] = None
    )(innerSelection: SelectionBuilder[PageListItem, A])(implicit
      encoder0: ArgEncoder[scala.Option[Int]],
      encoder1: ArgEncoder[scala.Option[PageOrderBy]],
      encoder2: ArgEncoder[scala.Option[PageOrderByDirection]],
      encoder3: ArgEncoder[scala.Option[List[String]]],
      encoder4: ArgEncoder[scala.Option[String]],
      encoder5: ArgEncoder[scala.Option[Int]],
      encoder6: ArgEncoder[scala.Option[Int]]
    ): SelectionBuilder[PageQuery, List[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "list",
      ListOf(Obj(innerSelection)),
      arguments = List(
        Argument("limit", limit, "Int")(encoder0),
        Argument("orderBy", orderBy, "PageOrderBy")(encoder1),
        Argument("orderByDirection", orderByDirection, "PageOrderByDirection")(encoder2),
        Argument("tags", tags, "[String!]")(encoder3),
        Argument("locale", locale, "String")(encoder4),
        Argument("creatorId", creatorId, "Int")(encoder5),
        Argument("authorId", authorId, "Int")(encoder6)
      )
    )
    def single[A](id: Int)(innerSelection: SelectionBuilder[Page, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[PageQuery, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("single", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
    def singleByPath[A](path: String, locale: String)(innerSelection: SelectionBuilder[Page, A])(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[String]
    ): SelectionBuilder[PageQuery, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "singleByPath",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("path", path, "String!")(encoder0), Argument("locale", locale, "String!")(encoder1))
    )
    def tags[A](innerSelection: SelectionBuilder[PageTag, A]): SelectionBuilder[PageQuery, List[scala.Option[A]]] =
      _root_.caliban.client.SelectionBuilder.Field("tags", ListOf(OptionOf(Obj(innerSelection))))
    def searchTags(query: String)(implicit
      encoder0: ArgEncoder[String]
    ): SelectionBuilder[PageQuery, List[scala.Option[String]]] = _root_.caliban.client.SelectionBuilder
      .Field("searchTags", ListOf(OptionOf(Scalar())), arguments = List(Argument("query", query, "String!")(encoder0)))
    def tree[A](
      path: scala.Option[String] = None,
      parent: scala.Option[Int] = None,
      mode: PageTreeMode,
      locale: String,
      includeAncestors: scala.Option[Boolean] = None
    )(innerSelection: SelectionBuilder[PageTreeItem, A])(implicit
      encoder0: ArgEncoder[scala.Option[String]],
      encoder1: ArgEncoder[scala.Option[Int]],
      encoder2: ArgEncoder[PageTreeMode],
      encoder3: ArgEncoder[String],
      encoder4: ArgEncoder[scala.Option[Boolean]]
    ): SelectionBuilder[PageQuery, scala.Option[List[scala.Option[A]]]] = _root_.caliban.client.SelectionBuilder.Field(
      "tree",
      OptionOf(ListOf(OptionOf(Obj(innerSelection)))),
      arguments = List(
        Argument("path", path, "String")(encoder0),
        Argument("parent", parent, "Int")(encoder1),
        Argument("mode", mode, "PageTreeMode!")(encoder2),
        Argument("locale", locale, "String!")(encoder3),
        Argument("includeAncestors", includeAncestors, "Boolean")(encoder4)
      )
    )
    def links[A](locale: String)(innerSelection: SelectionBuilder[PageLinkItem, A])(implicit
      encoder0: ArgEncoder[String]
    ): SelectionBuilder[PageQuery, scala.Option[List[scala.Option[A]]]] = _root_.caliban.client.SelectionBuilder.Field(
      "links",
      OptionOf(ListOf(OptionOf(Obj(innerSelection)))),
      arguments = List(Argument("locale", locale, "String!")(encoder0))
    )
    def checkConflicts(id: Int, checkoutDate: Date)(implicit
      encoder0: ArgEncoder[Int],
      encoder1: ArgEncoder[Date]
    ): SelectionBuilder[PageQuery, Boolean] = _root_.caliban.client.SelectionBuilder.Field(
      "checkConflicts",
      Scalar(),
      arguments = List(Argument("id", id, "Int!")(encoder0), Argument("checkoutDate", checkoutDate, "Date!")(encoder1))
    )
    def conflictLatest[A](id: Int)(
      innerSelection: SelectionBuilder[PageConflictLatest, A]
    )(implicit encoder0: ArgEncoder[Int]): SelectionBuilder[PageQuery, A] = _root_.caliban.client.SelectionBuilder
      .Field("conflictLatest", Obj(innerSelection), arguments = List(Argument("id", id, "Int!")(encoder0)))
  }

  type PageMutation
  object PageMutation {
    def create[A](
      content: String,
      description: String,
      editor: String,
      isPublished: Boolean,
      isPrivate: Boolean,
      locale: String,
      path: String,
      publishEndDate: scala.Option[Date] = None,
      publishStartDate: scala.Option[Date] = None,
      scriptCss: scala.Option[String] = None,
      scriptJs: scala.Option[String] = None,
      tags: List[scala.Option[String]] = Nil,
      title: String
    )(innerSelection: SelectionBuilder[PageResponse, A])(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[String],
      encoder2: ArgEncoder[String],
      encoder3: ArgEncoder[Boolean],
      encoder4: ArgEncoder[Boolean],
      encoder5: ArgEncoder[String],
      encoder6: ArgEncoder[String],
      encoder7: ArgEncoder[scala.Option[Date]],
      encoder8: ArgEncoder[scala.Option[Date]],
      encoder9: ArgEncoder[scala.Option[String]],
      encoder10: ArgEncoder[scala.Option[String]],
      encoder11: ArgEncoder[List[scala.Option[String]]],
      encoder12: ArgEncoder[String]
    ): SelectionBuilder[PageMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "create",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("content", content, "String!")(encoder0),
        Argument("description", description, "String!")(encoder1),
        Argument("editor", editor, "String!")(encoder2),
        Argument("isPublished", isPublished, "Boolean!")(encoder3),
        Argument("isPrivate", isPrivate, "Boolean!")(encoder4),
        Argument("locale", locale, "String!")(encoder5),
        Argument("path", path, "String!")(encoder6),
        Argument("publishEndDate", publishEndDate, "Date")(encoder7),
        Argument("publishStartDate", publishStartDate, "Date")(encoder8),
        Argument("scriptCss", scriptCss, "String")(encoder9),
        Argument("scriptJs", scriptJs, "String")(encoder10),
        Argument("tags", tags, "[String]!")(encoder11),
        Argument("title", title, "String!")(encoder12)
      )
    )
    def update[A](
      id: Int,
      content: scala.Option[String] = None,
      description: scala.Option[String] = None,
      editor: scala.Option[String] = None,
      isPrivate: scala.Option[Boolean] = None,
      isPublished: scala.Option[Boolean] = None,
      locale: scala.Option[String] = None,
      path: scala.Option[String] = None,
      publishEndDate: scala.Option[Date] = None,
      publishStartDate: scala.Option[Date] = None,
      scriptCss: scala.Option[String] = None,
      scriptJs: scala.Option[String] = None,
      tags: scala.Option[List[scala.Option[String]]] = None,
      title: scala.Option[String] = None
    )(innerSelection: SelectionBuilder[PageResponse, A])(implicit
      encoder0: ArgEncoder[Int],
      encoder1: ArgEncoder[scala.Option[String]],
      encoder2: ArgEncoder[scala.Option[String]],
      encoder3: ArgEncoder[scala.Option[String]],
      encoder4: ArgEncoder[scala.Option[Boolean]],
      encoder5: ArgEncoder[scala.Option[Boolean]],
      encoder6: ArgEncoder[scala.Option[String]],
      encoder7: ArgEncoder[scala.Option[String]],
      encoder8: ArgEncoder[scala.Option[Date]],
      encoder9: ArgEncoder[scala.Option[Date]],
      encoder10: ArgEncoder[scala.Option[String]],
      encoder11: ArgEncoder[scala.Option[String]],
      encoder12: ArgEncoder[scala.Option[List[scala.Option[String]]]],
      encoder13: ArgEncoder[scala.Option[String]]
    ): SelectionBuilder[PageMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "update",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("id", id, "Int!")(encoder0),
        Argument("content", content, "String")(encoder1),
        Argument("description", description, "String")(encoder2),
        Argument("editor", editor, "String")(encoder3),
        Argument("isPrivate", isPrivate, "Boolean")(encoder4),
        Argument("isPublished", isPublished, "Boolean")(encoder5),
        Argument("locale", locale, "String")(encoder6),
        Argument("path", path, "String")(encoder7),
        Argument("publishEndDate", publishEndDate, "Date")(encoder8),
        Argument("publishStartDate", publishStartDate, "Date")(encoder9),
        Argument("scriptCss", scriptCss, "String")(encoder10),
        Argument("scriptJs", scriptJs, "String")(encoder11),
        Argument("tags", tags, "[String]")(encoder12),
        Argument("title", title, "String")(encoder13)
      )
    )
    def convert[A](id: Int, editor: String)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int],
      encoder1: ArgEncoder[String]
    ): SelectionBuilder[PageMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "convert",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("id", id, "Int!")(encoder0), Argument("editor", editor, "String!")(encoder1))
    )
    def move[A](id: Int, destinationPath: String, destinationLocale: String)(
      innerSelection: SelectionBuilder[DefaultResponse, A]
    )(implicit
      encoder0: ArgEncoder[Int],
      encoder1: ArgEncoder[String],
      encoder2: ArgEncoder[String]
    ): SelectionBuilder[PageMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "move",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("id", id, "Int!")(encoder0),
        Argument("destinationPath", destinationPath, "String!")(encoder1),
        Argument("destinationLocale", destinationLocale, "String!")(encoder2)
      )
    )
    def delete[A](id: Int)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[PageMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("delete", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
    def deleteTag[A](id: Int)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[PageMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("deleteTag", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
    def updateTag[A](id: Int, tag: String, title: String)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int],
      encoder1: ArgEncoder[String],
      encoder2: ArgEncoder[String]
    ): SelectionBuilder[PageMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "updateTag",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("id", id, "Int!")(encoder0),
        Argument("tag", tag, "String!")(encoder1),
        Argument("title", title, "String!")(encoder2)
      )
    )
    def flushCache[A](
      innerSelection: SelectionBuilder[DefaultResponse, A]
    ): SelectionBuilder[PageMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("flushCache", OptionOf(Obj(innerSelection)))
    def migrateToLocale[A](sourceLocale: String, targetLocale: String)(
      innerSelection: SelectionBuilder[PageMigrationResponse, A]
    )(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[String]
    ): SelectionBuilder[PageMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "migrateToLocale",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("sourceLocale", sourceLocale, "String!")(encoder0),
        Argument("targetLocale", targetLocale, "String!")(encoder1)
      )
    )
    def rebuildTree[A](
      innerSelection: SelectionBuilder[DefaultResponse, A]
    ): SelectionBuilder[PageMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("rebuildTree", OptionOf(Obj(innerSelection)))
    def render[A](id: Int)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[PageMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("render", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
    def restore[A](pageId: Int, versionId: Int)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int],
      encoder1: ArgEncoder[Int]
    ): SelectionBuilder[PageMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "restore",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("pageId", pageId, "Int!")(encoder0), Argument("versionId", versionId, "Int!")(encoder1))
    )
    def purgeHistory[A](olderThan: String)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[String]
    ): SelectionBuilder[PageMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "purgeHistory",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("olderThan", olderThan, "String!")(encoder0))
    )
  }

  type PageResponse
  object PageResponse {
    def responseResult[A](innerSelection: SelectionBuilder[ResponseStatus, A]): SelectionBuilder[PageResponse, A] =
      _root_.caliban.client.SelectionBuilder.Field("responseResult", Obj(innerSelection))
    def page[A](innerSelection: SelectionBuilder[Page, A]): SelectionBuilder[PageResponse, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("page", OptionOf(Obj(innerSelection)))
  }

  type PageMigrationResponse
  object PageMigrationResponse {
    def responseResult[A](
      innerSelection: SelectionBuilder[ResponseStatus, A]
    ): SelectionBuilder[PageMigrationResponse, A] =
      _root_.caliban.client.SelectionBuilder.Field("responseResult", Obj(innerSelection))
    def count: SelectionBuilder[PageMigrationResponse, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("count", OptionOf(Scalar()))
  }

  type Page
  object Page {
    def id: SelectionBuilder[Page, Int] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def path: SelectionBuilder[Page, String] = _root_.caliban.client.SelectionBuilder.Field("path", Scalar())
    def hash: SelectionBuilder[Page, String] = _root_.caliban.client.SelectionBuilder.Field("hash", Scalar())
    def title: SelectionBuilder[Page, String] = _root_.caliban.client.SelectionBuilder.Field("title", Scalar())
    def description: SelectionBuilder[Page, String] =
      _root_.caliban.client.SelectionBuilder.Field("description", Scalar())
    def isPrivate: SelectionBuilder[Page, Boolean] = _root_.caliban.client.SelectionBuilder.Field("isPrivate", Scalar())
    def isPublished: SelectionBuilder[Page, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isPublished", Scalar())
    def privateNS: SelectionBuilder[Page, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("privateNS", OptionOf(Scalar()))
    def publishStartDate: SelectionBuilder[Page, Date] =
      _root_.caliban.client.SelectionBuilder.Field("publishStartDate", Scalar())
    def publishEndDate: SelectionBuilder[Page, Date] =
      _root_.caliban.client.SelectionBuilder.Field("publishEndDate", Scalar())
    def tags[A](innerSelection: SelectionBuilder[PageTag, A]): SelectionBuilder[Page, List[scala.Option[A]]] =
      _root_.caliban.client.SelectionBuilder.Field("tags", ListOf(OptionOf(Obj(innerSelection))))
    def content: SelectionBuilder[Page, String] = _root_.caliban.client.SelectionBuilder.Field("content", Scalar())
    def render: SelectionBuilder[Page, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("render", OptionOf(Scalar()))
    def toc: SelectionBuilder[Page, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("toc", OptionOf(Scalar()))
    def contentType: SelectionBuilder[Page, String] =
      _root_.caliban.client.SelectionBuilder.Field("contentType", Scalar())
    def createdAt: SelectionBuilder[Page, Date] = _root_.caliban.client.SelectionBuilder.Field("createdAt", Scalar())
    def updatedAt: SelectionBuilder[Page, Date] = _root_.caliban.client.SelectionBuilder.Field("updatedAt", Scalar())
    def editor: SelectionBuilder[Page, String] = _root_.caliban.client.SelectionBuilder.Field("editor", Scalar())
    def locale: SelectionBuilder[Page, String] = _root_.caliban.client.SelectionBuilder.Field("locale", Scalar())
    def scriptCss: SelectionBuilder[Page, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("scriptCss", OptionOf(Scalar()))
    def scriptJs: SelectionBuilder[Page, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("scriptJs", OptionOf(Scalar()))
    def authorId: SelectionBuilder[Page, Int] = _root_.caliban.client.SelectionBuilder.Field("authorId", Scalar())
    def authorName: SelectionBuilder[Page, String] =
      _root_.caliban.client.SelectionBuilder.Field("authorName", Scalar())
    def authorEmail: SelectionBuilder[Page, String] =
      _root_.caliban.client.SelectionBuilder.Field("authorEmail", Scalar())
    def creatorId: SelectionBuilder[Page, Int] = _root_.caliban.client.SelectionBuilder.Field("creatorId", Scalar())
    def creatorName: SelectionBuilder[Page, String] =
      _root_.caliban.client.SelectionBuilder.Field("creatorName", Scalar())
    def creatorEmail: SelectionBuilder[Page, String] =
      _root_.caliban.client.SelectionBuilder.Field("creatorEmail", Scalar())
  }

  type PageTag
  object PageTag {
    def id: SelectionBuilder[PageTag, Int] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def tag: SelectionBuilder[PageTag, String] = _root_.caliban.client.SelectionBuilder.Field("tag", Scalar())
    def title: SelectionBuilder[PageTag, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("title", OptionOf(Scalar()))
    def createdAt: SelectionBuilder[PageTag, Date] = _root_.caliban.client.SelectionBuilder.Field("createdAt", Scalar())
    def updatedAt: SelectionBuilder[PageTag, Date] = _root_.caliban.client.SelectionBuilder.Field("updatedAt", Scalar())
  }

  type PageHistory
  object PageHistory {
    def versionId: SelectionBuilder[PageHistory, Int] =
      _root_.caliban.client.SelectionBuilder.Field("versionId", Scalar())
    def versionDate: SelectionBuilder[PageHistory, Date] =
      _root_.caliban.client.SelectionBuilder.Field("versionDate", Scalar())
    def authorId: SelectionBuilder[PageHistory, Int] =
      _root_.caliban.client.SelectionBuilder.Field("authorId", Scalar())
    def authorName: SelectionBuilder[PageHistory, String] =
      _root_.caliban.client.SelectionBuilder.Field("authorName", Scalar())
    def actionType: SelectionBuilder[PageHistory, String] =
      _root_.caliban.client.SelectionBuilder.Field("actionType", Scalar())
    def valueBefore: SelectionBuilder[PageHistory, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("valueBefore", OptionOf(Scalar()))
    def valueAfter: SelectionBuilder[PageHistory, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("valueAfter", OptionOf(Scalar()))
  }

  type PageVersion
  object PageVersion {
    def action: SelectionBuilder[PageVersion, String] = _root_.caliban.client.SelectionBuilder.Field("action", Scalar())
    def authorId: SelectionBuilder[PageVersion, String] =
      _root_.caliban.client.SelectionBuilder.Field("authorId", Scalar())
    def authorName: SelectionBuilder[PageVersion, String] =
      _root_.caliban.client.SelectionBuilder.Field("authorName", Scalar())
    def content: SelectionBuilder[PageVersion, String] =
      _root_.caliban.client.SelectionBuilder.Field("content", Scalar())
    def contentType: SelectionBuilder[PageVersion, String] =
      _root_.caliban.client.SelectionBuilder.Field("contentType", Scalar())
    def createdAt: SelectionBuilder[PageVersion, Date] =
      _root_.caliban.client.SelectionBuilder.Field("createdAt", Scalar())
    def versionDate: SelectionBuilder[PageVersion, Date] =
      _root_.caliban.client.SelectionBuilder.Field("versionDate", Scalar())
    def description: SelectionBuilder[PageVersion, String] =
      _root_.caliban.client.SelectionBuilder.Field("description", Scalar())
    def editor: SelectionBuilder[PageVersion, String] = _root_.caliban.client.SelectionBuilder.Field("editor", Scalar())
    def isPrivate: SelectionBuilder[PageVersion, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isPrivate", Scalar())
    def isPublished: SelectionBuilder[PageVersion, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isPublished", Scalar())
    def locale: SelectionBuilder[PageVersion, String] = _root_.caliban.client.SelectionBuilder.Field("locale", Scalar())
    def pageId: SelectionBuilder[PageVersion, Int] = _root_.caliban.client.SelectionBuilder.Field("pageId", Scalar())
    def path: SelectionBuilder[PageVersion, String] = _root_.caliban.client.SelectionBuilder.Field("path", Scalar())
    def publishEndDate: SelectionBuilder[PageVersion, Date] =
      _root_.caliban.client.SelectionBuilder.Field("publishEndDate", Scalar())
    def publishStartDate: SelectionBuilder[PageVersion, Date] =
      _root_.caliban.client.SelectionBuilder.Field("publishStartDate", Scalar())
    def tags: SelectionBuilder[PageVersion, List[scala.Option[String]]] =
      _root_.caliban.client.SelectionBuilder.Field("tags", ListOf(OptionOf(Scalar())))
    def title: SelectionBuilder[PageVersion, String] = _root_.caliban.client.SelectionBuilder.Field("title", Scalar())
    def versionId: SelectionBuilder[PageVersion, Int] =
      _root_.caliban.client.SelectionBuilder.Field("versionId", Scalar())
  }

  type PageHistoryResult
  object PageHistoryResult {
    def trail[A](
      innerSelection: SelectionBuilder[PageHistory, A]
    ): SelectionBuilder[PageHistoryResult, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("trail", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
    def total: SelectionBuilder[PageHistoryResult, Int] =
      _root_.caliban.client.SelectionBuilder.Field("total", Scalar())
  }

  type PageSearchResponse
  object PageSearchResponse {
    def results[A](
      innerSelection: SelectionBuilder[PageSearchResult, A]
    ): SelectionBuilder[PageSearchResponse, List[scala.Option[A]]] =
      _root_.caliban.client.SelectionBuilder.Field("results", ListOf(OptionOf(Obj(innerSelection))))
    def suggestions: SelectionBuilder[PageSearchResponse, List[scala.Option[String]]] =
      _root_.caliban.client.SelectionBuilder.Field("suggestions", ListOf(OptionOf(Scalar())))
    def totalHits: SelectionBuilder[PageSearchResponse, Int] =
      _root_.caliban.client.SelectionBuilder.Field("totalHits", Scalar())
  }

  type PageSearchResult
  object PageSearchResult {
    def id: SelectionBuilder[PageSearchResult, String] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def title: SelectionBuilder[PageSearchResult, String] =
      _root_.caliban.client.SelectionBuilder.Field("title", Scalar())
    def description: SelectionBuilder[PageSearchResult, String] =
      _root_.caliban.client.SelectionBuilder.Field("description", Scalar())
    def path: SelectionBuilder[PageSearchResult, String] =
      _root_.caliban.client.SelectionBuilder.Field("path", Scalar())
    def locale: SelectionBuilder[PageSearchResult, String] =
      _root_.caliban.client.SelectionBuilder.Field("locale", Scalar())
  }

  type PageListItem
  object PageListItem {
    def id: SelectionBuilder[PageListItem, Int] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def path: SelectionBuilder[PageListItem, String] = _root_.caliban.client.SelectionBuilder.Field("path", Scalar())
    def locale: SelectionBuilder[PageListItem, String] =
      _root_.caliban.client.SelectionBuilder.Field("locale", Scalar())
    def title: SelectionBuilder[PageListItem, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("title", OptionOf(Scalar()))
    def description: SelectionBuilder[PageListItem, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("description", OptionOf(Scalar()))
    def contentType: SelectionBuilder[PageListItem, String] =
      _root_.caliban.client.SelectionBuilder.Field("contentType", Scalar())
    def isPublished: SelectionBuilder[PageListItem, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isPublished", Scalar())
    def isPrivate: SelectionBuilder[PageListItem, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isPrivate", Scalar())
    def privateNS: SelectionBuilder[PageListItem, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("privateNS", OptionOf(Scalar()))
    def createdAt: SelectionBuilder[PageListItem, Date] =
      _root_.caliban.client.SelectionBuilder.Field("createdAt", Scalar())
    def updatedAt: SelectionBuilder[PageListItem, Date] =
      _root_.caliban.client.SelectionBuilder.Field("updatedAt", Scalar())
    def tags: SelectionBuilder[PageListItem, scala.Option[List[scala.Option[String]]]] =
      _root_.caliban.client.SelectionBuilder.Field("tags", OptionOf(ListOf(OptionOf(Scalar()))))
  }

  type PageTreeItem
  object PageTreeItem {
    def id: SelectionBuilder[PageTreeItem, Int] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def path: SelectionBuilder[PageTreeItem, String] = _root_.caliban.client.SelectionBuilder.Field("path", Scalar())
    def depth: SelectionBuilder[PageTreeItem, Int] = _root_.caliban.client.SelectionBuilder.Field("depth", Scalar())
    def title: SelectionBuilder[PageTreeItem, String] = _root_.caliban.client.SelectionBuilder.Field("title", Scalar())
    def isPrivate: SelectionBuilder[PageTreeItem, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isPrivate", Scalar())
    def isFolder: SelectionBuilder[PageTreeItem, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isFolder", Scalar())
    def privateNS: SelectionBuilder[PageTreeItem, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("privateNS", OptionOf(Scalar()))
    def parent: SelectionBuilder[PageTreeItem, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("parent", OptionOf(Scalar()))
    def pageId: SelectionBuilder[PageTreeItem, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("pageId", OptionOf(Scalar()))
    def locale: SelectionBuilder[PageTreeItem, String] =
      _root_.caliban.client.SelectionBuilder.Field("locale", Scalar())
  }

  type PageLinkItem
  object PageLinkItem {
    def id: SelectionBuilder[PageLinkItem, Int] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def path: SelectionBuilder[PageLinkItem, String] = _root_.caliban.client.SelectionBuilder.Field("path", Scalar())
    def title: SelectionBuilder[PageLinkItem, String] = _root_.caliban.client.SelectionBuilder.Field("title", Scalar())
    def links: SelectionBuilder[PageLinkItem, List[scala.Option[String]]] =
      _root_.caliban.client.SelectionBuilder.Field("links", ListOf(OptionOf(Scalar())))
  }

  type PageConflictLatest
  object PageConflictLatest {
    def id: SelectionBuilder[PageConflictLatest, Int] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def authorId: SelectionBuilder[PageConflictLatest, String] =
      _root_.caliban.client.SelectionBuilder.Field("authorId", Scalar())
    def authorName: SelectionBuilder[PageConflictLatest, String] =
      _root_.caliban.client.SelectionBuilder.Field("authorName", Scalar())
    def content: SelectionBuilder[PageConflictLatest, String] =
      _root_.caliban.client.SelectionBuilder.Field("content", Scalar())
    def createdAt: SelectionBuilder[PageConflictLatest, Date] =
      _root_.caliban.client.SelectionBuilder.Field("createdAt", Scalar())
    def description: SelectionBuilder[PageConflictLatest, String] =
      _root_.caliban.client.SelectionBuilder.Field("description", Scalar())
    def isPublished: SelectionBuilder[PageConflictLatest, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isPublished", Scalar())
    def locale: SelectionBuilder[PageConflictLatest, String] =
      _root_.caliban.client.SelectionBuilder.Field("locale", Scalar())
    def path: SelectionBuilder[PageConflictLatest, String] =
      _root_.caliban.client.SelectionBuilder.Field("path", Scalar())
    def tags: SelectionBuilder[PageConflictLatest, scala.Option[List[scala.Option[String]]]] =
      _root_.caliban.client.SelectionBuilder.Field("tags", OptionOf(ListOf(OptionOf(Scalar()))))
    def title: SelectionBuilder[PageConflictLatest, String] =
      _root_.caliban.client.SelectionBuilder.Field("title", Scalar())
    def updatedAt: SelectionBuilder[PageConflictLatest, Date] =
      _root_.caliban.client.SelectionBuilder.Field("updatedAt", Scalar())
  }

  type RenderingQuery
  object RenderingQuery {
    def renderers[A](filter: scala.Option[String] = None, orderBy: scala.Option[String] = None)(
      innerSelection: SelectionBuilder[Renderer, A]
    )(implicit
      encoder0: ArgEncoder[scala.Option[String]],
      encoder1: ArgEncoder[scala.Option[String]]
    ): SelectionBuilder[RenderingQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field(
        "renderers",
        OptionOf(ListOf(OptionOf(Obj(innerSelection)))),
        arguments =
          List(Argument("filter", filter, "String")(encoder0), Argument("orderBy", orderBy, "String")(encoder1))
      )
  }

  type RenderingMutation
  object RenderingMutation {
    def updateRenderers[A](
      renderers: scala.Option[List[scala.Option[RendererInput]]] = None
    )(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[scala.Option[List[scala.Option[RendererInput]]]]
    ): SelectionBuilder[RenderingMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "updateRenderers",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("renderers", renderers, "[RendererInput]")(encoder0))
    )
  }

  type Renderer
  object Renderer {
    def isEnabled: SelectionBuilder[Renderer, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isEnabled", Scalar())
    def key: SelectionBuilder[Renderer, String] = _root_.caliban.client.SelectionBuilder.Field("key", Scalar())
    def title: SelectionBuilder[Renderer, String] = _root_.caliban.client.SelectionBuilder.Field("title", Scalar())
    def description: SelectionBuilder[Renderer, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("description", OptionOf(Scalar()))
    def icon: SelectionBuilder[Renderer, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("icon", OptionOf(Scalar()))
    def dependsOn: SelectionBuilder[Renderer, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("dependsOn", OptionOf(Scalar()))
    def input: SelectionBuilder[Renderer, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("input", OptionOf(Scalar()))
    def output: SelectionBuilder[Renderer, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("output", OptionOf(Scalar()))
    def config[A](
      innerSelection: SelectionBuilder[KeyValuePair, A]
    ): SelectionBuilder[Renderer, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("config", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
  }

  type SearchQuery
  object SearchQuery {
    def searchEngines[A](filter: scala.Option[String] = None, orderBy: scala.Option[String] = None)(
      innerSelection: SelectionBuilder[SearchEngine, A]
    )(implicit
      encoder0: ArgEncoder[scala.Option[String]],
      encoder1: ArgEncoder[scala.Option[String]]
    ): SelectionBuilder[SearchQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field(
        "searchEngines",
        OptionOf(ListOf(OptionOf(Obj(innerSelection)))),
        arguments =
          List(Argument("filter", filter, "String")(encoder0), Argument("orderBy", orderBy, "String")(encoder1))
      )
  }

  type SearchMutation
  object SearchMutation {
    def updateSearchEngines[A](
      engines: scala.Option[List[scala.Option[SearchEngineInput]]] = None
    )(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[scala.Option[List[scala.Option[SearchEngineInput]]]]
    ): SelectionBuilder[SearchMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "updateSearchEngines",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("engines", engines, "[SearchEngineInput]")(encoder0))
    )
    def rebuildIndex[A](
      innerSelection: SelectionBuilder[DefaultResponse, A]
    ): SelectionBuilder[SearchMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("rebuildIndex", OptionOf(Obj(innerSelection)))
  }

  type SearchEngine
  object SearchEngine {
    def isEnabled: SelectionBuilder[SearchEngine, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isEnabled", Scalar())
    def key: SelectionBuilder[SearchEngine, String] = _root_.caliban.client.SelectionBuilder.Field("key", Scalar())
    def title: SelectionBuilder[SearchEngine, String] = _root_.caliban.client.SelectionBuilder.Field("title", Scalar())
    def description: SelectionBuilder[SearchEngine, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("description", OptionOf(Scalar()))
    def logo: SelectionBuilder[SearchEngine, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("logo", OptionOf(Scalar()))
    def website: SelectionBuilder[SearchEngine, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("website", OptionOf(Scalar()))
    def isAvailable: SelectionBuilder[SearchEngine, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("isAvailable", OptionOf(Scalar()))
    def config[A](
      innerSelection: SelectionBuilder[KeyValuePair, A]
    ): SelectionBuilder[SearchEngine, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("config", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
  }

  type SiteQuery
  object SiteQuery {
    def config[A](innerSelection: SelectionBuilder[SiteConfig, A]): SelectionBuilder[SiteQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("config", OptionOf(Obj(innerSelection)))
  }

  type SiteMutation
  object SiteMutation {
    def updateConfig[A](
      host: scala.Option[String] = None,
      title: scala.Option[String] = None,
      description: scala.Option[String] = None,
      robots: scala.Option[List[scala.Option[String]]] = None,
      analyticsService: scala.Option[String] = None,
      analyticsId: scala.Option[String] = None,
      company: scala.Option[String] = None,
      contentLicense: scala.Option[String] = None,
      footerOverride: scala.Option[String] = None,
      logoUrl: scala.Option[String] = None,
      pageExtensions: scala.Option[String] = None,
      authAutoLogin: scala.Option[Boolean] = None,
      authEnforce2FA: scala.Option[Boolean] = None,
      authHideLocal: scala.Option[Boolean] = None,
      authLoginBgUrl: scala.Option[String] = None,
      authJwtAudience: scala.Option[String] = None,
      authJwtExpiration: scala.Option[String] = None,
      authJwtRenewablePeriod: scala.Option[String] = None,
      editFab: scala.Option[Boolean] = None,
      editMenuBar: scala.Option[Boolean] = None,
      editMenuBtn: scala.Option[Boolean] = None,
      editMenuExternalBtn: scala.Option[Boolean] = None,
      editMenuExternalName: scala.Option[String] = None,
      editMenuExternalIcon: scala.Option[String] = None,
      editMenuExternalUrl: scala.Option[String] = None,
      featurePageRatings: scala.Option[Boolean] = None,
      featurePageComments: scala.Option[Boolean] = None,
      featurePersonalWikis: scala.Option[Boolean] = None,
      securityOpenRedirect: scala.Option[Boolean] = None,
      securityIframe: scala.Option[Boolean] = None,
      securityReferrerPolicy: scala.Option[Boolean] = None,
      securityTrustProxy: scala.Option[Boolean] = None,
      securitySRI: scala.Option[Boolean] = None,
      securityHSTS: scala.Option[Boolean] = None,
      securityHSTSDuration: scala.Option[Int] = None,
      securityCSP: scala.Option[Boolean] = None,
      securityCSPDirectives: scala.Option[String] = None,
      uploadMaxFileSize: scala.Option[Int] = None,
      uploadMaxFiles: scala.Option[Int] = None,
      uploadScanSVG: scala.Option[Boolean] = None,
      uploadForceDownload: scala.Option[Boolean] = None
    )(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[scala.Option[String]],
      encoder1: ArgEncoder[scala.Option[String]],
      encoder2: ArgEncoder[scala.Option[String]],
      encoder3: ArgEncoder[scala.Option[List[scala.Option[String]]]],
      encoder4: ArgEncoder[scala.Option[String]],
      encoder5: ArgEncoder[scala.Option[String]],
      encoder6: ArgEncoder[scala.Option[String]],
      encoder7: ArgEncoder[scala.Option[String]],
      encoder8: ArgEncoder[scala.Option[String]],
      encoder9: ArgEncoder[scala.Option[String]],
      encoder10: ArgEncoder[scala.Option[String]],
      encoder11: ArgEncoder[scala.Option[Boolean]],
      encoder12: ArgEncoder[scala.Option[Boolean]],
      encoder13: ArgEncoder[scala.Option[Boolean]],
      encoder14: ArgEncoder[scala.Option[String]],
      encoder15: ArgEncoder[scala.Option[String]],
      encoder16: ArgEncoder[scala.Option[String]],
      encoder17: ArgEncoder[scala.Option[String]],
      encoder18: ArgEncoder[scala.Option[Boolean]],
      encoder19: ArgEncoder[scala.Option[Boolean]],
      encoder20: ArgEncoder[scala.Option[Boolean]],
      encoder21: ArgEncoder[scala.Option[Boolean]],
      encoder22: ArgEncoder[scala.Option[String]],
      encoder23: ArgEncoder[scala.Option[String]],
      encoder24: ArgEncoder[scala.Option[String]],
      encoder25: ArgEncoder[scala.Option[Boolean]],
      encoder26: ArgEncoder[scala.Option[Boolean]],
      encoder27: ArgEncoder[scala.Option[Boolean]],
      encoder28: ArgEncoder[scala.Option[Boolean]],
      encoder29: ArgEncoder[scala.Option[Boolean]],
      encoder30: ArgEncoder[scala.Option[Boolean]],
      encoder31: ArgEncoder[scala.Option[Boolean]],
      encoder32: ArgEncoder[scala.Option[Boolean]],
      encoder33: ArgEncoder[scala.Option[Boolean]],
      encoder34: ArgEncoder[scala.Option[Int]],
      encoder35: ArgEncoder[scala.Option[Boolean]],
      encoder36: ArgEncoder[scala.Option[String]],
      encoder37: ArgEncoder[scala.Option[Int]],
      encoder38: ArgEncoder[scala.Option[Int]],
      encoder39: ArgEncoder[scala.Option[Boolean]],
      encoder40: ArgEncoder[scala.Option[Boolean]]
    ): SelectionBuilder[SiteMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "updateConfig",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("host", host, "String")(encoder0),
        Argument("title", title, "String")(encoder1),
        Argument("description", description, "String")(encoder2),
        Argument("robots", robots, "[String]")(encoder3),
        Argument("analyticsService", analyticsService, "String")(encoder4),
        Argument("analyticsId", analyticsId, "String")(encoder5),
        Argument("company", company, "String")(encoder6),
        Argument("contentLicense", contentLicense, "String")(encoder7),
        Argument("footerOverride", footerOverride, "String")(encoder8),
        Argument("logoUrl", logoUrl, "String")(encoder9),
        Argument("pageExtensions", pageExtensions, "String")(encoder10),
        Argument("authAutoLogin", authAutoLogin, "Boolean")(encoder11),
        Argument("authEnforce2FA", authEnforce2FA, "Boolean")(encoder12),
        Argument("authHideLocal", authHideLocal, "Boolean")(encoder13),
        Argument("authLoginBgUrl", authLoginBgUrl, "String")(encoder14),
        Argument("authJwtAudience", authJwtAudience, "String")(encoder15),
        Argument("authJwtExpiration", authJwtExpiration, "String")(encoder16),
        Argument("authJwtRenewablePeriod", authJwtRenewablePeriod, "String")(encoder17),
        Argument("editFab", editFab, "Boolean")(encoder18),
        Argument("editMenuBar", editMenuBar, "Boolean")(encoder19),
        Argument("editMenuBtn", editMenuBtn, "Boolean")(encoder20),
        Argument("editMenuExternalBtn", editMenuExternalBtn, "Boolean")(encoder21),
        Argument("editMenuExternalName", editMenuExternalName, "String")(encoder22),
        Argument("editMenuExternalIcon", editMenuExternalIcon, "String")(encoder23),
        Argument("editMenuExternalUrl", editMenuExternalUrl, "String")(encoder24),
        Argument("featurePageRatings", featurePageRatings, "Boolean")(encoder25),
        Argument("featurePageComments", featurePageComments, "Boolean")(encoder26),
        Argument("featurePersonalWikis", featurePersonalWikis, "Boolean")(encoder27),
        Argument("securityOpenRedirect", securityOpenRedirect, "Boolean")(encoder28),
        Argument("securityIframe", securityIframe, "Boolean")(encoder29),
        Argument("securityReferrerPolicy", securityReferrerPolicy, "Boolean")(encoder30),
        Argument("securityTrustProxy", securityTrustProxy, "Boolean")(encoder31),
        Argument("securitySRI", securitySRI, "Boolean")(encoder32),
        Argument("securityHSTS", securityHSTS, "Boolean")(encoder33),
        Argument("securityHSTSDuration", securityHSTSDuration, "Int")(encoder34),
        Argument("securityCSP", securityCSP, "Boolean")(encoder35),
        Argument("securityCSPDirectives", securityCSPDirectives, "String")(encoder36),
        Argument("uploadMaxFileSize", uploadMaxFileSize, "Int")(encoder37),
        Argument("uploadMaxFiles", uploadMaxFiles, "Int")(encoder38),
        Argument("uploadScanSVG", uploadScanSVG, "Boolean")(encoder39),
        Argument("uploadForceDownload", uploadForceDownload, "Boolean")(encoder40)
      )
    )
  }

  type SiteConfig
  object SiteConfig {
    def host: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("host", OptionOf(Scalar()))
    def title: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("title", OptionOf(Scalar()))
    def description: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("description", OptionOf(Scalar()))
    def robots: SelectionBuilder[SiteConfig, scala.Option[List[scala.Option[String]]]] =
      _root_.caliban.client.SelectionBuilder.Field("robots", OptionOf(ListOf(OptionOf(Scalar()))))
    def analyticsService: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("analyticsService", OptionOf(Scalar()))
    def analyticsId: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("analyticsId", OptionOf(Scalar()))
    def company: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("company", OptionOf(Scalar()))
    def contentLicense: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("contentLicense", OptionOf(Scalar()))
    def footerOverride: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("footerOverride", OptionOf(Scalar()))
    def logoUrl: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("logoUrl", OptionOf(Scalar()))
    def pageExtensions: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("pageExtensions", OptionOf(Scalar()))
    def authAutoLogin: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("authAutoLogin", OptionOf(Scalar()))
    def authEnforce2FA: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("authEnforce2FA", OptionOf(Scalar()))
    def authHideLocal: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("authHideLocal", OptionOf(Scalar()))
    def authLoginBgUrl: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("authLoginBgUrl", OptionOf(Scalar()))
    def authJwtAudience: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("authJwtAudience", OptionOf(Scalar()))
    def authJwtExpiration: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("authJwtExpiration", OptionOf(Scalar()))
    def authJwtRenewablePeriod: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("authJwtRenewablePeriod", OptionOf(Scalar()))
    def editFab: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("editFab", OptionOf(Scalar()))
    def editMenuBar: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("editMenuBar", OptionOf(Scalar()))
    def editMenuBtn: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("editMenuBtn", OptionOf(Scalar()))
    def editMenuExternalBtn: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("editMenuExternalBtn", OptionOf(Scalar()))
    def editMenuExternalName: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("editMenuExternalName", OptionOf(Scalar()))
    def editMenuExternalIcon: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("editMenuExternalIcon", OptionOf(Scalar()))
    def editMenuExternalUrl: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("editMenuExternalUrl", OptionOf(Scalar()))
    def featurePageRatings: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("featurePageRatings", OptionOf(Scalar()))
    def featurePageComments: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("featurePageComments", OptionOf(Scalar()))
    def featurePersonalWikis: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("featurePersonalWikis", OptionOf(Scalar()))
    def securityOpenRedirect: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("securityOpenRedirect", OptionOf(Scalar()))
    def securityIframe: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("securityIframe", OptionOf(Scalar()))
    def securityReferrerPolicy: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("securityReferrerPolicy", OptionOf(Scalar()))
    def securityTrustProxy: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("securityTrustProxy", OptionOf(Scalar()))
    def securitySRI: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("securitySRI", OptionOf(Scalar()))
    def securityHSTS: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("securityHSTS", OptionOf(Scalar()))
    def securityHSTSDuration: SelectionBuilder[SiteConfig, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("securityHSTSDuration", OptionOf(Scalar()))
    def securityCSP: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("securityCSP", OptionOf(Scalar()))
    def securityCSPDirectives: SelectionBuilder[SiteConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("securityCSPDirectives", OptionOf(Scalar()))
    def uploadMaxFileSize: SelectionBuilder[SiteConfig, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("uploadMaxFileSize", OptionOf(Scalar()))
    def uploadMaxFiles: SelectionBuilder[SiteConfig, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("uploadMaxFiles", OptionOf(Scalar()))
    def uploadScanSVG: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("uploadScanSVG", OptionOf(Scalar()))
    def uploadForceDownload: SelectionBuilder[SiteConfig, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("uploadForceDownload", OptionOf(Scalar()))
  }

  type StorageQuery
  object StorageQuery {
    def targets[A](
      innerSelection: SelectionBuilder[StorageTarget, A]
    ): SelectionBuilder[StorageQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("targets", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
    def status[A](
      innerSelection: SelectionBuilder[StorageStatus, A]
    ): SelectionBuilder[StorageQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("status", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
  }

  type StorageMutation
  object StorageMutation {
    def updateTargets[A](
      targets: List[scala.Option[StorageTargetInput]] = Nil
    )(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[List[scala.Option[StorageTargetInput]]]
    ): SelectionBuilder[StorageMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "updateTargets",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("targets", targets, "[StorageTargetInput]!")(encoder0))
    )
    def executeAction[A](targetKey: String, handler: String)(
      innerSelection: SelectionBuilder[DefaultResponse, A]
    )(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[String]
    ): SelectionBuilder[StorageMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "executeAction",
      OptionOf(Obj(innerSelection)),
      arguments =
        List(Argument("targetKey", targetKey, "String!")(encoder0), Argument("handler", handler, "String!")(encoder1))
    )
  }

  type StorageTarget
  object StorageTarget {
    def isAvailable: SelectionBuilder[StorageTarget, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isAvailable", Scalar())
    def isEnabled: SelectionBuilder[StorageTarget, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isEnabled", Scalar())
    def key: SelectionBuilder[StorageTarget, String] = _root_.caliban.client.SelectionBuilder.Field("key", Scalar())
    def title: SelectionBuilder[StorageTarget, String] = _root_.caliban.client.SelectionBuilder.Field("title", Scalar())
    def description: SelectionBuilder[StorageTarget, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("description", OptionOf(Scalar()))
    def logo: SelectionBuilder[StorageTarget, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("logo", OptionOf(Scalar()))
    def website: SelectionBuilder[StorageTarget, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("website", OptionOf(Scalar()))
    def supportedModes: SelectionBuilder[StorageTarget, scala.Option[List[scala.Option[String]]]] =
      _root_.caliban.client.SelectionBuilder.Field("supportedModes", OptionOf(ListOf(OptionOf(Scalar()))))
    def mode: SelectionBuilder[StorageTarget, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("mode", OptionOf(Scalar()))
    def hasSchedule: SelectionBuilder[StorageTarget, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("hasSchedule", Scalar())
    def syncInterval: SelectionBuilder[StorageTarget, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("syncInterval", OptionOf(Scalar()))
    def syncIntervalDefault: SelectionBuilder[StorageTarget, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("syncIntervalDefault", OptionOf(Scalar()))
    def config[A](
      innerSelection: SelectionBuilder[KeyValuePair, A]
    ): SelectionBuilder[StorageTarget, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("config", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
    def actions[A](
      innerSelection: SelectionBuilder[StorageTargetAction, A]
    ): SelectionBuilder[StorageTarget, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("actions", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
  }

  type StorageStatus
  object StorageStatus {
    def key: SelectionBuilder[StorageStatus, String] = _root_.caliban.client.SelectionBuilder.Field("key", Scalar())
    def title: SelectionBuilder[StorageStatus, String] = _root_.caliban.client.SelectionBuilder.Field("title", Scalar())
    def status: SelectionBuilder[StorageStatus, String] =
      _root_.caliban.client.SelectionBuilder.Field("status", Scalar())
    def message: SelectionBuilder[StorageStatus, String] =
      _root_.caliban.client.SelectionBuilder.Field("message", Scalar())
    def lastAttempt: SelectionBuilder[StorageStatus, String] =
      _root_.caliban.client.SelectionBuilder.Field("lastAttempt", Scalar())
  }

  type StorageTargetAction
  object StorageTargetAction {
    def handler: SelectionBuilder[StorageTargetAction, String] =
      _root_.caliban.client.SelectionBuilder.Field("handler", Scalar())
    def label: SelectionBuilder[StorageTargetAction, String] =
      _root_.caliban.client.SelectionBuilder.Field("label", Scalar())
    def hint: SelectionBuilder[StorageTargetAction, String] =
      _root_.caliban.client.SelectionBuilder.Field("hint", Scalar())
  }

  type SystemQuery
  object SystemQuery {
    def flags[A](
      innerSelection: SelectionBuilder[SystemFlag, A]
    ): SelectionBuilder[SystemQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("flags", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
    def info[A](innerSelection: SelectionBuilder[SystemInfo, A]): SelectionBuilder[SystemQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("info", OptionOf(Obj(innerSelection)))
    def extensions[A](
      innerSelection: SelectionBuilder[SystemExtension, A]
    ): SelectionBuilder[SystemQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("extensions", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
    def exportStatus[A](
      innerSelection: SelectionBuilder[SystemExportStatus, A]
    ): SelectionBuilder[SystemQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("exportStatus", OptionOf(Obj(innerSelection)))
  }

  type SystemMutation
  object SystemMutation {
    def updateFlags[A](
      flags: List[scala.Option[SystemFlagInput]] = Nil
    )(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[List[scala.Option[SystemFlagInput]]]
    ): SelectionBuilder[SystemMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "updateFlags",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("flags", flags, "[SystemFlagInput]!")(encoder0))
    )
    def resetTelemetryClientId[A](
      innerSelection: SelectionBuilder[DefaultResponse, A]
    ): SelectionBuilder[SystemMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("resetTelemetryClientId", OptionOf(Obj(innerSelection)))
    def setTelemetry[A](enabled: Boolean)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Boolean]
    ): SelectionBuilder[SystemMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "setTelemetry",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("enabled", enabled, "Boolean!")(encoder0))
    )
    def performUpgrade[A](
      innerSelection: SelectionBuilder[DefaultResponse, A]
    ): SelectionBuilder[SystemMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("performUpgrade", OptionOf(Obj(innerSelection)))
    def importUsersFromV1[A](mongoDbConnString: String, groupMode: SystemImportUsersGroupMode)(
      innerSelection: SelectionBuilder[SystemImportUsersResponse, A]
    )(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[SystemImportUsersGroupMode]
    ): SelectionBuilder[SystemMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "importUsersFromV1",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("mongoDbConnString", mongoDbConnString, "String!")(encoder0),
        Argument("groupMode", groupMode, "SystemImportUsersGroupMode!")(encoder1)
      )
    )
    def setHTTPSRedirection[A](enabled: Boolean)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Boolean]
    ): SelectionBuilder[SystemMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "setHTTPSRedirection",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("enabled", enabled, "Boolean!")(encoder0))
    )
    def renewHTTPSCertificate[A](
      innerSelection: SelectionBuilder[DefaultResponse, A]
    ): SelectionBuilder[SystemMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("renewHTTPSCertificate", OptionOf(Obj(innerSelection)))
    def `export`[A](entities: List[scala.Option[String]] = Nil, path: String)(
      innerSelection: SelectionBuilder[DefaultResponse, A]
    )(implicit
      encoder0: ArgEncoder[List[scala.Option[String]]],
      encoder1: ArgEncoder[String]
    ): SelectionBuilder[SystemMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "export",
      OptionOf(Obj(innerSelection)),
      arguments =
        List(Argument("entities", entities, "[String]!")(encoder0), Argument("path", path, "String!")(encoder1))
    )
  }

  type SystemFlag
  object SystemFlag {
    def key: SelectionBuilder[SystemFlag, String] = _root_.caliban.client.SelectionBuilder.Field("key", Scalar())
    def value: SelectionBuilder[SystemFlag, Boolean] = _root_.caliban.client.SelectionBuilder.Field("value", Scalar())
  }

  type SystemInfo
  object SystemInfo {
    def configFile: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("configFile", OptionOf(Scalar()))
    def cpuCores: SelectionBuilder[SystemInfo, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("cpuCores", OptionOf(Scalar()))
    def currentVersion: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("currentVersion", OptionOf(Scalar()))
    def dbHost: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("dbHost", OptionOf(Scalar()))
    def dbType: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("dbType", OptionOf(Scalar()))
    def dbVersion: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("dbVersion", OptionOf(Scalar()))
    def groupsTotal: SelectionBuilder[SystemInfo, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("groupsTotal", OptionOf(Scalar()))
    def hostname: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("hostname", OptionOf(Scalar()))
    def httpPort: SelectionBuilder[SystemInfo, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("httpPort", OptionOf(Scalar()))
    def httpRedirection: SelectionBuilder[SystemInfo, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("httpRedirection", OptionOf(Scalar()))
    def httpsPort: SelectionBuilder[SystemInfo, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("httpsPort", OptionOf(Scalar()))
    def latestVersion: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("latestVersion", OptionOf(Scalar()))
    def latestVersionReleaseDate: SelectionBuilder[SystemInfo, scala.Option[Date]] =
      _root_.caliban.client.SelectionBuilder.Field("latestVersionReleaseDate", OptionOf(Scalar()))
    def nodeVersion: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("nodeVersion", OptionOf(Scalar()))
    def operatingSystem: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("operatingSystem", OptionOf(Scalar()))
    def pagesTotal: SelectionBuilder[SystemInfo, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("pagesTotal", OptionOf(Scalar()))
    def platform: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("platform", OptionOf(Scalar()))
    def ramTotal: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("ramTotal", OptionOf(Scalar()))
    def sslDomain: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("sslDomain", OptionOf(Scalar()))
    def sslExpirationDate: SelectionBuilder[SystemInfo, scala.Option[Date]] =
      _root_.caliban.client.SelectionBuilder.Field("sslExpirationDate", OptionOf(Scalar()))
    def sslProvider: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("sslProvider", OptionOf(Scalar()))
    def sslStatus: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("sslStatus", OptionOf(Scalar()))
    def sslSubscriberEmail: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("sslSubscriberEmail", OptionOf(Scalar()))
    def tagsTotal: SelectionBuilder[SystemInfo, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("tagsTotal", OptionOf(Scalar()))
    def telemetry: SelectionBuilder[SystemInfo, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("telemetry", OptionOf(Scalar()))
    def telemetryClientId: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("telemetryClientId", OptionOf(Scalar()))
    def upgradeCapable: SelectionBuilder[SystemInfo, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("upgradeCapable", OptionOf(Scalar()))
    def usersTotal: SelectionBuilder[SystemInfo, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("usersTotal", OptionOf(Scalar()))
    def workingDirectory: SelectionBuilder[SystemInfo, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("workingDirectory", OptionOf(Scalar()))
  }

  type SystemImportUsersResponse
  object SystemImportUsersResponse {
    def responseResult[A](
      innerSelection: SelectionBuilder[ResponseStatus, A]
    ): SelectionBuilder[SystemImportUsersResponse, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("responseResult", OptionOf(Obj(innerSelection)))
    def usersCount: SelectionBuilder[SystemImportUsersResponse, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("usersCount", OptionOf(Scalar()))
    def groupsCount: SelectionBuilder[SystemImportUsersResponse, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("groupsCount", OptionOf(Scalar()))
    def failed[A](
      innerSelection: SelectionBuilder[SystemImportUsersResponseFailed, A]
    ): SelectionBuilder[SystemImportUsersResponse, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("failed", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
  }

  type SystemImportUsersResponseFailed
  object SystemImportUsersResponseFailed {
    def provider: SelectionBuilder[SystemImportUsersResponseFailed, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("provider", OptionOf(Scalar()))
    def email: SelectionBuilder[SystemImportUsersResponseFailed, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("email", OptionOf(Scalar()))
    def error: SelectionBuilder[SystemImportUsersResponseFailed, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("error", OptionOf(Scalar()))
  }

  type SystemExtension
  object SystemExtension {
    def key: SelectionBuilder[SystemExtension, String] = _root_.caliban.client.SelectionBuilder.Field("key", Scalar())
    def title: SelectionBuilder[SystemExtension, String] =
      _root_.caliban.client.SelectionBuilder.Field("title", Scalar())
    def description: SelectionBuilder[SystemExtension, String] =
      _root_.caliban.client.SelectionBuilder.Field("description", Scalar())
    def isInstalled: SelectionBuilder[SystemExtension, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isInstalled", Scalar())
    def isCompatible: SelectionBuilder[SystemExtension, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isCompatible", Scalar())
  }

  type SystemExportStatus
  object SystemExportStatus {
    def status: SelectionBuilder[SystemExportStatus, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("status", OptionOf(Scalar()))
    def progress: SelectionBuilder[SystemExportStatus, scala.Option[Int]] =
      _root_.caliban.client.SelectionBuilder.Field("progress", OptionOf(Scalar()))
    def message: SelectionBuilder[SystemExportStatus, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("message", OptionOf(Scalar()))
    def startedAt: SelectionBuilder[SystemExportStatus, scala.Option[Date]] =
      _root_.caliban.client.SelectionBuilder.Field("startedAt", OptionOf(Scalar()))
  }

  type ThemingQuery
  object ThemingQuery {
    def themes[A](
      innerSelection: SelectionBuilder[ThemingTheme, A]
    ): SelectionBuilder[ThemingQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("themes", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
    def config[A](innerSelection: SelectionBuilder[ThemingConfig, A]): SelectionBuilder[ThemingQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("config", OptionOf(Obj(innerSelection)))
  }

  type ThemingMutation
  object ThemingMutation {
    def setConfig[A](
      theme: String,
      iconset: String,
      darkMode: Boolean,
      tocPosition: scala.Option[String] = None,
      injectCSS: scala.Option[String] = None,
      injectHead: scala.Option[String] = None,
      injectBody: scala.Option[String] = None
    )(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[String],
      encoder2: ArgEncoder[Boolean],
      encoder3: ArgEncoder[scala.Option[String]],
      encoder4: ArgEncoder[scala.Option[String]],
      encoder5: ArgEncoder[scala.Option[String]],
      encoder6: ArgEncoder[scala.Option[String]]
    ): SelectionBuilder[ThemingMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "setConfig",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("theme", theme, "String!")(encoder0),
        Argument("iconset", iconset, "String!")(encoder1),
        Argument("darkMode", darkMode, "Boolean!")(encoder2),
        Argument("tocPosition", tocPosition, "String")(encoder3),
        Argument("injectCSS", injectCSS, "String")(encoder4),
        Argument("injectHead", injectHead, "String")(encoder5),
        Argument("injectBody", injectBody, "String")(encoder6)
      )
    )
  }

  type ThemingConfig
  object ThemingConfig {
    def theme: SelectionBuilder[ThemingConfig, String] = _root_.caliban.client.SelectionBuilder.Field("theme", Scalar())
    def iconset: SelectionBuilder[ThemingConfig, String] =
      _root_.caliban.client.SelectionBuilder.Field("iconset", Scalar())
    def darkMode: SelectionBuilder[ThemingConfig, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("darkMode", Scalar())
    def tocPosition: SelectionBuilder[ThemingConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("tocPosition", OptionOf(Scalar()))
    def injectCSS: SelectionBuilder[ThemingConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("injectCSS", OptionOf(Scalar()))
    def injectHead: SelectionBuilder[ThemingConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("injectHead", OptionOf(Scalar()))
    def injectBody: SelectionBuilder[ThemingConfig, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("injectBody", OptionOf(Scalar()))
  }

  type ThemingTheme
  object ThemingTheme {
    def key: SelectionBuilder[ThemingTheme, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("key", OptionOf(Scalar()))
    def title: SelectionBuilder[ThemingTheme, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("title", OptionOf(Scalar()))
    def author: SelectionBuilder[ThemingTheme, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("author", OptionOf(Scalar()))
  }

  type UserQuery
  object UserQuery {
    def list[A](filter: scala.Option[String] = None, orderBy: scala.Option[String] = None)(
      innerSelection: SelectionBuilder[UserMinimal, A]
    )(implicit
      encoder0: ArgEncoder[scala.Option[String]],
      encoder1: ArgEncoder[scala.Option[String]]
    ): SelectionBuilder[UserQuery, scala.Option[List[scala.Option[A]]]] = _root_.caliban.client.SelectionBuilder.Field(
      "list",
      OptionOf(ListOf(OptionOf(Obj(innerSelection)))),
      arguments = List(Argument("filter", filter, "String")(encoder0), Argument("orderBy", orderBy, "String")(encoder1))
    )
    def search[A](query: String)(innerSelection: SelectionBuilder[UserMinimal, A])(implicit
      encoder0: ArgEncoder[String]
    ): SelectionBuilder[UserQuery, scala.Option[List[scala.Option[A]]]] = _root_.caliban.client.SelectionBuilder.Field(
      "search",
      OptionOf(ListOf(OptionOf(Obj(innerSelection)))),
      arguments = List(Argument("query", query, "String!")(encoder0))
    )
    def single[A](id: Int)(innerSelection: SelectionBuilder[User, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[UserQuery, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("single", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
    def profile[A](innerSelection: SelectionBuilder[UserProfile, A]): SelectionBuilder[UserQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("profile", OptionOf(Obj(innerSelection)))
    def lastLogins[A](
      innerSelection: SelectionBuilder[UserLastLogin, A]
    ): SelectionBuilder[UserQuery, scala.Option[List[scala.Option[A]]]] =
      _root_.caliban.client.SelectionBuilder.Field("lastLogins", OptionOf(ListOf(OptionOf(Obj(innerSelection)))))
  }

  type UserMutation
  object UserMutation {
    def create[A](
      email: String,
      name: String,
      passwordRaw: scala.Option[String] = None,
      providerKey: String,
      groups: List[scala.Option[Int]] = Nil,
      mustChangePassword: scala.Option[Boolean] = None,
      sendWelcomeEmail: scala.Option[Boolean] = None
    )(innerSelection: SelectionBuilder[UserResponse, A])(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[String],
      encoder2: ArgEncoder[scala.Option[String]],
      encoder3: ArgEncoder[String],
      encoder4: ArgEncoder[List[scala.Option[Int]]],
      encoder5: ArgEncoder[scala.Option[Boolean]],
      encoder6: ArgEncoder[scala.Option[Boolean]]
    ): SelectionBuilder[UserMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "create",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("email", email, "String!")(encoder0),
        Argument("name", name, "String!")(encoder1),
        Argument("passwordRaw", passwordRaw, "String")(encoder2),
        Argument("providerKey", providerKey, "String!")(encoder3),
        Argument("groups", groups, "[Int]!")(encoder4),
        Argument("mustChangePassword", mustChangePassword, "Boolean")(encoder5),
        Argument("sendWelcomeEmail", sendWelcomeEmail, "Boolean")(encoder6)
      )
    )
    def update[A](
      id: Int,
      email: scala.Option[String] = None,
      name: scala.Option[String] = None,
      newPassword: scala.Option[String] = None,
      groups: scala.Option[List[scala.Option[Int]]] = None,
      location: scala.Option[String] = None,
      jobTitle: scala.Option[String] = None,
      timezone: scala.Option[String] = None,
      dateFormat: scala.Option[String] = None,
      appearance: scala.Option[String] = None
    )(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int],
      encoder1: ArgEncoder[scala.Option[String]],
      encoder2: ArgEncoder[scala.Option[String]],
      encoder3: ArgEncoder[scala.Option[String]],
      encoder4: ArgEncoder[scala.Option[List[scala.Option[Int]]]],
      encoder5: ArgEncoder[scala.Option[String]],
      encoder6: ArgEncoder[scala.Option[String]],
      encoder7: ArgEncoder[scala.Option[String]],
      encoder8: ArgEncoder[scala.Option[String]],
      encoder9: ArgEncoder[scala.Option[String]]
    ): SelectionBuilder[UserMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "update",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("id", id, "Int!")(encoder0),
        Argument("email", email, "String")(encoder1),
        Argument("name", name, "String")(encoder2),
        Argument("newPassword", newPassword, "String")(encoder3),
        Argument("groups", groups, "[Int]")(encoder4),
        Argument("location", location, "String")(encoder5),
        Argument("jobTitle", jobTitle, "String")(encoder6),
        Argument("timezone", timezone, "String")(encoder7),
        Argument("dateFormat", dateFormat, "String")(encoder8),
        Argument("appearance", appearance, "String")(encoder9)
      )
    )
    def delete[A](id: Int, replaceId: Int)(
      innerSelection: SelectionBuilder[DefaultResponse, A]
    )(implicit encoder0: ArgEncoder[Int], encoder1: ArgEncoder[Int]): SelectionBuilder[UserMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field(
        "delete",
        OptionOf(Obj(innerSelection)),
        arguments = List(Argument("id", id, "Int!")(encoder0), Argument("replaceId", replaceId, "Int!")(encoder1))
      )
    def verify[A](id: Int)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[UserMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("verify", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
    def activate[A](id: Int)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[UserMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("activate", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
    def deactivate[A](id: Int)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[UserMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("deactivate", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
    def enableTFA[A](id: Int)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[UserMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("enableTFA", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
    def disableTFA[A](id: Int)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[UserMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("disableTFA", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
    def resetPassword[A](id: Int)(innerSelection: SelectionBuilder[DefaultResponse, A])(implicit
      encoder0: ArgEncoder[Int]
    ): SelectionBuilder[UserMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder
      .Field("resetPassword", OptionOf(Obj(innerSelection)), arguments = List(Argument("id", id, "Int!")(encoder0)))
    def updateProfile[A](
      name: String,
      location: String,
      jobTitle: String,
      timezone: String,
      dateFormat: String,
      appearance: String
    )(innerSelection: SelectionBuilder[UserTokenResponse, A])(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[String],
      encoder2: ArgEncoder[String],
      encoder3: ArgEncoder[String],
      encoder4: ArgEncoder[String],
      encoder5: ArgEncoder[String]
    ): SelectionBuilder[UserMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "updateProfile",
      OptionOf(Obj(innerSelection)),
      arguments = List(
        Argument("name", name, "String!")(encoder0),
        Argument("location", location, "String!")(encoder1),
        Argument("jobTitle", jobTitle, "String!")(encoder2),
        Argument("timezone", timezone, "String!")(encoder3),
        Argument("dateFormat", dateFormat, "String!")(encoder4),
        Argument("appearance", appearance, "String!")(encoder5)
      )
    )
    def changePassword[A](current: String, `new`: String)(
      innerSelection: SelectionBuilder[UserTokenResponse, A]
    )(implicit
      encoder0: ArgEncoder[String],
      encoder1: ArgEncoder[String]
    ): SelectionBuilder[UserMutation, scala.Option[A]] = _root_.caliban.client.SelectionBuilder.Field(
      "changePassword",
      OptionOf(Obj(innerSelection)),
      arguments = List(Argument("current", current, "String!")(encoder0), Argument("new", `new`, "String!")(encoder1))
    )
  }

  type UserResponse
  object UserResponse {
    def responseResult[A](innerSelection: SelectionBuilder[ResponseStatus, A]): SelectionBuilder[UserResponse, A] =
      _root_.caliban.client.SelectionBuilder.Field("responseResult", Obj(innerSelection))
    def user[A](innerSelection: SelectionBuilder[User, A]): SelectionBuilder[UserResponse, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("user", OptionOf(Obj(innerSelection)))
  }

  type UserLastLogin
  object UserLastLogin {
    def id: SelectionBuilder[UserLastLogin, Int] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def name: SelectionBuilder[UserLastLogin, String] = _root_.caliban.client.SelectionBuilder.Field("name", Scalar())
    def lastLoginAt: SelectionBuilder[UserLastLogin, Date] =
      _root_.caliban.client.SelectionBuilder.Field("lastLoginAt", Scalar())
  }

  type UserMinimal
  object UserMinimal {
    def id: SelectionBuilder[UserMinimal, Int] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def name: SelectionBuilder[UserMinimal, String] = _root_.caliban.client.SelectionBuilder.Field("name", Scalar())
    def email: SelectionBuilder[UserMinimal, String] = _root_.caliban.client.SelectionBuilder.Field("email", Scalar())
    def providerKey: SelectionBuilder[UserMinimal, String] =
      _root_.caliban.client.SelectionBuilder.Field("providerKey", Scalar())
    def isSystem: SelectionBuilder[UserMinimal, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isSystem", Scalar())
    def isActive: SelectionBuilder[UserMinimal, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isActive", Scalar())
    def createdAt: SelectionBuilder[UserMinimal, Date] =
      _root_.caliban.client.SelectionBuilder.Field("createdAt", Scalar())
    def lastLoginAt: SelectionBuilder[UserMinimal, scala.Option[Date]] =
      _root_.caliban.client.SelectionBuilder.Field("lastLoginAt", OptionOf(Scalar()))
  }

  type User
  object User {
    def id: SelectionBuilder[User, Int] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def name: SelectionBuilder[User, String] = _root_.caliban.client.SelectionBuilder.Field("name", Scalar())
    def email: SelectionBuilder[User, String] = _root_.caliban.client.SelectionBuilder.Field("email", Scalar())
    def providerKey: SelectionBuilder[User, String] =
      _root_.caliban.client.SelectionBuilder.Field("providerKey", Scalar())
    def providerName: SelectionBuilder[User, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("providerName", OptionOf(Scalar()))
    def providerId: SelectionBuilder[User, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("providerId", OptionOf(Scalar()))
    def providerIs2FACapable: SelectionBuilder[User, scala.Option[Boolean]] =
      _root_.caliban.client.SelectionBuilder.Field("providerIs2FACapable", OptionOf(Scalar()))
    def isSystem: SelectionBuilder[User, Boolean] = _root_.caliban.client.SelectionBuilder.Field("isSystem", Scalar())
    def isActive: SelectionBuilder[User, Boolean] = _root_.caliban.client.SelectionBuilder.Field("isActive", Scalar())
    def isVerified: SelectionBuilder[User, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isVerified", Scalar())
    def location: SelectionBuilder[User, String] = _root_.caliban.client.SelectionBuilder.Field("location", Scalar())
    def jobTitle: SelectionBuilder[User, String] = _root_.caliban.client.SelectionBuilder.Field("jobTitle", Scalar())
    def timezone: SelectionBuilder[User, String] = _root_.caliban.client.SelectionBuilder.Field("timezone", Scalar())
    def dateFormat: SelectionBuilder[User, String] =
      _root_.caliban.client.SelectionBuilder.Field("dateFormat", Scalar())
    def appearance: SelectionBuilder[User, String] =
      _root_.caliban.client.SelectionBuilder.Field("appearance", Scalar())
    def createdAt: SelectionBuilder[User, Date] = _root_.caliban.client.SelectionBuilder.Field("createdAt", Scalar())
    def updatedAt: SelectionBuilder[User, Date] = _root_.caliban.client.SelectionBuilder.Field("updatedAt", Scalar())
    def lastLoginAt: SelectionBuilder[User, scala.Option[Date]] =
      _root_.caliban.client.SelectionBuilder.Field("lastLoginAt", OptionOf(Scalar()))
    def tfaIsActive: SelectionBuilder[User, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("tfaIsActive", Scalar())
    def groups[A](innerSelection: SelectionBuilder[Group, A]): SelectionBuilder[User, List[scala.Option[A]]] =
      _root_.caliban.client.SelectionBuilder.Field("groups", ListOf(OptionOf(Obj(innerSelection))))
  }

  type UserProfile
  object UserProfile {
    def id: SelectionBuilder[UserProfile, Int] = _root_.caliban.client.SelectionBuilder.Field("id", Scalar())
    def name: SelectionBuilder[UserProfile, String] = _root_.caliban.client.SelectionBuilder.Field("name", Scalar())
    def email: SelectionBuilder[UserProfile, String] = _root_.caliban.client.SelectionBuilder.Field("email", Scalar())
    def providerKey: SelectionBuilder[UserProfile, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("providerKey", OptionOf(Scalar()))
    def providerName: SelectionBuilder[UserProfile, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("providerName", OptionOf(Scalar()))
    def isSystem: SelectionBuilder[UserProfile, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isSystem", Scalar())
    def isVerified: SelectionBuilder[UserProfile, Boolean] =
      _root_.caliban.client.SelectionBuilder.Field("isVerified", Scalar())
    def location: SelectionBuilder[UserProfile, String] =
      _root_.caliban.client.SelectionBuilder.Field("location", Scalar())
    def jobTitle: SelectionBuilder[UserProfile, String] =
      _root_.caliban.client.SelectionBuilder.Field("jobTitle", Scalar())
    def timezone: SelectionBuilder[UserProfile, String] =
      _root_.caliban.client.SelectionBuilder.Field("timezone", Scalar())
    def dateFormat: SelectionBuilder[UserProfile, String] =
      _root_.caliban.client.SelectionBuilder.Field("dateFormat", Scalar())
    def appearance: SelectionBuilder[UserProfile, String] =
      _root_.caliban.client.SelectionBuilder.Field("appearance", Scalar())
    def createdAt: SelectionBuilder[UserProfile, Date] =
      _root_.caliban.client.SelectionBuilder.Field("createdAt", Scalar())
    def updatedAt: SelectionBuilder[UserProfile, Date] =
      _root_.caliban.client.SelectionBuilder.Field("updatedAt", Scalar())
    def lastLoginAt: SelectionBuilder[UserProfile, scala.Option[Date]] =
      _root_.caliban.client.SelectionBuilder.Field("lastLoginAt", OptionOf(Scalar()))
    def groups: SelectionBuilder[UserProfile, List[scala.Option[String]]] =
      _root_.caliban.client.SelectionBuilder.Field("groups", ListOf(OptionOf(Scalar())))
    def pagesTotal: SelectionBuilder[UserProfile, Int] =
      _root_.caliban.client.SelectionBuilder.Field("pagesTotal", Scalar())
  }

  type UserTokenResponse
  object UserTokenResponse {
    def responseResult[A](innerSelection: SelectionBuilder[ResponseStatus, A]): SelectionBuilder[UserTokenResponse, A] =
      _root_.caliban.client.SelectionBuilder.Field("responseResult", Obj(innerSelection))
    def jwt: SelectionBuilder[UserTokenResponse, scala.Option[String]] =
      _root_.caliban.client.SelectionBuilder.Field("jwt", OptionOf(Scalar()))
  }

  final case class AnalyticsProviderInput(
    isEnabled: Boolean,
    key: String,
    config: scala.Option[List[scala.Option[KeyValuePairInput]]] = None
  )
  object AnalyticsProviderInput {
    implicit val encoder: ArgEncoder[AnalyticsProviderInput] = new ArgEncoder[AnalyticsProviderInput] {
      override def encode(value: AnalyticsProviderInput): __Value =
        __ObjectValue(
          List(
            "isEnabled" -> implicitly[ArgEncoder[Boolean]].encode(value.isEnabled),
            "key" -> implicitly[ArgEncoder[String]].encode(value.key),
            "config" -> value.config.fold(__NullValue: __Value)(value =>
              __ListValue(
                value.map(value =>
                  value.fold(__NullValue: __Value)(value => implicitly[ArgEncoder[KeyValuePairInput]].encode(value))
                )
              )
            )
          )
        )
    }
  }
  final case class AuthenticationStrategyInput(
    key: String,
    strategyKey: String,
    config: scala.Option[List[scala.Option[KeyValuePairInput]]] = None,
    displayName: String,
    order: Int,
    isEnabled: Boolean,
    selfRegistration: Boolean,
    domainWhitelist: List[scala.Option[String]] = Nil,
    autoEnrollGroups: List[scala.Option[Int]] = Nil
  )
  object AuthenticationStrategyInput {
    implicit val encoder: ArgEncoder[AuthenticationStrategyInput] = new ArgEncoder[AuthenticationStrategyInput] {
      override def encode(value: AuthenticationStrategyInput): __Value =
        __ObjectValue(
          List(
            "key" -> implicitly[ArgEncoder[String]].encode(value.key),
            "strategyKey" -> implicitly[ArgEncoder[String]].encode(value.strategyKey),
            "config" -> value.config.fold(__NullValue: __Value)(value =>
              __ListValue(
                value.map(value =>
                  value.fold(__NullValue: __Value)(value => implicitly[ArgEncoder[KeyValuePairInput]].encode(value))
                )
              )
            ),
            "displayName" -> implicitly[ArgEncoder[String]].encode(value.displayName),
            "order" -> implicitly[ArgEncoder[Int]].encode(value.order),
            "isEnabled" -> implicitly[ArgEncoder[Boolean]].encode(value.isEnabled),
            "selfRegistration" -> implicitly[ArgEncoder[Boolean]].encode(value.selfRegistration),
            "domainWhitelist" -> __ListValue(
              value.domainWhitelist.map(value =>
                value.fold(__NullValue: __Value)(value => implicitly[ArgEncoder[String]].encode(value))
              )
            ),
            "autoEnrollGroups" -> __ListValue(
              value.autoEnrollGroups.map(value =>
                value.fold(__NullValue: __Value)(value => implicitly[ArgEncoder[Int]].encode(value))
              )
            )
          )
        )
    }
  }
  final case class CommentProviderInput(
    isEnabled: Boolean,
    key: String,
    config: scala.Option[List[scala.Option[KeyValuePairInput]]] = None
  )
  object CommentProviderInput {
    implicit val encoder: ArgEncoder[CommentProviderInput] = new ArgEncoder[CommentProviderInput] {
      override def encode(value: CommentProviderInput): __Value =
        __ObjectValue(
          List(
            "isEnabled" -> implicitly[ArgEncoder[Boolean]].encode(value.isEnabled),
            "key" -> implicitly[ArgEncoder[String]].encode(value.key),
            "config" -> value.config.fold(__NullValue: __Value)(value =>
              __ListValue(
                value.map(value =>
                  value.fold(__NullValue: __Value)(value => implicitly[ArgEncoder[KeyValuePairInput]].encode(value))
                )
              )
            )
          )
        )
    }
  }
  final case class KeyValuePairInput(key: String, value: String)
  object KeyValuePairInput {
    implicit val encoder: ArgEncoder[KeyValuePairInput] = new ArgEncoder[KeyValuePairInput] {
      override def encode(value: KeyValuePairInput): __Value =
        __ObjectValue(
          List(
            "key" -> implicitly[ArgEncoder[String]].encode(value.key),
            "value" -> implicitly[ArgEncoder[String]].encode(value.value)
          )
        )
    }
  }
  final case class PageRuleInput(
    id: String,
    deny: Boolean,
    `match`: PageRuleMatch,
    roles: List[scala.Option[String]] = Nil,
    path: String,
    locales: List[scala.Option[String]] = Nil
  )
  object PageRuleInput {
    implicit val encoder: ArgEncoder[PageRuleInput] = new ArgEncoder[PageRuleInput] {
      override def encode(value: PageRuleInput): __Value =
        __ObjectValue(
          List(
            "id" -> implicitly[ArgEncoder[String]].encode(value.id),
            "deny" -> implicitly[ArgEncoder[Boolean]].encode(value.deny),
            "match" -> implicitly[ArgEncoder[PageRuleMatch]].encode(value.`match`),
            "roles" -> __ListValue(
              value.roles.map(value =>
                value.fold(__NullValue: __Value)(value => implicitly[ArgEncoder[String]].encode(value))
              )
            ),
            "path" -> implicitly[ArgEncoder[String]].encode(value.path),
            "locales" -> __ListValue(
              value.locales.map(value =>
                value.fold(__NullValue: __Value)(value => implicitly[ArgEncoder[String]].encode(value))
              )
            )
          )
        )
    }
  }
  final case class LoggerInput(
    isEnabled: Boolean,
    key: String,
    level: String,
    config: scala.Option[List[scala.Option[KeyValuePairInput]]] = None
  )
  object LoggerInput {
    implicit val encoder: ArgEncoder[LoggerInput] = new ArgEncoder[LoggerInput] {
      override def encode(value: LoggerInput): __Value =
        __ObjectValue(
          List(
            "isEnabled" -> implicitly[ArgEncoder[Boolean]].encode(value.isEnabled),
            "key" -> implicitly[ArgEncoder[String]].encode(value.key),
            "level" -> implicitly[ArgEncoder[String]].encode(value.level),
            "config" -> value.config.fold(__NullValue: __Value)(value =>
              __ListValue(
                value.map(value =>
                  value.fold(__NullValue: __Value)(value => implicitly[ArgEncoder[KeyValuePairInput]].encode(value))
                )
              )
            )
          )
        )
    }
  }
  final case class NavigationTreeInput(locale: String, items: List[scala.Option[NavigationItemInput]] = Nil)
  object NavigationTreeInput {
    implicit val encoder: ArgEncoder[NavigationTreeInput] = new ArgEncoder[NavigationTreeInput] {
      override def encode(value: NavigationTreeInput): __Value =
        __ObjectValue(
          List(
            "locale" -> implicitly[ArgEncoder[String]].encode(value.locale),
            "items" -> __ListValue(
              value.items.map(value =>
                value.fold(__NullValue: __Value)(value => implicitly[ArgEncoder[NavigationItemInput]].encode(value))
              )
            )
          )
        )
    }
  }
  final case class NavigationItemInput(
    id: String,
    kind: String,
    label: scala.Option[String] = None,
    icon: scala.Option[String] = None,
    targetType: scala.Option[String] = None,
    target: scala.Option[String] = None,
    visibilityMode: scala.Option[String] = None,
    visibilityGroups: scala.Option[List[scala.Option[Int]]] = None
  )
  object NavigationItemInput {
    implicit val encoder: ArgEncoder[NavigationItemInput] = new ArgEncoder[NavigationItemInput] {
      override def encode(value: NavigationItemInput): __Value =
        __ObjectValue(
          List(
            "id" -> implicitly[ArgEncoder[String]].encode(value.id),
            "kind" -> implicitly[ArgEncoder[String]].encode(value.kind),
            "label" -> value.label.fold(__NullValue: __Value)(value => implicitly[ArgEncoder[String]].encode(value)),
            "icon" -> value.icon.fold(__NullValue: __Value)(value => implicitly[ArgEncoder[String]].encode(value)),
            "targetType" -> value.targetType.fold(__NullValue: __Value)(value =>
              implicitly[ArgEncoder[String]].encode(value)
            ),
            "target" -> value.target.fold(__NullValue: __Value)(value => implicitly[ArgEncoder[String]].encode(value)),
            "visibilityMode" -> value.visibilityMode.fold(__NullValue: __Value)(value =>
              implicitly[ArgEncoder[String]].encode(value)
            ),
            "visibilityGroups" -> value.visibilityGroups.fold(__NullValue: __Value)(value =>
              __ListValue(
                value.map(value => value.fold(__NullValue: __Value)(value => implicitly[ArgEncoder[Int]].encode(value)))
              )
            )
          )
        )
    }
  }
  final case class RendererInput(
    isEnabled: Boolean,
    key: String,
    config: scala.Option[List[scala.Option[KeyValuePairInput]]] = None
  )
  object RendererInput {
    implicit val encoder: ArgEncoder[RendererInput] = new ArgEncoder[RendererInput] {
      override def encode(value: RendererInput): __Value =
        __ObjectValue(
          List(
            "isEnabled" -> implicitly[ArgEncoder[Boolean]].encode(value.isEnabled),
            "key" -> implicitly[ArgEncoder[String]].encode(value.key),
            "config" -> value.config.fold(__NullValue: __Value)(value =>
              __ListValue(
                value.map(value =>
                  value.fold(__NullValue: __Value)(value => implicitly[ArgEncoder[KeyValuePairInput]].encode(value))
                )
              )
            )
          )
        )
    }
  }
  final case class SearchEngineInput(
    isEnabled: Boolean,
    key: String,
    config: scala.Option[List[scala.Option[KeyValuePairInput]]] = None
  )
  object SearchEngineInput {
    implicit val encoder: ArgEncoder[SearchEngineInput] = new ArgEncoder[SearchEngineInput] {
      override def encode(value: SearchEngineInput): __Value =
        __ObjectValue(
          List(
            "isEnabled" -> implicitly[ArgEncoder[Boolean]].encode(value.isEnabled),
            "key" -> implicitly[ArgEncoder[String]].encode(value.key),
            "config" -> value.config.fold(__NullValue: __Value)(value =>
              __ListValue(
                value.map(value =>
                  value.fold(__NullValue: __Value)(value => implicitly[ArgEncoder[KeyValuePairInput]].encode(value))
                )
              )
            )
          )
        )
    }
  }
  final case class StorageTargetInput(
    isEnabled: Boolean,
    key: String,
    mode: String,
    syncInterval: scala.Option[String] = None,
    config: scala.Option[List[scala.Option[KeyValuePairInput]]] = None
  )
  object StorageTargetInput {
    implicit val encoder: ArgEncoder[StorageTargetInput] = new ArgEncoder[StorageTargetInput] {
      override def encode(value: StorageTargetInput): __Value =
        __ObjectValue(
          List(
            "isEnabled" -> implicitly[ArgEncoder[Boolean]].encode(value.isEnabled),
            "key" -> implicitly[ArgEncoder[String]].encode(value.key),
            "mode" -> implicitly[ArgEncoder[String]].encode(value.mode),
            "syncInterval" -> value.syncInterval.fold(__NullValue: __Value)(value =>
              implicitly[ArgEncoder[String]].encode(value)
            ),
            "config" -> value.config.fold(__NullValue: __Value)(value =>
              __ListValue(
                value.map(value =>
                  value.fold(__NullValue: __Value)(value => implicitly[ArgEncoder[KeyValuePairInput]].encode(value))
                )
              )
            )
          )
        )
    }
  }
  final case class SystemFlagInput(key: String, value: Boolean)
  object SystemFlagInput {
    implicit val encoder: ArgEncoder[SystemFlagInput] = new ArgEncoder[SystemFlagInput] {
      override def encode(value: SystemFlagInput): __Value =
        __ObjectValue(
          List(
            "key" -> implicitly[ArgEncoder[String]].encode(value.key),
            "value" -> implicitly[ArgEncoder[Boolean]].encode(value.value)
          )
        )
    }
  }
  type Query = _root_.caliban.client.Operations.RootQuery
  object Query {
    def analytics[A](
      innerSelection: SelectionBuilder[AnalyticsQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("analytics", OptionOf(Obj(innerSelection)))
    def assets[A](
      innerSelection: SelectionBuilder[AssetQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("assets", OptionOf(Obj(innerSelection)))
    def authentication[A](
      innerSelection: SelectionBuilder[AuthenticationQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("authentication", OptionOf(Obj(innerSelection)))
    def comments[A](
      innerSelection: SelectionBuilder[CommentQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("comments", OptionOf(Obj(innerSelection)))
    def contribute[A](
      innerSelection: SelectionBuilder[ContributeQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("contribute", OptionOf(Obj(innerSelection)))
    def groups[A](
      innerSelection: SelectionBuilder[GroupQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("groups", OptionOf(Obj(innerSelection)))
    def localization[A](
      innerSelection: SelectionBuilder[LocalizationQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("localization", OptionOf(Obj(innerSelection)))
    def logging[A](
      innerSelection: SelectionBuilder[LoggingQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("logging", OptionOf(Obj(innerSelection)))
    def mail[A](
      innerSelection: SelectionBuilder[MailQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("mail", OptionOf(Obj(innerSelection)))
    def navigation[A](
      innerSelection: SelectionBuilder[NavigationQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("navigation", OptionOf(Obj(innerSelection)))
    def pages[A](
      innerSelection: SelectionBuilder[PageQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("pages", OptionOf(Obj(innerSelection)))
    def rendering[A](
      innerSelection: SelectionBuilder[RenderingQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("rendering", OptionOf(Obj(innerSelection)))
    def search[A](
      innerSelection: SelectionBuilder[SearchQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("search", OptionOf(Obj(innerSelection)))
    def site[A](
      innerSelection: SelectionBuilder[SiteQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("site", OptionOf(Obj(innerSelection)))
    def storage[A](
      innerSelection: SelectionBuilder[StorageQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("storage", OptionOf(Obj(innerSelection)))
    def system[A](
      innerSelection: SelectionBuilder[SystemQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("system", OptionOf(Obj(innerSelection)))
    def theming[A](
      innerSelection: SelectionBuilder[ThemingQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("theming", OptionOf(Obj(innerSelection)))
    def users[A](
      innerSelection: SelectionBuilder[UserQuery, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("users", OptionOf(Obj(innerSelection)))
  }

  type Mutation = _root_.caliban.client.Operations.RootMutation
  object Mutation {
    def analytics[A](
      innerSelection: SelectionBuilder[AnalyticsMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("analytics", OptionOf(Obj(innerSelection)))
    def assets[A](
      innerSelection: SelectionBuilder[AssetMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("assets", OptionOf(Obj(innerSelection)))
    def authentication[A](
      innerSelection: SelectionBuilder[AuthenticationMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("authentication", OptionOf(Obj(innerSelection)))
    def comments[A](
      innerSelection: SelectionBuilder[CommentMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("comments", OptionOf(Obj(innerSelection)))
    def groups[A](
      innerSelection: SelectionBuilder[GroupMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("groups", OptionOf(Obj(innerSelection)))
    def localization[A](
      innerSelection: SelectionBuilder[LocalizationMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("localization", OptionOf(Obj(innerSelection)))
    def logging[A](
      innerSelection: SelectionBuilder[LoggingMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("logging", OptionOf(Obj(innerSelection)))
    def mail[A](
      innerSelection: SelectionBuilder[MailMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("mail", OptionOf(Obj(innerSelection)))
    def navigation[A](
      innerSelection: SelectionBuilder[NavigationMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("navigation", OptionOf(Obj(innerSelection)))
    def pages[A](
      innerSelection: SelectionBuilder[PageMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("pages", OptionOf(Obj(innerSelection)))
    def rendering[A](
      innerSelection: SelectionBuilder[RenderingMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("rendering", OptionOf(Obj(innerSelection)))
    def search[A](
      innerSelection: SelectionBuilder[SearchMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("search", OptionOf(Obj(innerSelection)))
    def site[A](
      innerSelection: SelectionBuilder[SiteMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("site", OptionOf(Obj(innerSelection)))
    def storage[A](
      innerSelection: SelectionBuilder[StorageMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("storage", OptionOf(Obj(innerSelection)))
    def system[A](
      innerSelection: SelectionBuilder[SystemMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("system", OptionOf(Obj(innerSelection)))
    def theming[A](
      innerSelection: SelectionBuilder[ThemingMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("theming", OptionOf(Obj(innerSelection)))
    def users[A](
      innerSelection: SelectionBuilder[UserMutation, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("users", OptionOf(Obj(innerSelection)))
  }

  type Subscription = _root_.caliban.client.Operations.RootSubscription
  object Subscription {
    def loggingLiveTrail[A](
      innerSelection: SelectionBuilder[LoggerTrailLine, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootSubscription, scala.Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field("loggingLiveTrail", OptionOf(Obj(innerSelection)))
  }

}
