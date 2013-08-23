package tv.camfire.media_server.webrtc

import org.webrtc._
import org.webrtc.PeerConnection.{IceConnectionState, IceGatheringState, SignalingState}
import org.slf4j.LoggerFactory
import tv.camfire.media_server.server.{UnRegisterAllMediaStreams, RegisterMediaStream}
import tv.camfire.media_server.signal.SignalHelper
import tv.camfire.media_server.serialization.SerializationHelper
import akka.actor.ActorRef
import org.scalatra.atmosphere.AtmosphereClient
import tv.camfire.media_server.atmosphere.{AtmosphereClientFilter, GlobalAtmosphereSupport}
import scala.concurrent.ExecutionContext

/**
 * User: jonathan
 * Date: 4/28/13
 * Time: 2:10 PM
 */
class PeerConnectionObserver(mediaService: ActorRef,
                             _serializationHelper: SerializationHelper,
                             _signalHelper: SignalHelper,
                             val sessionId: String)(implicit var _resourceUuid: String)
  extends PeerConnection.Observer
  with GlobalAtmosphereSupport
  with AtmosphereClientFilter {

  import ExecutionContext.Implicits.global

  val logger = LoggerFactory.getLogger(classOf[PeerConnectionObserver])

  def onSignalingChange(p1: SignalingState) {
    logger.debug("onSignalingChange called: [%s]...".format(p1.name()))
  }

  def onIceConnectionChange(p1: IceConnectionState) {
    logger.debug("onIceConnectionChange called: [%s]...".format(p1.name()))
    if (IceConnectionState.DISCONNECTED.equals(p1)) {
      logger.debug("Connection [%s] disconnected, attempting to un-register media stream...".format(sessionId))
      mediaService ! UnRegisterAllMediaStreams(sessionId)
    }
  }

  def onIceGatheringChange(p1: IceGatheringState) {
    logger.debug("onIceGatheringChange called: [%s]...".format(p1.name()))
  }

  def onIceCandidate(iceCandidate: IceCandidate) {
    logger.debug("onIceCandidate called...")
    logger.debug("Broadcasting server candidate: ")
    val candidate = _serializationHelper.createSignalString("candidate", iceCandidate)
    AtmosphereClient.broadcastAll(candidate, Me)
  }

  def onError() {
    logger.error("onError called...")
  }

  def onAddStream(mediaStream: MediaStream) {
    logger.debug("onAddStream called...")
    mediaService ! RegisterMediaStream(sessionId, mediaStream)
  }

  def onRemoveStream(p1: MediaStream) {
    logger.debug("onRemoveStream called...")
    logger.error("IS NOT IMPLEMENTED!")
  }

}
