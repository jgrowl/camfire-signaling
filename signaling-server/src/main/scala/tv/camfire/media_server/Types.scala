package tv.camfire.media_server

import tv.camfire.media_server.config.factory.{PeerConnectionObserverFactoryFactory, SessionCompanionFactoryFactory}


/**
 * User: jonathan
 * Date: 7/22/13
 * Time: 11:26 PM
 */
object Types {
  type SessionCompanionFactory = SessionCompanionFactoryFactory#create
  type PeerConnectionObserverFactory = PeerConnectionObserverFactoryFactory#create
}
