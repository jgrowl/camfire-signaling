package tv.camfire.media_server.test.util.e2e.connection

import com.ning.http.client.websocket.{WebSocket, WebSocketUpgradeHandler}
import com.ning.http.client.{AsyncHttpClientConfig, Cookie, AsyncHttpClient}
import tv.camfire.media_server.test.util.e2e.listener.ConnectivityListener
import tv.camfire.media_server.config.Properties
import java.util.concurrent.TimeUnit

/**
 * User: jonathan
 * Date: 7/30/13
 * Time: 12:29 AM
 */
class SimpleConnection(sessionId: String = null,
                       val webSocketTextListener: ConnectivityListener = new ConnectivityListener())
                      (implicit _jettyPort: Int, properties: Properties) {
  val host = "localhost"
  val resource = "/signal"
  val c = new AsyncHttpClient()
  val url = "ws://%s:%s%s".format(host, _jettyPort, resource)
  val receivedMessages = webSocketTextListener.receivedMessages

  val builder = c.prepareGet(url)
  if (sessionId != null) {
    builder.addCookie(getCookie(sessionId))
  }

  val websocket = builder
    .execute(new WebSocketUpgradeHandler.Builder().addWebSocketListener(webSocketTextListener).build()).get()

  def couldConnect: Boolean = {
    webSocketTextListener.wasAbleToConnect
  }

  def receivedAMessage: Boolean = {
    webSocketTextListener.receivedAMessage
  }

  def getCookie(sessionId: String): Cookie = {
    val domain = "localhost"
    val name = properties.sessionCookie
    val value = sessionId
    val path = "/"
    val maxAge = -1
    val secure = false
    val cookie = new Cookie(domain, name, value, path, maxAge, secure)
    cookie
  }
}


