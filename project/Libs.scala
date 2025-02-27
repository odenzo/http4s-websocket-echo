import sbt.*

//noinspection TypeAnnotation
object Libs {
  object V {
    val circe        = "0.14.10"
    val circeOptics  = "0.15.0"
    val cats         = "2.13.0"
    val catsEffect   = "3.5.7"
    val monocle      = "3.3.0"
    val pprint       = "0.9.0"
    val munit        = "1.1.0"
    val munitCheck   = "1.0.0"
    val munitCats    = "2.0.0"
    val logback      = "1.5.16"
    val http4s       = "0.23.30"
    val chimney      = "1.7.3"
    val fs2          = "3.11.0"
    val fs2Data      = "1.11.2"
    val scalaLogging = "3.9.5"
  }

  val chimney = Seq(
    "io.scalaland" %% "chimney" % V.chimney
  )

  val fs2 = Seq(
    "co.fs2" %% "fs2-core" % V.fs2,
    "co.fs2" %% "fs2-io" % V.fs2,
    "org.gnieh" %% "fs2-data-json" % V.fs2Data,
    "org.gnieh" %% "fs2-data-json-circe" % V.fs2Data,
  )

  val http4s = Seq(
    "org.http4s" %% "http4s-ember-client" % V.http4s,
    "org.http4s" %% "http4s-ember-server" % V.http4s,
    "org.http4s" %% "http4s-dsl" % V.http4s,
    "org.http4s" %% "http4s-circe" % V.http4s,
    "org.http4s" %% "http4s-netty-client" % "0.5.22",
    "org.http4s" %% "http4s-jdk-http-client" % "0.10.0",
  )
  val munit  = Seq(
    "org.scalameta" %% "munit" % V.munit % Test,
    "org.scalameta" %% "munit-scalacheck" % V.munit % Test,
    "org.typelevel" %% "munit-cats-effect" % V.munitCats % Test,
  )

  val pprint = Seq("com.lihaoyi" %% "pprint" % V.pprint)

  /** JSON Libs == Circe and Associated Support Libs */
  val circe =
    Seq(
      "io.circe" %% "circe-core" % V.circe,
      "io.circe" %% "circe-generic" % V.circe,
      "io.circe" %% "circe-parser" % V.circe,
      // "io.circe" %% "circe-scodec" % V.circe,
      "io.circe" %% "circe-pointer" % V.circe,
      "io.circe" %% "circe-pointer-literal" % V.circe,
      "io.circe" %% "circe-testing" % V.circe % Test,
      "io.circe" %% "circe-optics" % V.circeOptics,
      "io.circe" %% "circe-literal" % V.circe,
      // "io.circe" %% "circe-derivation" % V.circeVersion
      // "io.circe" %% "circe-refined"    % V.circeVersion,
      // "io.circe" %% "circe-extras"     % V.circeVersion,
      //   "io.circe" %% "circe-generic-extras" % circeGenericExtraVersion,
      //   "io.circe" %% "circe-yaml"    % circeVersion, // Version 0.14 only
    )

  val monocle = Seq(
    "dev.optics" %% "monocle-core" % V.monocle,
    "dev.optics" %% "monocle-macro" % V.monocle,
    "dev.optics" %% "monocle-law" % V.monocle % Test,
  )

  /** Simple Lightbend Logging */
  val logging = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % V.scalaLogging,
    "ch.qos.logback" % "logback-classic" % "1.5.17"
  )
  val cats    = Seq(
    "org.typelevel" %% "cats-core" % V.cats,
    "org.typelevel" %% "cats-effect" % V.catsEffect,
    "org.typelevel" %% "cats-effect-testkit" % V.catsEffect % Test,
  )

  val stdlibs = cats ++ monocle ++ munit ++ pprint ++ circe ++ chimney ++ logging

}
