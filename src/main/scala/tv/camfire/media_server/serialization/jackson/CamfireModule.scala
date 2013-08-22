package tv.camfire.media_server.serialization.jackson

import com.fasterxml.jackson.databind.{BeanDescription, DeserializationConfig, JsonDeserializer}
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.Module.SetupContext
import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.deser.Deserializers.Base
import com.fasterxml.jackson.databind.module.SimpleModule
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription
import us.spectr.webrtc.serialization.jackson.mixin.SessionDescriptionMixin
import us.spectr.webrtc.serialization.jackson.mixin.IceCandidateMixin
import tv.camfire.media_server.signal.Signal
import tv.camfire.media_server.serialization.jackson.module.SignalMixin


/**
 * User: jonathan
 * Date: 5/1/13
 * Time: 12:56 AM
 */
class CamfireModule extends SimpleModule("CamfireModule", new Version(1, 0, 0, "", "tv.camfire", "media_server")) {
  override def setupModule(context: SetupContext) {
    super.setupModule(context)
    context.setMixInAnnotations(classOf[Signal], classOf[SignalMixin])
  }
}
