package tv.camfire.media_server.test.util.e2e.listener

import com.ning.http.client.websocket.{WebSocket, WebSocketTextListener}

/**
 * Provides a WebSocket connection listener with a skeleton layout.
 *
 * User: jonathan
 * Date: 7/30/13
 * Time: 12:25 AM
 */
class DefaultWebSocketTextListener extends WebSocketTextListener {
  def onOpen(websocket: WebSocket) {}

  def onClose(websocket: WebSocket) {}

  def onError(t: Throwable) {}

  def onMessage(p1: String) {}

  def onFragment(p1: String, p2: Boolean) {}
}
