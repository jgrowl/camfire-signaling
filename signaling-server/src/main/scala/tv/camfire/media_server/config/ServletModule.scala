package tv.camfire.media_server.config

import tv.camfire.media_server.controllers.SignalController
import tv.camfire.media_server.listener.SessionListener

/**
 * User: jonathan
 * Date: 7/22/13
 * Time: 7:01 PM
 */
trait ServletModule extends LogicModule {
  lazy val signalController: SignalController = wire[SignalController]
  lazy val sessionListener: SessionListener = wire[SessionListener]
}
