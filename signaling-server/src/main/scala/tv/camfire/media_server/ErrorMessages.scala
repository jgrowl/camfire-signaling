package tv.camfire.media_server

/**
 * User: jonathan
 * Date: 7/30/13
 * Time: 4:08 PM
 */
object ErrorMessages {
  val UNKNOWN_SIGNAL = "Unknown Signal"
  val FAILED_SIGNAL_PARSE = "Could not parse a signal in the request"
  val FAILED_DATA_PARSE = "Could not parse embedded data inside of the signal"
  val CLIENT_ERROR = "There may be a problem with the javascript client or someone is trying to do something malicious"

  // This user can really send anything they want. They could try to send a session that did not ever exist in the
  // system. It could be indicative that there is a client side problem or that the user is up to something malicious.
  val _NO_SESSION_COMPANION = "There was no associated companion for the requested session [%s] to forward the message [%s]!"
  def NO_SESSION_COMPANION(sessionId: String, msg: AnyRef): String = {
    _NO_SESSION_COMPANION.format(sessionId, msg)
  }
}
