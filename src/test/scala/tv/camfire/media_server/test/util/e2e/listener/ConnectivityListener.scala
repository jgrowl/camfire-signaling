package tv.camfire.media_server.test.util.e2e.listener

import com.ning.http.client.websocket.WebSocket
import java.util
import java.util.concurrent.LinkedBlockingQueue
import org.json4s.jackson
import us.spectr.webrtc.serialization.jackson.WebrtcSerializationSupport
import tv.camfire.media_server.signal.Signal
import tv.camfire.media_server.serialization.jackson.CamfireSerializationSupport

/**
 * User: jonathan
 * Date: 7/30/13
 * Time: 12:26 AM
 */
class ConnectivityListener() extends DefaultWebSocketTextListener
with jackson.JsonMethods with CamfireSerializationSupport {
  var wasAbleToConnect = false
  var receivedAMessage = false
  val receivedMessages: util.Queue[String] = new LinkedBlockingQueue[String]()

  override def onOpen(websocket: WebSocket) {
    wasAbleToConnect = true
    super.onOpen(websocket)
  }

  override def onMessage(message: String) {
    receivedAMessage = true
    receivedMessages.add(message)
    super.onMessage(message)
  }

  def receivedAtLeast(`type`: String, count: Int): Boolean = {
    receivedCountSignalType(`type`) >= count
  }

  def receivedExactly(`type`: String, count: Int): Boolean = {
    receivedCountSignalType(`type`) == count
  }

  def receivedCountSignalType(`type`: String): Int = {
    var count = 0
    import scala.collection.JavaConversions._
    receivedMessages.foreach({
      message =>
        val signal = mapper.readValue(message, classOf[Signal])
        if (signal.`type`.equals(`type`)) {
          count += 1
        }
    })

    count
  }

}
