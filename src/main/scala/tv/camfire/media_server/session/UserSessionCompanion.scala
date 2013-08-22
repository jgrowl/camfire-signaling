package tv.camfire.media_server.session

import tv.camfire.media_server.signal.{SignalHelper, UserStreamInfo}
import tv.camfire.media_server.webrtc.WebRtcHelper
import tv.camfire.media_server.serialization.SerializationHelper
import tv.camfire.media_server.factory.CamfirePeerConnectionFactory


/**
 * User: jonathan
 * Date: 6/10/13
 * Time: 8:20 PM
 */
class UserSessionCompanion(webRtcHelper: WebRtcHelper,
                           serializationHelper: SerializationHelper,
                           signalHelper: SignalHelper,
                           camfirePeerConnectionFactory: CamfirePeerConnectionFactory,
                           _sessionId: String, userId: Int)
  extends SessionCompanion(webRtcHelper: WebRtcHelper,
    serializationHelper: SerializationHelper,
    signalHelper: SignalHelper,
    camfirePeerConnectionFactory: CamfirePeerConnectionFactory,
    _sessionId) {
  //  override def receive: PartialFunction[Any, Unit] = _receive orElse super.receive
  //
  //  def _receive: PartialFunction[Any, Unit] = {
  //    case msg@RegisterMediaStream(sessionId, mediaStream) =>
  //      registerMediaStream(mediaStream, newUserStreamInfo())
  //  }

  override def newStreamInfo(): UserStreamInfo = {
    new UserStreamInfo(_sessionId, sessionIdHash, userId)
  }
}
