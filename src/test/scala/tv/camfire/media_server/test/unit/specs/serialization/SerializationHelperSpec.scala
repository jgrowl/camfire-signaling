package tv.camfire.media_server.test.unit.specs.serialization

import org.specs2.mutable.Specification
import tv.camfire.media_server.signal.Signal
import tv.camfire.media_server.test.util.TestUtil._
import tv.camfire.media_server.serialization.SerializationHelper

/**
 * User: jonathan
 * Date: 5/1/13
 * Time: 12:04 AM
 */
class SerializationHelperSpec extends Specification {

  val serializationHelper = new SerializationHelper

  "A SerializationHelper" should {
    "be able to (de)serialize signals" in {
      val `type` = getAnonymousString
      val data = getAnonymousString
      val signal = new Signal(`type`, data)
      val serializedSignal = serializationHelper.writeValueAsString(signal)

      val reconstructedSignal = serializationHelper.mapper.readValue(serializedSignal, classOf[Signal])
      `type` must_== reconstructedSignal.`type`
      data must_== data
    }
  }


//
////    val test = SerializationHelper.createSignalString("new-stream", new NewUserStreamInfo("1111", 1))
//  }
//
//  @Test
//  def everythingGetsSerializedExceptSessionIdTest() {
//    val sessionId = "sessionId"
//    val sessionIdHash = BCryptUtils.hashAndSalt(sessionId)
//    val userStreamInfoString = mapper.writeValueAsString(new UserStreamInfo(sessionId, sessionIdHash, 1))
//    println(userStreamInfoString)
//    val userStreamInfo = mapper.readValue(userStreamInfoString, classOf[UserStreamInfo])
//    Assert.assertEquals(1, userStreamInfo.userId)
//    // This is very important. We never want to send users' session ids to other people.
//    Assert.assertEquals(null, userStreamInfo.sessionId)
//  }

}
