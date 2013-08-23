package tv.camfire.media_server.server

import akka.actor.{ActorRef, Actor}
import scala.Some
import org.webrtc.{MediaStream, IceCandidate, SessionDescription}
import org.slf4j.{Logger, LoggerFactory}
import tv.camfire.media_server.util.BCryptMap
import tv.camfire.media_server.ErrorMessages._


/**
 * User: jonathan
 * Date: 5/2/13
 * Time: 8:06 PM
 */

sealed trait SessionCompanionEvent

case class ClientOffer(sessionId: String, remoteDescription: SessionDescription) extends SessionCompanionEvent {}

case class ClientAnswer(sessionId: String, remoteDescription: SessionDescription) extends SessionCompanionEvent

case class ClientIceCandidate(sessionId: String, iceCandidate: IceCandidate) extends SessionCompanionEvent

case class SubscribeToMediaStream(userId: String, targetSessionIdHash: String) extends SessionCompanionEvent

case class GetSessionIdHash(sessionId: String) extends SessionCompanionEvent

case class GetAvailableStreams() extends SessionCompanionEvent

case class RegisterMediaStream(sessionId: String, mediaStream: MediaStream) extends SessionCompanionEvent

case class UnRegisterAllMediaStreams(sessionId: String) extends SessionCompanionEvent

case class GetMediaStream() extends SessionCompanionEvent

case class TellActorMediaStream(actorRef: ActorRef) extends SessionCompanionEvent

case class ReceiveActorMediaStream(mediaStream: MediaStream) extends SessionCompanionEvent


trait MediaManager extends Actor {
  private val log: Logger = LoggerFactory.getLogger(getClass)
  val sessions: BCryptMap[ActorRef]

  protected def mediaManagement: Receive = {
    // TODO: Find a way to make this more generic
    case msg@ClientOffer(sessionId, remoteSessionDescription) =>
      log.debug("Received ClientOffer from [%s]".format(sessionId))
      forwardToSession(sessionId, msg)
    case msg@ClientAnswer(sessionId, remoteSessionDescription) =>
      forwardToSession(sessionId, msg)
    case msg@ClientIceCandidate(sessionId, iceCandidate) =>
      forwardToSession(sessionId, msg)
    case msg@SubscribeToMediaStream(sessionId, targetSessionIdHash) =>
      val requestingActor = sessions.get(sessionId)
      val targetActor = sessions.get(targetSessionIdHash)
      targetActor ! TellActorMediaStream(requestingActor)

      // TODO: The Following is broken, clean up
//      if (requestingActor.isDefined) {
//        sendToSession(sessionId, TellActorMediaStream(requestingActor.get))
//      } else {
//        log.warn(NO_SESSION_COMPANION(sessionId, msg))
//      }
    case msg@RegisterMediaStream(sessionId, mediaStream) =>
      forwardToSession(sessionId, msg)
    case msg@UnRegisterAllMediaStreams(sessionId) =>
      forwardToSession(sessionId, msg)
    case msg@GetAvailableStreams() =>
    //      val streams = redisClient.keys("stream:*")
    //      val all = StreamModel.findAll()

  }

  def sendToSession(sessionId: String, msg: SessionCompanionEvent) {
    val session = getSession(sessionId)
    println("ARRGG:")
    println(session)
    if (session.isDefined) {
      session.get ! msg
    } else {
      log.warn(NO_SESSION_COMPANION(sessionId, msg))
    }
  }

  def forwardToSession(sessionId: String, msg: AnyRef) {
//    log.trace("SIZE: %s".format(sessions.size()))
//    import scala.collection.JavaConversions._
//    sessions.descendingMap().foreach(kv => {
//      println("%s : %s".format(kv._1, kv._2.path.toString))
//    })

    val session = getSession(sessionId)
    if (session.isDefined) {
      log.debug("Forwarding message to session companion...")
      session.get forward msg
    } else {
      log.warn(NO_SESSION_COMPANION(sessionId, msg))
    }
  }

  private def getSession(sessionId: String): Option[ActorRef] = {
    if (sessions.containsKey(sessionId))
      Some(sessions.get(sessionId))
    else {
      None
    }
  }

}
