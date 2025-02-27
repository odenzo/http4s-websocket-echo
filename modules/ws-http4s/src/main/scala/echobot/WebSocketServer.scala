package echobot

import cats.effect.*
import cats.effect.syntax.all.*
import cats.*
import cats.data.*
import cats.syntax.all.*
import com.comcast.ip4s.Literals.port
import com.comcast.ip4s.{ Port, ipv4, port }
import com.typesafe.scalalogging.StrictLogging
import org.http4s.{ HttpRoutes, websocket, * }
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import org.http4s.server.websocket.*
import fs2.{ Pipe, *, given }
import org.http4s.dsl.io.*
import org.http4s.websocket.WebSocketFrame

import scala.concurrent.duration.given
class WebSocketServer {}

/** It seems like WebSocket Server and Client is Just Available on JDKClient */
object WebSocketServer extends StrictLogging {

  def routes(ws: WebSocketBuilder2[IO]): HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case rq@GET -> Root / "ws" =>
        val loop: Pipe[IO, WebSocketFrame, WebSocketFrame] =
          (in: fs2.Stream[IO, websocket.WebSocketFrame]) => in.debug(wsf => wsf.toString, s => logger.info(s"Inbound: $s "))


        logger.info(s"Matched a GET Request to /ws ${rq.toString}")
        ws.build(loop)
    }

  def buildServerResource(onPort: Port): Resource[IO, Server] = {
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(onPort)
      .withHttpWebSocketApp((ws: WebSocketBuilder2[IO]) => routes(ws).orNotFound)
      .build
  }

  def run(onPort: Port): IO[ExitCode] = {
    fs2
      .Stream
      .resource(
        buildServerResource(onPort) >> Resource.never // Run forever
      )
      .debug(v => v.toString, s => logger.info(s"Based Resource Block: $s"))
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
