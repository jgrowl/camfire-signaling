package tv.camfire.media_server.config.factory

import akka.actor.ActorRef
import tv.camfire.media_server.signal.SignalHelper
import tv.camfire.media_server.webrtc.PeerConnectionObserver
import tv.camfire.media_server.serialization.SerializationHelper

/**
 * User: jonathan
 * Date: 7/23/13
 * Time: 3:10 AM
 */
class PeerConnectionObserverFactoryFactory(mediaService: ActorRef,
                                           serializationHelper: SerializationHelper,
                                           signalHelper: SignalHelper,
                                           sessionId: String) {

  case class create() {
    def create(sessionId: String)(implicit resourceUuid: String): PeerConnectionObserver = {
      new PeerConnectionObserver(mediaService, serializationHelper, signalHelper, sessionId)
    }
  }

}
