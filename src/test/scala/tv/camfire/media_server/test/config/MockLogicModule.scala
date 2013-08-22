package tv.camfire.media_server.test.config

import tv.camfire.media_server.config.LogicModule
import tv.camfire.media_server.server.MediaService
import akka.testkit.TestActorRef
import akka.actor.Props
import tv.camfire.media_server.session.SessionCompanion

/**
 * User: jonathan
 * Date: 7/28/13
 * Time: 6:17 PM
 */
class MockLogicModule extends LogicModule {
  // Note: It would probably be best if we could override the mediaService but for some reason TestActorRef
  // does not like something about the sessionCompanionFactoryFactory. I can see that the mediaService tries go
  // start up many times and then throws errors. It also seems to have something to do with the mediaService being
  // lazy. The fix for now is to just add in an eager media service to test off of.
  val eagerMediaService = TestActorRef(new MediaService(sessionCompanionFactoryFactory))

  def sessionCompanionProps(sessionId: String) = Props(
    new SessionCompanion(
      webRtcHelper,
      serializationHelper,
      signalHelper,
      camfirePeerConnectionFactory,
      sessionId))

  def getTestSessionCompanion(sessionId: String) = {
    TestActorRef[SessionCompanion](sessionCompanionProps(sessionId))
  }

}
