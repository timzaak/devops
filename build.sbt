val scala3Version = "3.7.1"

enablePlugins(JavaAppPackaging)

lazy val jsonLib = {
  val circeVersion = "0.14.1"
  Seq("io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-extras",
    "io.circe" %% "circe-parser").map(_ % circeVersion)
}

lazy val root = project
  .in(file("."))
  .settings(
    name := "devops",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "com.sshtools" % "maverick-synergy-client" % "3.1.2",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
      "ch.qos.logback" % "logback-classic" % "1.5.18",
      "io.circe" %% "circe-config" % "0.10.2",
      "io.circe" %% "circe-optics" % "0.15.1",
      "com.lihaoyi" %% "mainargs" % "0.7.6",
      "com.github.pathikrit" %% "better-files" % "3.9.2",
      "com.softwaremill.sttp.client4" %% "core" % "4.0.9",
      "com.softwaremill.sttp.client4" %% "circe" % "4.0.9",
      "com.github.ghostdogpr" %% "caliban-client" % "2.10.0",
      "org.bouncycastle" % "bcprov-jdk18on" % "1.81",
      // "com.lihaoyi" % "ammonite" % "3.0.2" cross CrossVersion.full,
      "org.scalameta" %% "munit" % "1.1.1" % Test
    ) ++ jsonLib
  )
