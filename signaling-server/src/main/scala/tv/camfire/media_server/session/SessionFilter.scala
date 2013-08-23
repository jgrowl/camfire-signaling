package tv.camfire.media_server.session

import javax.servlet._

/**
 * User: jonathan
 * Date: 6/6/13
 * Time: 12:57 PM
 */
class SessionFilter extends Filter {
  //TODO: Use this instead of an atmosphere interceptor
  def init(filterConfig: FilterConfig) {}

  def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {}

  def destroy() {}
}
