package tv.camfire.media_server.config

import com.softwaremill.macwire.Macwire
import akka.actor.{ActorRef, Props, ActorSystem}
import tv.camfire.media_server.server.MediaService
import tv.camfire.media_server.signal.SignalHelper
import tv.camfire.media_server.bridge.{StandardBroadcasterFactoryBridge}
import tv.camfire.media_server.factory.CamfirePeerConnectionFactory
import org.webrtc.PeerConnection
import tv.camfire.media_server.webrtc.WebRtcHelper
import tv.camfire.media_server.serialization.SerializationHelper
import tv.camfire.media_server.config.factory.{SessionCompanionFactoryFactory, PeerConnectionObserverFactoryFactory}

/**
 * User: jonathan
 * Date: 7/22/13
 * Time: 7:05 PM
 */
trait LogicModule extends Macwire {
  /**
   * Atmosphere
   */
  lazy val broadcasterFactoryBridge = wire[StandardBroadcasterFactoryBridge]

  /**
   * Utilities & Configuration
   */
  lazy val properties = new Properties {}
  lazy val serializationHelper = wire[SerializationHelper]
  lazy val signalHelper: SignalHelper = wire[SignalHelper]

  /**
   * WebRTC
   */
  lazy val iceServers = new java.util.ArrayList[PeerConnection.IceServer]()
  iceServers.add(new PeerConnection.IceServer(properties.iceUri, properties.iceUsername, properties.icePassword))
  lazy val peerConnectionObserverFactoryFactory = wire[PeerConnectionObserverFactoryFactory]
  lazy val webRtcHelper: WebRtcHelper = wire[WebRtcHelper]
  lazy val camfirePeerConnectionFactory = wire[CamfirePeerConnectionFactory]

  /**
   * Redis
   */

  /**
   * Server
   */
  lazy implicit val actorSystem = ActorSystem()
  lazy val sessionCompanionFactoryFactory: SessionCompanionFactoryFactory = wire[SessionCompanionFactoryFactory]
  lazy val mediaServiceProps: Props = Props(new MediaService(sessionCompanionFactoryFactory))
//  lazy val mediaServiceProps: Props = Props(classOf[MediaService], sessionCompanionFactoryFactory) //(sessionCompanionFactoryFactory))
  lazy val mediaService: ActorRef = actorSystem.actorOf(mediaServiceProps, properties.mediaServiceName)
}
