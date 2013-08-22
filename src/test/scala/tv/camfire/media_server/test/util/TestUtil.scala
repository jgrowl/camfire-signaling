package tv.camfire.media_server.test.util

import java.util.UUID
import scala.util.Random
import org.webrtc.SessionDescription
import tv.camfire.media_server.serialization.SerializationHelper
import tv.camfire.media_server.signal.Signal
import tv.camfire.media_server.webrtc.WebRtcHelper
import tv.camfire.media_server.test.util.TestUtil._

/**
 * The trait is to be used mainly in acceptance tests. These functions require some major dependencies that need to
 * be set up in bootstrap phase.
 *
 * User: jonathan
 * Date: 7/11/13
 * Time: 9:03 PM
 */
trait TestUtil {
  val serializationHelper: SerializationHelper
  val webRtcHelper: WebRtcHelper

  def getAnonymousUnrecognizedSignal: Signal = serializationHelper.createSignal(getUnrecognizedSignalType, getAnonymousSignalData)

  def getAnonymousUnrecognizedSignalString: String = serializationHelper.writeValueAsString(getAnonymousUnrecognizedSignal)
}

/**
 * This companion object is mainly for unit test helper functions. Anything that does not have any major dependencies
 * can go here.
 */
object TestUtil {
  private val randomSeed = 2304934
  private val random = new Random(randomSeed)

  def getAnonymousUserId: Int = {
    random.nextInt()
  }

  def getAnonymousString: String = {
    UUID.randomUUID().toString
  }

  def getAnonymousSessionId: String = getAnonymousString

  def getAnonymousSessionDescriptionString: String = getAnonymousString

  def getAnonymousSignalData: String = getAnonymousString

  def getAnonymousNonJsonString: String = getAnonymousString

  def getUnrecognizedSignalType: String = getAnonymousString

  def getAnonymousOffer: SessionDescription = {
    new SessionDescription(SessionDescription.Type.OFFER, getAnonymousSessionDescriptionString)
  }

  def getAnonymousSessionJson: String = {
    "{\"_csrf_token\":\"tOH1PtmqN4hjJ4TtiaZu4eM6GQtofMG5oy7DqjCqdA8=\"}"
  }
}
