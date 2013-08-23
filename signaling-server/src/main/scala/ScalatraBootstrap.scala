import org.scalatra._
import javax.servlet.ServletContext
import tv.camfire.media_server.controllers.SignalController
//import org.scalatra.CorsSupport._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
//    context.mount(new SignalController(null), "/*")

//    context.initParameters(AllowedOriginsKey) = "http://localhost:8080"
//    context.initParameters(AllowedMethodsKey) = "GET, POST, OPTIONS"
//    context.initParameters(AllowedHeadersKey) = "Content-Type"
//    context.initParameters(PreflightMaxAgeKey) = "10"
//    context.initParameters(AllowCredentialsKey) = "false"
  }

}
