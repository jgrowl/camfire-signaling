package tv.camfire.media_server.serialization.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import us.spectr.webrtc.serialization.jackson.WebrtcSerializationSupport

/**
 * User: jonathan
 * Date: 7/27/13
 * Time: 3:48 PM
 */
trait CamfireSerializationSupport extends WebrtcSerializationSupport {
  def mapper: ObjectMapper

  mapper.registerModule(new CamfireModule)
}
