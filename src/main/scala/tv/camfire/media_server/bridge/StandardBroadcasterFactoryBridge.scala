package tv.camfire.media_server.bridge

import org.atmosphere.cpr.BroadcasterFactory

/**
 * User: jonathan
 * Date: 7/16/13
 * Time: 3:27 PM
 */
class StandardBroadcasterFactoryBridge extends BroadcasterFactoryBridge {
  def getDefault: BroadcasterFactory = {
    BroadcasterFactory.getDefault
  }
//  def broadcast(o: Object) : Future[Object] = Bro
}
