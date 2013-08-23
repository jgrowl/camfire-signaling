package tv.camfire.media_server.filter

import javax.servlet._
import javax.servlet.http.HttpServletRequest
import org.slf4j.{LoggerFactory, Logger}


/**
 * User: jonathan
 * Date: 7/29/13
 * Time: 1:27 PM
 */
class RequireSessionFilter extends Filter {
  private val _log: Logger = LoggerFactory.getLogger(getClass)

  def init(filterConfig: FilterConfig) {}

  def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
    val session = request.asInstanceOf[HttpServletRequest].getSession(false)
    if (session == null) {
      _log.debug("Rejecting request: no associated session.")
      return
    }
    chain.doFilter(request, response)
  }

  def destroy() {}

}
