package karen

import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.*
import com.comcast.ip4s.Port
import com.typesafe.scalalogging.StrictLogging
import org.http4s.Uri
import org.http4s.client.websocket.{ WSClient, WSConnection, WSFrame, WSRequest }
import org.http4s.implicits.uri
import org.http4s.jdkhttpclient.JdkWSClient
import scala.concurrent.duration.given
object AutomaticSender extends IOApp with StrictLogging {
  val defaultUri                                   = uri"ws://localhost:8080/ws"
  def startShouting(target: Option[Uri]): IO[Unit] = {
    val request    = WSRequest(target.getOrElse(defaultUri))
    logger.info(s"Will send to $request")
    val connection = for {
      wsClient   <- JdkWSClient.simple[IO]
      connection <- wsClient.connectHighLevel(request)

    } yield connection

    connection.use { con =>
      val cmd = IO(logger.info(s"About to Send")) *>
        con.sendText("Jello") *>
        IO.sleep(4.seconds)

      val sender             = cmd.replicateA(5) *> IO(logger.info("Done Sending"))
      val receiver: IO[Unit] =
        con
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

  override def run(args: List[String]): IO[ExitCode] = {
    startShouting(None).as(ExitCode.Success) // ???
  }
}
