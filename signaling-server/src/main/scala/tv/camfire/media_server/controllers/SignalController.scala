package tv.camfire.media_server.controllers

import _root_.akka.actor.{ActorSystem, ActorRef}
import _root_.akka.util.Timeout
import org.scalatra._
import scalate.ScalateSupport
import scala.concurrent.{Await, ExecutionContext}
import org.webrtc.{IceCandidate, SessionDescription}
import org.slf4j.LoggerFactory
import org.scalatra.atmosphere._
import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.json4s._
import tv.camfire.media_server.server._
import tv.camfire.media_server.server.ClientAnswer
import tv.camfire.media_server.server.ClientIceCandidate
import org.scalatra.atmosphere.Disconnected
import org.scalatra.atmosphere.Error
import tv.camfire.media_server.server.SubscribeToMediaStream
import org.scalatra.atmosphere.JsonMessage
import org.scalatra.atmosphere.TextMessage
import tv.camfire.media_server.server.ClientOffer
import scala.Some
import tv.camfire.media_server.ErrorMessages._
import tv.camfire.media_server.signal.Signal
import com.fasterxml.jackson.databind.JsonMappingException
import tv.camfire.media_server.serialization.jackson.CamfireSerializationSupport
import java.util.concurrent.TimeUnit

/**
 * Main end user access point.
 *
 * Note: Generally, the user can attempt to send anything they want. If their request does not meet expectations we will
 * not notify them that anything went wrong. We will just log it as a warning.
 *
 * @param actorSystem
 * @param mediaService
 */
class SignalController(actorSystem: ActorSystem, mediaService: ActorRef) extends ScalatraServlet
with ScalateSupport
with JValueResult
with JacksonJsonSupport
with SessionSupport
with AtmosphereSupport
with CorsSupport
with CamfireSerializationSupport
//with FutureSupport {
  {
  scalatraBroadcasterClass = classOf[RedisScalatraBroadcaster]

  import _root_.akka.pattern.ask

  protected val _logger = LoggerFactory.getLogger(getClass)

  protected implicit def executor: ExecutionContext = actorSystem.dispatcher

  implicit protected val jsonFormats: Formats = DefaultFormats
  implicit val askTimeout = Timeout(10000)

  // TODO: Restrict this
  options("/*") {
    response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"))
  }

  atmosphere("/signal") {
    val sessionId = request.getSession.id
    new AtmosphereClient {
      def receive = {
        case Connected =>
          _logger.info("New user connected.")
          val future = mediaService ? RegisterResourceUuid(sessionId, uuid)
          val registered = Await.result(future, new Timeout(5, TimeUnit.SECONDS).duration).asInstanceOf[Boolean]
          if (registered) {
            _logger.debug("Successfully registered resource [%s] to session [%s]".format(uuid, sessionId))
          } else {
            _logger.warn("Failed to register resource [%s] to session [%s]".format(uuid, sessionId))
          }

        case Disconnected(disconnector, Some(error)) =>
          _logger.info("User has disconnected.")
        case Error(Some(error)) =>
          _logger.error("An unexpected error has occurred: %s".format(error.getMessage))
        case TextMessage(text) =>
          _logger.warn("%s: %s".format("Received an unexpected text message.", CLIENT_ERROR))
        case JsonMessage(json) =>
          _logger.debug("Received JSON from client: %s".format(render(json)))
          try {
            implicit val requestUuid = uuid
            val signal = json.extract[Signal]
            handleSignal(signal)
          } catch {
            case jme: JsonMappingException =>
              _logger.warn("%s %s".format(FAILED_SIGNAL_PARSE, CLIENT_ERROR))
          }
      }

      private def handleSignal(signal: Signal)(implicit askTimeout: Timeout, requestId: String) {
        val `type` = signal.`type`
        val data = signal.data
        _logger.debug("Received [%s] signal with the following data: [%s]".format(`type`, data))
        try {
          `type` match {
            case "offer" =>
              val remoteSessionDescription = mapper.readValue(data, classOf[SessionDescription])
//              new AsyncResult {
                val is = mediaService ? ClientOffer(sessionId, remoteSessionDescription)
                is.onSuccess({
                  case answer: Signal =>
                    send(mapper.writeValueAsString(answer))
                })
                is.onFailure({
                  case _ =>
                    println("failed ")
                })
//              }
            case "answer" =>
              val remoteSessionDescription = mapper.readValue(data, classOf[SessionDescription])
              mediaService ! ClientAnswer(sessionId, remoteSessionDescription)
            case "candidate" =>
              val iceCandidate = mapper.readValue(data, classOf[IceCandidate])
              mediaService ! ClientIceCandidate(sessionId, iceCandidate)
            case "subscribe" =>
              mediaService ! SubscribeToMediaStream(sessionId, data)
            case "session-id-hash" =>
//              new AsyncResult {
                val is = mediaService ? GetSessionIdHash(sessionId)
                is.onSuccess({
                  case sessionIdHash: String =>
                    send(sessionIdHash)
                })
//              }
            case "available-stream" =>
//              new AsyncResult {
                val is = mediaService ? GetAvailableStreams()
                is.onSuccess({
                  // TODO: Fix this, comes back as array?
                  case sessionIdHash: String =>
                    send(sessionIdHash)
                })
//              }
            case _ =>
              _logger.warn("%s : [%s] %s'".format(UNKNOWN_SIGNAL, `type`, CLIENT_ERROR))
          }
        } catch {
          case jme: JsonMappingException =>
            _logger.warn("%s %s".format(FAILED_DATA_PARSE, CLIENT_ERROR))
        }
      }
    }
  }
}
