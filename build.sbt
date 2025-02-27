import BuildSettings.MyCompileOptions.scala3Options
import sbt.BasicCommands.alias

addCommandAlias("runSender", "run echobot.AutomaticSender")
ThisBuild / organization := "com.odenzo"
ThisBuild / name := "websocket-echo"
ThisBuild / scalaVersion := "3.6.3"
ThisBuild / semanticdbEnabled := true

Test / logBuffered := true
Test / parallelExecution := false

lazy val websocket =
  (project in file(".")).aggregate(echobot, karen).settings(publish / skip := true)

lazy val echobot = (project in file("modules/echobot")).settings(
  name := "echobot",
  scalacOptions := scala3Options,
  Compile / run / fork := true,
  libraryDependencies ++= Libs.stdlibs ++ Libs.http4s,
)

lazy val karen = (project in file("modules/ws-sender"))
  .dependsOn(echobot).settings(
    name := "karen",
    scalacOptions := scala3Options,
    libraryDependencies ++= Libs.stdlibs ++ Libs.http4s,
  )
