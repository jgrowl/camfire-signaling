package tv.camfire.media_server.server

import akka.actor.Actor
import org.slf4j.{LoggerFactory, Logger}

/**
 * User: jonathan
 * Date: 5/2/13
 * Time: 7:19 PM
 */
trait MediaServer extends Actor {
  private val log: Logger = LoggerFactory.getLogger(getClass)
  protected def mediaManagement: Receive
  protected def sessionManagement: Receive

  def receive: Actor.Receive = sessionManagement orElse mediaManagement

  override def preStart() {
    super.preStart()
    log.info("Media server is starting up...")
  }

  override def postStop() {
    super.postStop()
    log.info("Media server is shutting down...")
  }
}
