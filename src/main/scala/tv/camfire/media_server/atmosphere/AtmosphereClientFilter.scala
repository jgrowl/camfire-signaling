package tv.camfire.media_server.atmosphere

import org.scalatra.atmosphere._

/**
 * User: jonathan
 * Date: 8/1/13
 * Time: 4:08 PM
 */
trait AtmosphereClientFilter {

  protected implicit var _resourceUuid: String

  /**
   * Deliver the message to everyone except the current user.
   */
  final protected def SkipSelf: ClientFilter = _.uuid != _resourceUuid
  final protected def Others: ClientFilter = SkipSelf

  /**
   * Deliver the message only to the current user.
   */
  final protected def OnlySelf: ClientFilter = _.uuid == _resourceUuid

  final protected def Me: ClientFilter = OnlySelf

  /**
   * Deliver the message to all connected users.
   */
  final protected val Everyone: ClientFilter = _ => true

}
