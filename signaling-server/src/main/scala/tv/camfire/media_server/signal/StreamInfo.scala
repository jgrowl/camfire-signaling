package tv.camfire.media_server.signal

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonAutoDetect}
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import tv.camfire.media_server.util.BCryptUtils

/**
 * User: jonathan
 * Date: 6/11/13
 * Time: 4:20 PM
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class StreamInfo(@transient val sessionId: String, val sessionIdHash: String) {
//  val sessionIdHash = BCryptUtils.hashAndSalt(sessionId)
}

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class UserStreamInfo(sessionId: String, sessionIdHash: String, val userId: Int) extends StreamInfo(sessionId, sessionIdHash) {
  def this() = this(null, null, -1)
}
