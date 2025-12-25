val scala3Version = "3.7.1"

enablePlugins(JavaAppPackaging)

lazy val jsonLib = {
  val circeVersion = "0.14.1"
  Seq("io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-extras",
    "io.circe" %% "circe-parser").map(_ % circeVersion)
}

javacOptions ++= Seq("-encoding", "UTF-8")
scalacOptions ++= Seq("-encoding", "UTF-8")

lazy val root = project
  .in(file("."))
  .settings(
    name := "devops",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "com.sshtools" % "maverick-synergy-client" % "3.1.4",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.6",
      "ch.qos.logback" % "logback-classic" % "1.5.23",
      "io.circe" %% "circe-config" % "0.10.2",
      "io.circe" %% "circe-optics" % "0.15.1",
      "com.lihaoyi" %% "mainargs" % "0.7.7",
      "com.github.pathikrit" %% "better-files" % "3.9.2",
      "com.softwaremill.sttp.client4" %% "core" % "4.0.13",
      "com.softwaremill.sttp.client4" %% "circe" % "4.0.13",
      "com.github.ghostdogpr" %% "caliban-client" % "2.11.1",
      "org.bouncycastle" % "bcprov-jdk18on" % "1.83",
      "org.bouncycastle" % "bcpkix-jdk18on" % "1.83",
      "com.aliyun" % "aliyun-java-sdk-core" % "4.7.8",
      "com.aliyun" % "aliyun-java-sdk-cdn" % "3.8.8",
      "org.scala-lang.modules" %% "scala-parallel-collections" % "1.2.0",
      "org.apache.poi" % "poi" % "5.5.1",
      "org.apache.poi" % "poi-ooxml" % "5.5.1",
      // "com.lihaoyi" % "ammonite" % "3.0.2" cross CrossVersion.full,
      "org.scalameta" %% "munit" % "1.2.1" % Test
    ) ++ jsonLib
  )
