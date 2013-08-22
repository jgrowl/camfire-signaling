package tv.camfire.media_server.listener

import javax.servlet.http.{HttpSessionEvent, HttpSessionListener}
import tv.camfire.jetty.server.session.RedisRailsSessionUtil
import org.slf4j.{LoggerFactory, Logger}
import tv.camfire.media_server.signal.SignalHelper
import tv.camfire.media_server.server.{RemoveSession, AddSessionForUser, AddSession}
import akka.actor.ActorRef
import akka.util.Timeout
import scala.concurrent.Await

/**
 * User: jonathan
 * Date: 6/6/13
 * Time: 12:37 PM
 */
class SessionListener(mediaService: ActorRef, signalHelper: SignalHelper) extends HttpSessionListener {
  private val log: Logger = LoggerFactory.getLogger(getClass)
  import _root_.akka.pattern.ask
  implicit val askTimeout = Timeout(10000)

  def sessionCreated(se: HttpSessionEvent) {
    val session = se.getSession
    val sessionId = session.getId

//    log.debug("Creating broadcaster [%s]...".format(signalHelper.getBroadcasterName(sessionId)))
//    signalHelper.getSessionBroadcaster(sessionId, createIfNull = true)

    val userId = RedisRailsSessionUtil.getWardenUserIdAsInt(session)
    if (userId == null) {
      val future = mediaService ? AddSession(sessionId)
      val createSuccess = Await.result(future, new Timeout(5000).duration).asInstanceOf[Boolean]
      if (createSuccess) {
        log.debug("Successfully created SessionCompanion [%s].".format(sessionId))
      }
    } else {
      val future = mediaService ? AddSessionForUser(sessionId, userId)
      val createSuccess = Await.result(future, new Timeout(5000).duration).asInstanceOf[Boolean]
      if (createSuccess) {
        log.debug("Successfully created SessionCompanion [%s] for user [%s].".format(sessionId, userId))
      }
    }
  }

  def sessionDestroyed(se: HttpSessionEvent) {
    val session = se.getSession
    val sessionId = session.getId

    log.debug("Removing broadcaster [%s]...".format(signalHelper.getBroadcasterName(sessionId)))
    signalHelper.removeSessionBroadcaster(sessionId)

    mediaService ! RemoveSession(sessionId)
  }

}
