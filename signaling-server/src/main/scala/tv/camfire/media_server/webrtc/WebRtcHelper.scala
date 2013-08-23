package tv.camfire.media_server.webrtc

import org.webrtc._
import tv.camfire.SdpObserverLatch
import java.util
import org.slf4j.{LoggerFactory, Logger}
import org.webrtc.MediaConstraints.KeyValuePair
import tv.camfire.media_server.Types._
import tv.camfire.media_server.config.factory.PeerConnectionObserverFactoryFactory

/**
 * User: jonathan
 * Date: 4/24/13
 * Time: 1:45 PM
 */
class WebRtcHelper(peerConnectionObserverFactoryFactory: PeerConnectionObserverFactoryFactory,
                   val factory: PeerConnectionFactory,
                   iceServers: util.ArrayList[PeerConnection.IceServer]) {
  private val peerConnectionObserverFactory: PeerConnectionObserverFactory = peerConnectionObserverFactoryFactory.create()

  val logger: Logger = LoggerFactory.getLogger(classOf[WebRtcHelper])

  def createPeerConnection(sessionId: String)(implicit resourceUuid: String): PeerConnection = {
    val observer = peerConnectionObserverFactory.create(sessionId)
    factory.createPeerConnection(iceServers, createConstraints, observer)
  }

  def createPeerConnection(sessionId: String, observer: PeerConnection.Observer) = {
    factory.createPeerConnection(iceServers, createConstraints, observer)
  }

  def createPeerConnection(constraints: MediaConstraints, observer: PeerConnectionObserver): PeerConnection = {
    factory.createPeerConnection(iceServers, createConstraints, observer)
  }

  def setRemoteDescription(peerConnection: PeerConnection, remoteSessionDescription: SessionDescription) {
    val sdpLatch = new SdpObserverLatch
    logger.debug("Attempting to set remote description...")
    peerConnection.setRemoteDescription(sdpLatch, remoteSessionDescription)
    if (sdpLatch.await()) {
      logger.info("Successfully set remote description.")
    } else {
      logger.error("There was a problem waiting to set remote description!")
    }
  }

  def establishPeerConnection(peerConnection: PeerConnection, remoteSessionDescription: SessionDescription) {
    setRemoteDescription(peerConnection, remoteSessionDescription)

    var sdpLatch = new SdpObserverLatch()
    peerConnection.createAnswer(sdpLatch, createConstraints)
    if (sdpLatch.await()) {
      logger.info("Successfully created answer.")
      val answer = sdpLatch.getSdp
      sdpLatch = new SdpObserverLatch
      peerConnection.setLocalDescription(sdpLatch, answer)
      if (sdpLatch.await()) {
        logger.info("Successfully set local description.")
      } else {
        logger.error("There was a problem setting the local description!")
      }
    } else {
      logger.error("Failed to create answer!")
    }
  }

  def makeOffer(peerConnection: PeerConnection): PeerConnection = {
    logger.debug("Attempting to create offer...")
    val sdpLatch = new SdpObserverLatch
    peerConnection.createOffer(sdpLatch, createConstraints)
    if (sdpLatch.await()) {
      logger.debug("Successfully created offer.")
      val localSessionDescription = sdpLatch.getSdp
      logger.debug("Attempting to set local description...")
      peerConnection.setLocalDescription(sdpLatch, localSessionDescription)
      if (sdpLatch.await()) {
        logger.debug("Successfully set local description.")
      } else {
        logger.error("Failed to set local description!")
      }
    } else {
      logger.error("Failed to create offer!")
    }

    peerConnection
  }

  def createConstraints: MediaConstraints = {
    val constraints = new MediaConstraints()
    constraints.mandatory.add(new KeyValuePair("OfferToReceiveVideo", "true"))
    constraints.mandatory.add(new KeyValuePair("OfferToReceiveAudio", "true"))
    constraints
  }

}



