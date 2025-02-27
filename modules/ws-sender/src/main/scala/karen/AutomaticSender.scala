package karen

import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.*
import com.comcast.ip4s.Literals.port
import com.comcast.ip4s.{ Port, port }
import com.typesafe.scalalogging.StrictLogging
import echobot.WebSocketServer
import org.http4s.Uri
import org.http4s.client.websocket.{ WSClient, WSConnection, WSFrame, WSRequest }
import org.http4s.implicits.uri
import org.http4s.jdkhttpclient.JdkWSClient

import java.time.Instant
import scala.concurrent.duration.{ FiniteDuration, given }
object AutomaticSender extends IOApp with StrictLogging {
  def startShouting(target: Option[Uri]): IO[Unit] = {
    val request    = WSRequest(target.getOrElse(defaultUri))
    logger.info(s"Will send to $request")
    val connection = for {
      wsClient   <- JdkWSClient.simple[IO]
      connection <- wsClient.connectHighLevel(request)

    } yield connection

    connection.use { con =>
      val cmd = IO(logger.info(s"About to Send")) *>
        IO.randomUUID.flatMap(id => con.sendText(s"Jello ${id}")) *>
        IO.sleep(4.seconds)

      val sender             = cmd.replicateA(5) *> IO(logger.info("Done Sending"))
      val receiver: IO[Unit] = con
        .receiveStream
        .collect {
          case WSFrame.Text(text, last) =>
            logger.info(s"Received: $text: $last")
            text
        }
        .debug(d => d, s => logger.info(s"Piped $s"))
        .compile
        .drain

      sender &> receiver
    }
  }

  lazy val defaultUri                                = uri"ws://localhost:8080/ws"
  override def run(args: List[String]): IO[ExitCode] = {
    val serverUri        = uri"ws://localhost:8080/ws"
    val serverPort: Port = serverUri.port.flatMap(Port.fromInt).getOrElse(port"8080")

    val client = startShouting(serverUri.some)
    val server = WebSocketServer.run(serverPort)
    IO.both(server, client).as(ExitCode.Success)

  }
}
