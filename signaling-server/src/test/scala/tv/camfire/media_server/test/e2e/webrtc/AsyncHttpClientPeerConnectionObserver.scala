package tv.camfire.media_server.test.e2e.webrtc

import org.webrtc.{IceCandidate, MediaStream, PeerConnection}
import org.webrtc.PeerConnection.{SignalingState, IceConnectionState, IceGatheringState}
import com.ning.http.client.websocket.WebSocket

/**
 * User: jonathan
 * Date: 7/31/13
 * Time: 1:48 AM
 */
class AsyncHttpClientPeerConnectionObserver(webSocket: WebSocket) extends PeerConnection.Observer {
  def onSignalingChange(p1: SignalingState) {}

  def onIceConnectionChange(p1: IceConnectionState) {}

  def onIceGatheringChange(p1: IceGatheringState) {}

  def onIceCandidate(p1: IceCandidate) {}

  def onError() {}

  def onAddStream(p1: MediaStream) {}

  def onRemoveStream(p1: MediaStream) {}
}
