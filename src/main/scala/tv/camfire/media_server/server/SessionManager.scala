package tv.camfire.media_server.server

import akka.actor.{ActorRef, Actor}
import tv.camfire.media_server.Types._
import org.slf4j.{LoggerFactory, Logger}
import tv.camfire.media_server.util.BCryptMap

/**
 * User: jonathan
 * Date: 5/2/13
 * Time: 8:06 PM
 */


sealed trait SessionManagerEvent
case class AddSession(sessionId: String) extends SessionManagerEvent
case class AddSessionForUser(sessionId: String, userId: Int) extends SessionManagerEvent
case class RemoveSession(sessionId: String) extends SessionManagerEvent
case class RegisterResourceUuid(sessionId: String, resourceUuid: String) extends SessionManagerEvent

trait SessionManager extends Actor {
  private val log: Logger = LoggerFactory.getLogger(getClass)
  protected val _sessionCompanionFactory: SessionCompanionFactory
  val sessions = new BCryptMap[ActorRef]

  protected def sessionManagement: Receive = {
    case msg@AddSession(sessionId) =>
      log.debug("Creating [%s] session...".format(sessionId))
      if (!sessions.containsKey(sessionId)) {
        val session = _sessionCompanionFactory.createSessionCompanion(sessionId)
        sessions.put(sessionId, session)
      }
      // TODO: Figure out if I really need to send true/false
      sender ! true
    case msg@AddSessionForUser(sessionId, userId) =>
      log.debug("Creating [%s] session for user [%s]...".format(sessionId, userId))
      if (!sessions.containsKey(sessionId)) {
        val session = _sessionCompanionFactory.createUserSessionCompanion(sessionId, userId)
        sessions.put(sessionId,session)
      }
      sender ! true
    case msg@RemoveSession(sessionId) =>
      log.debug("Deleting session object for [%s]...".format(sessionId))
      if (sessions.containsKey(sessionId)) {
        context.stop(sessions.get(sessionId))
        sessions.remove(sessionId)
      }
    case msg@GetSessionIdHash(sessionId) =>
      if (sessions.containsKey(sessionId)) {
        sessions.get(sessionId) ! GetSessionIdHash(sessionId)
      }
    case msg@RegisterResourceUuid(sessionId, resourceUuid) =>
      if (sessions.containsKey(sessionId)) {
        sessions.get(sessionId) forward msg
      }
  }
}
