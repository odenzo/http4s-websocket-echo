package echobot

import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.*
import com.comcast.ip4s.{ Port, port }
import com.typesafe.scalalogging.StrictLogging
import echobot.EchoAllMain.logger

object EchoAllMain extends IOApp with StrictLogging {
  override def run(args: List[String]): IO[ExitCode] = {
    logger.info(s"Running EchoAllMain with args: $args")
    val default    = port"8080"
    val port: Port = args.headOption.flatMap(Port.fromString).getOrElse(default)

    WebSocketServer.run(port).as(ExitCode.Success)
  }

}
