package tv.camfire.media_server.signal

import org.atmosphere.cpr.{AtmosphereConfig, Action, AtmosphereResource, AtmosphereInterceptor}

/**
 * User: jonathan
 * Date: 5/13/13
 * Time: 11:38 PM
 */
class RequireSessionInterceptor extends AtmosphereInterceptor {

  def configure(config: AtmosphereConfig) {}

  def inspect(r: AtmosphereResource): Action = {
    val session = r.getRequest.getSession(false)

    if (session == null) {
      return Action.CANCELLED
    }

    Action.CONTINUE
  }

  def postInspect(r: AtmosphereResource) {}

}
