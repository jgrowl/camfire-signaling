package tv.camfire.media_server.session

import akka.actor.Actor
import tv.camfire.media_server.serialization.SerializationHelper
import tv.camfire.media_server.signal.{SignalHelper, StreamInfo}
import tv.camfire.media_server.webrtc.WebRtcHelper
import org.webrtc.{PeerConnection, MediaStream}
import java.util
import tv.camfire.media_server.factory.CamfirePeerConnectionFactory
import org.slf4j.LoggerFactory
import tv.camfire.media_server.util.BCryptUtils
import org.scalatra.atmosphere._
import tv.camfire.media_server.server._
import scala.concurrent.ExecutionContext
import tv.camfire.media_server.atmosphere.{AtmosphereClientFilter, GlobalAtmosphereSupport}
import tv.camfire.media_server.server.ReceiveActorMediaStream
import tv.camfire.media_server.server.TellActorMediaStream
import tv.camfire.media_server.server.ClientAnswer
import tv.camfire.media_server.server.ClientIceCandidate
import tv.camfire.media_server.server.GetSessionIdHash
import tv.camfire.media_server.server.UnRegisterAllMediaStreams
import tv.camfire.media_server.server.RegisterMediaStream
import tv.camfire.media_server.server.ClientOffer
import tv.camfire.media_server.model.SessionInfo


/**
 * User: jonathan
 * Date: 5/2/13
 * Time: 9:05 PM
 */

class SessionCompanion(webRtcHelper: WebRtcHelper,
                       serializationHelper: SerializationHelper,
                       signalHelper: SignalHelper,
                       camfirePeerConnectionFactory: CamfirePeerConnectionFactory,
                       _sessionId: String) extends Actor
with GlobalAtmosphereSupport with AtmosphereClientFilter {

  val log = LoggerFactory.getLogger(getClass)
  import ExecutionContext.Implicits.global

  protected implicit var _resourceUuid: String = null

  private val initializationTime = System.currentTimeMillis
  log.debug("Created session [%s] at [%s]".format(_sessionId, initializationTime))

  private var peerConnection = webRtcHelper.createPeerConnection(_sessionId)
//  private var peerConnection: PeerConnection = null
  private var _registeredMediaStream: MediaStream = null

  protected val sessionIdHash = BCryptUtils.hashAndSalt(_sessionId)
  createSessionInfo()

  def receive = {
    case msg@ClientOffer(sessionId, remoteSessionDescription) =>
      log.debug("Creating PeerConnection for [%s]".format(sessionId))
      // TODO: Validate this description
      webRtcHelper.establishPeerConnection(peerConnection, remoteSessionDescription)
      sender ! serializationHelper.createSignal("answer", peerConnection.getLocalDescription)
    case msg@ClientAnswer(sessionId, remoteSessionDescription) =>
      log.debug("Setting PeerConnection's remote description received in answer for [%s]".format(sessionId))
      webRtcHelper.setRemoteDescription(peerConnection, remoteSessionDescription)
    case msg@ClientIceCandidate(sessionId, iceCandidate) =>
      log.debug("Adding iceCandidate to [%s] session...".format(sessionId))
      peerConnection.addIceCandidate(iceCandidate)
    case msg@RegisterMediaStream(sessionId, mediaStream) =>
      registerMediaStream(mediaStream)
    case msg@UnRegisterAllMediaStreams(sessionId) =>
      unRegisterAllMediaStreams()
    case msg@TellActorMediaStream(actorRef) =>
      actorRef ! ReceiveActorMediaStream(_registeredMediaStream)
    case msg@ReceiveActorMediaStream(mediaStream: MediaStream) =>
      peerConnection.addStream(mediaStream, webRtcHelper.createConstraints)
      webRtcHelper.makeOffer(peerConnection)
      val offer = serializationHelper.createSignalString("offer", peerConnection.getLocalDescription)
      AtmosphereClient.broadcastAll(offer, Me)

    // TODO: I think I can delete this!
    // TODO: Broadcast a message that tells the client it was successful
    case msg@GetSessionIdHash(sessionId) =>
      sender ! sessionIdHash

    case msg@RegisterResourceUuid(sessionId, resourceUuid) =>
      _resourceUuid = resourceUuid
//      peerConnection = webRtcHelper.createPeerConnection(_sessionId)
      log.debug("Successfully registered resource [%s] to session [%s]".format(resourceUuid, sessionId))
      sender ! true

    case msg@_ =>
      log.error("Received an unknown message!")
      println(msg)
  }

  def registerMediaStream(mediaStream: MediaStream) {
    _registeredMediaStream = camfirePeerConnectionFactory.createDuplicatedMediaStream(mediaStream, sessionIdHash)
    val mediaStreamLabel = mediaStream.label()
    //    redisClient.set("stream:%s".format(_sessionId), true)
    //    StreamModel.create(_sessionId)

    log.debug("Registering MediaStream [%s] to [%s] session...".format(mediaStreamLabel, _sessionId))
    val newStream = serializationHelper.createSignalString("new-stream", newStreamInfo())
    // TODO: Send to everyone except broadcaster
    AtmosphereClient.broadcastAll(newStream, Others)
  }

  def unRegisterAllMediaStreams() {
    // TODO: Check to see if there are any streams first
//    log.debug("UnRegistering mediaStreams for [%s] session...".format(_sessionId))
//    val streamInfo = newStreamInfo()
//    val removeStreamSignal = serializationHelper.createSignalString("remove-stream", streamInfo)
    //    signalHelper.getSignalBroadcaster.broadcast(serializationHelper.createSignalString("server-remove-stream", removeStreamSignal))
    //    redisClient.del("stream:%s".format(_sessionId))
  }

  protected def newStreamInfo(): StreamInfo = {
    new StreamInfo(_sessionId, sessionIdHash)
  }

  protected def createSessionInfo() {
    val sessionInfo = new SessionInfo()
    sessionInfo.sessionId = _sessionId
  }

  protected def deleteSessionInfo() {
  }
}
