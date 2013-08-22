package tv.camfire.media_server.atmosphere

import org.json4s._
import org.scalatra.atmosphere.{TextMessage, JsonMessage, OutboundMessage}

/**
 * User: jonathan
 * Date: 7/31/13
 * Time: 8:49 PM
 */
trait GlobalAtmosphereSupport extends jackson.JsonMethods {
  import org.scalatra.util.RicherString._
  implicit def json2JsonMessage(json: JValue): OutboundMessage = JsonMessage(json)

  implicit def string2Outbound(text: String): OutboundMessage = text.blankOption map { txt =>
    if (txt.startsWith("{") || txt.startsWith("["))
      parseOpt(txt) map JsonMessage.apply getOrElse TextMessage(txt)
    else
      TextMessage(txt)
  } getOrElse TextMessage("")

}
