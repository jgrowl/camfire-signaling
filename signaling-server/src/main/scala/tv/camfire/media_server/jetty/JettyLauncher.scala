package tv.camfire.media_server.jetty

import org.scalatra.servlet.ScalatraListener
import tv.camfire.jetty.server.session.{RedisClientFactory, RedisRailsSessionIdManager, RedisRailsSessionManager}
import tv.camfire.media_server.config.{Properties, ServletModule}
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import org.eclipse.jetty.server.session.SessionHandler
import org.eclipse.jetty.servlet.ServletHolder
import tv.camfire.media_server.filter.RequireSessionFilter
import javax.servlet.DispatcherType
import java.util

/**
 * User: jonathan
 * Date: 7/21/13
 * Time: 11:15 PM
 */
object JettyLauncher {
  // I pulled the launcher out mainly to the e2e tests could access some of the bootstrapped objects. I would like
  // to find a better way of doing this.
  var modules: ServletModule = null
  def main(args: Array[String]) {
    modules = new ServletModule {}
//    val modules = new ServletModule {}
    val properties = modules.properties
    val launcher = new JettyLauncher(modules, properties)
    launcher.start()
    launcher.join()
  }
}

class JettyLauncher(modules: ServletModule, properties: Properties) {
  val server = new Server(properties.mediaServerPort)
  val context: WebAppContext = new WebAppContext()
  server.setHandler(context)

  val sessionIdManager = new RedisRailsSessionIdManager()
  val redisClientFactory = new RedisClientFactory(properties.redisHost, properties.redisPort, properties.sessionKeyPrefix)
  val sessionManager = new RedisRailsSessionManager(redisClientFactory)

  server.setSessionIdManager(sessionIdManager)
  sessionManager.setSessionIdManager(sessionIdManager)

  val sessionHandler = new SessionHandler(sessionManager)
  sessionHandler.setSessionManager(sessionManager)
  context.setSessionHandler(sessionHandler)

  val resourcePath = properties.resourcePath
  context setContextPath properties.contextPath
  context.setResourceBase(resourcePath)
  context.setDescriptor(resourcePath + "/WEB-INF/web.xml")
  context.addEventListener(new ScalatraListener)

  val DefaultDispatcherTypes: util.EnumSet[DispatcherType] =
    util.EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC)

  //    context.setInitParameter("org.atmosphere.cpr.AtmosphereInterceptor", "tv.camfire.media_server.signal.RequireSessionInterceptor")
  context.addFilter(classOf[RequireSessionFilter], "/*", DefaultDispatcherTypes)

  context.setInitParameter("org.eclipse.jetty.servlet.SessionCookie", properties.sessionCookie)

  // Testing redis pubsub
//  context.setInitParameter("org.atmosphere.plugin.redis.RedisBroadcaster.server", "http://localhost")

  context.addEventListener(modules.sessionListener)
  context.addServlet(new ServletHolder(modules.signalController), "/*")

  def start() {
    server.start()
  }

  def stop() {
    server.stop()
  }

  def join() {
    server.join()
  }
}
