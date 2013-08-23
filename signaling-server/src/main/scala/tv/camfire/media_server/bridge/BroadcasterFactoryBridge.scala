package tv.camfire.media_server.bridge

import org.atmosphere.cpr.BroadcasterFactory


/**
 * User: jonathan
 * Date: 7/16/13
 * Time: 3:22 PM
 */
trait BroadcasterFactoryBridge {
  def getDefault: BroadcasterFactory
}
