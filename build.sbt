val scala3Version = "3.6.4"


lazy val jsonLib = {
  val circeVersion = "0.14.1"
  Seq("io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser").map(_ % circeVersion)
}

lazy val root = project
  .in(file("."))
  .settings(
    name := "devops",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "com.hierynomus" % "sshj" % "0.39.0",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
      "ch.qos.logback" % "logback-classic" % "1.5.18",
      "io.circe" %% "circe-config" % "0.10.1",
      "com.github.pathikrit" %% "better-files" % "3.9.2",
      "org.scalameta" %% "munit" % "1.1.0" % Test
    ) ++ jsonLib
  )
