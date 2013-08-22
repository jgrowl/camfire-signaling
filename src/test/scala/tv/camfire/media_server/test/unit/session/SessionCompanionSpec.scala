package tv.camfire.media_server.test.unit.session

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import tv.camfire.media_server.session.SessionCompanion
import tv.camfire.media_server.test.util.TestUtil._
import tv.camfire.media_server.test.config.MockLogicModule
import akka.pattern.ask
import tv.camfire.media_server.server.ClientOffer
import scala.util.Success
import akka.util.Timeout


/**
 * User: jonathan
 * Date: 7/13/13
 * Time: 5:31 PM
 */
class SessionCompanionSpec extends Specification {
  implicit val askTimeout = Timeout(10)

  val sutName = classOf[SessionCompanion].getName
  "A %s".format(sutName) should {
    "accept session description offers from the client" in new sessionCompanionScope {
      val sessionCompanion = module.getTestSessionCompanion(sessionId)
      val future = sessionCompanion ? ClientOffer(getAnonymousSessionId, getAnonymousOffer)
      val Success(result: String) = future.value.get
      println(result)

      //      val description = mock[SessionDescription]
      //      sessionCompanion ! ClientOffer(sessionId, description)
      //      there was one(configuredSignalBroadcaster).broadcast(any[String])
    }
    //
    //    "accept session description answers from the client" in new sessionCompanionScope {
    //      val answerDescription = mock[SessionDescription]
    //      sessionCompanion ! ClientAnswer(sessionId, answerDescription)
    //      true
    //    }
    //
    //    "accept ice candidates from the client " in new sessionCompanionScope {
    //      val iceCandidate = mock[IceCandidate]
    //      sessionCompanion ! ClientIceCandidate(sessionId, iceCandidate)
    //      true
    //    }
    //
    //    "be able to subscribe to a specified media stream" in new sessionCompanionScope {
    //      //    actorRef ! SubscribeToMediaStream(userId: String, targetSessionIdHash: String)
    //      sessionCompanion ! SubscribeToMediaStream("", null)
    //      true
    //    }
    //
    //    "be able to get a session ID hash" in new sessionCompanionScope {
    //      sessionCompanion ! GetSessionIdHash(sessionId)
    //      true
    //    }
    //
    //    "be able to register a specified MediaStream" in new sessionCompanionScope {
    //      val mediaStream = mock[MediaStream]
    //      sessionCompanion ! RegisterMediaStream(sessionId, mediaStream)
    //      true
    //    }
    //
    //    "be able to un-register all MediaStreams" in new sessionCompanionScope {
    //      sessionCompanion ! UnRegisterAllMediaStreams(sessionId)
    //      true
    //    }
    //
    //    "get a MediaStream" in new sessionCompanionScope {
    //      sessionCompanion ! GetMediaStream()
    //    }
    //
    //    // TODO: Fix this name
    //    "be able to tell a MediaStream" in new sessionCompanionScope {
    //      //      actorRef ! TellActorMediaStream(null)
    //      true
    //    }
    //
    "be able to receive MediaStream" in new sessionCompanionScope {
      //      val receiveMediaStream = mock[MediaStream]
      //      sessionCompanion ! ReceiveActorMediaStream(receiveMediaStream)
      true
    }
  }

  trait sessionCompanionScope extends Scope {
    val module = new MockLogicModule
//    val mediaService = module.eagerMediaService

    val sessionId = getAnonymousSessionId


    //    val sessionCompanion = TestActorRef(Props(new SessionCompanion(sessionId)))
    //    val actor = sessionCompanion.underlyingActor
  }

}

