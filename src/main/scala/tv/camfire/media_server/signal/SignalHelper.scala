package tv.camfire.media_server.signal

import org.slf4j.{LoggerFactory, Logger}
import org.atmosphere.cpr.Broadcaster
import tv.camfire.media_server.bridge.BroadcasterFactoryBridge

/**
 * User: jonathan
 * Date: 4/30/13
 * Time: 9:27 PM
 */
class SignalHelper(broadcasterFactoryBridge: BroadcasterFactoryBridge) {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  def logSignal(signal: Signal) {
    logger.debug("Message type received: " + signal.`type`)
    logger.debug("Message data received: " + signal.data)
  }

  def getSessionBroadcaster(sessionId: String, createIfNull: Boolean = false): Broadcaster = {
    broadcasterFactoryBridge.getDefault.lookup(getBroadcasterName(sessionId), createIfNull)
  }

  def removeSessionBroadcaster(sessionId: String) {
    broadcasterFactoryBridge.getDefault.remove(getBroadcasterName(sessionId))
  }

  def getBroadcasterName(sessionId: String): String = {
    "/session/%s".format(sessionId)
  }

  def getSignalBroadcaster: Broadcaster = {
    val signalBroadcaster = broadcasterFactoryBridge.getDefault.lookup("/signal", true)
    signalBroadcaster
  }
}
