package tv.camfire.media_server.server

import tv.camfire.media_server.Types._
import tv.camfire.media_server.config.factory.SessionCompanionFactoryFactory


/**
 * User: jonathan
 * Date: 5/6/13
 * Time: 4:25 PM
 */
class MediaService(sessionCompanionFactoryFactory: SessionCompanionFactoryFactory) extends MediaServer
with SessionManager
with MediaManager {
  protected val _sessionCompanionFactory: SessionCompanionFactory = sessionCompanionFactoryFactory.create()
}
