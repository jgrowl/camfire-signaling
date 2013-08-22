package tv.camfire.media_server.test.unit.server

import org.specs2.mutable.Specification
import tv.camfire.media_server.server._
import tv.camfire.media_server.test.util.TestUtil._
import org.specs2.specification.Scope
import tv.camfire.media_server.test.config.MockLogicModule
import akka.util.Timeout
import akka.testkit.TestActorRef
import scala.concurrent.duration._
import scala.concurrent.Await
import akka.pattern.ask
import scala.util.Success


/**
 * User: jonathan
 * Date: 7/28/13
 * Time: 3:39 PM
 */
class MediaServiceSpec extends Specification {
  import _root_.akka.pattern.ask
  implicit val askTimeout = Timeout(10)

  val module = new MockLogicModule
  val mediaService = module.mediaService
  "A SessionManager" should {
    "be able to add and remove session companions" in new mediaManagerScope {
      val actor = mediaService.underlyingActor
      var sessionId = getAnonymousSessionId
      mediaService ! AddSession(sessionId)
      actor.sessions.size() should beEqualTo(1)

      mediaService ! RemoveSession(sessionId)
      actor.sessions.size() should beEqualTo(0)

      sessionId = getAnonymousSessionId
      mediaService ! AddSessionForUser(sessionId, getAnonymousUserId)
      actor.sessions.size() should beEqualTo(1)
    }
  }

  "A MediaManager" should {
    "blah.." in new mediaManagerScope {

      val future = mediaService ? ClientOffer(getAnonymousSessionId, getAnonymousOffer)
//      val Success(result: String) = future.value.get
//      println(future.value.get)


//      val actorRef = TestActorRef[MediaService]
//      val actor = actorRef.underlyingActor
//      var sessionId = getAnonymousSessionId
//      actorRef ! AddSession(sessionId)
//      actor.sessions.size() should beEqualTo(1)
//
//      actorRef ! RemoveSession(sessionId)
//      actor.sessions.size() should beEqualTo(0)
//
//      sessionId = getAnonymousSessionId
//      actorRef ! AddSessionForUser(sessionId, getAnonymousUserId)
//      actor.sessions.size() should beEqualTo(1)
    }
  }

  trait mediaManagerScope extends Scope {
    val module = new MockLogicModule
    val mediaService = module.eagerMediaService
  }
}
