package tv.camfire.media_server.serialization.jackson.module

import com.fasterxml.jackson.annotation.{JsonAutoDetect, JsonProperty}
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility

/**
 * User: jonathan
 * Date: 5/1/13
 * Time: 12:51 AM
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
abstract class SignalMixin(@JsonProperty("type") `type`: String,
                           @JsonProperty("data") data: String) {
}
