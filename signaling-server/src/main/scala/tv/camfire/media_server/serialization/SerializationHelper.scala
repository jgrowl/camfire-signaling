package tv.camfire.media_server.serialization

import org.slf4j.LoggerFactory
import tv.camfire.media_server.signal.Signal
import tv.camfire.media_server.serialization.jackson.CamfireSerializationSupport
import org.json4s.jackson.JsonMethods
import org.webrtc.SessionDescription

/**
 * User: jonathan
 * Date: 5/16/13
 * Time: 10:12 PM
 */
class SerializationHelper extends JsonMethods with CamfireSerializationSupport {
  protected val _logger = LoggerFactory.getLogger(getClass)

  def writeValueAsString(value: Any): String = {
    mapper.writeValueAsString(value)
  }

  def createSignal(signalName: String, value: Any): Signal = {
    val data = writeValueAsString(value)
    new Signal(signalName, data)
  }

  def createSignalString(signalName: String, value: Any): String = {
    writeValueAsString(createSignal(signalName, value))
  }

  def createOfferString(offer: SessionDescription): String = {
    createSignalString("offer", writeValueAsString(offer))
  }
}
