package tv.camfire.media_server.config.factory

import tv.camfire.media_server.signal.SignalHelper
import tv.camfire.media_server.serialization.SerializationHelper
import tv.camfire.media_server.factory.CamfirePeerConnectionFactory
import tv.camfire.media_server.webrtc.WebRtcHelper
import akka.actor.{Props, ActorRef, ActorContext}
import tv.camfire.media_server.session.{UserSessionCompanion, SessionCompanion}

/**
 * User: jonathan
 * Date: 7/23/13
 * Time: 3:10 AM
 */
class SessionCompanionFactoryFactory(signalHelper: SignalHelper,
                                     serializationHelper: SerializationHelper,
                                     camfirePeerConnectionFactory: CamfirePeerConnectionFactory,
                                     webRtcHelper: WebRtcHelper) {

  case class create(implicit context: ActorContext) {
    def createSessionCompanion(sessionId: String): ActorRef = {
      val props = Props(
        new SessionCompanion(
          webRtcHelper,
          serializationHelper,
          signalHelper,
          camfirePeerConnectionFactory,
          sessionId))
      context.actorOf(props, sessionId)
    }

    def createUserSessionCompanion(sessionId: String, userId: Int): ActorRef = {
      val props = Props(new UserSessionCompanion(webRtcHelper,
        serializationHelper,
        signalHelper,
        camfirePeerConnectionFactory,
        sessionId, userId))
      context.actorOf(props, sessionId)
    }
  }

}
