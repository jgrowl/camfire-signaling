package tv.camfire.media_server.test.util.e2e

import org.scalatra.test.specs2.ScalatraSpec
import tv.camfire.media_server.config.{Properties, ServletModule}
import tv.camfire.media_server.jetty.JettyLauncher
import com.redis.RedisClientPool
import tv.camfire.media_server.test.util.TestUtil
import tv.camfire.media_server.serialization.SerializationHelper
import tv.camfire.media_server.webrtc.WebRtcHelper
import tv.camfire.media_server.test.util.TestUtil._

/**
 * User: jonathan
 * Date: 7/30/13
 * Time: 12:39 AM
 */
trait StandaloneScalatraSpec extends ScalatraSpec with TestUtil {

  val modules = new ServletModule {
    override lazy val properties = new Properties {
      override def mediaServerPort = 0
//      override def redisPort = 0
    }
  }

  val serializationHelper: SerializationHelper = modules.serializationHelper
  val webRtcHelper: WebRtcHelper = modules.webRtcHelper

  protected implicit var _actualPort: Int = 0
  implicit var properties = modules.properties

  val jettyLauncher = new JettyLauncher(modules, properties)
  override lazy val server = jettyLauncher.server
  protected var _redisClient: RedisClientPool = null

  override def start() {
    jettyLauncher.start()
    // We can only get the actual port after jetty has started when using a random port
    _actualPort = server.getConnectors()(0).getLocalPort
    _redisClient = jettyLauncher.redisClientFactory.create()
  }

  def createSession(sessionId: String) {
    _redisClient.withClient {
      client => {
        client.set(sessionId, getAnonymousSessionJson)
      }
    }
  }

//  def findFreePort: Int = {
//    var socket: ServerSocket = null
//    try {
//      socket = new ServerSocket(0)
//      return socket.getLocalPort
//    }
//    catch {
//      case e: IOException => {
//      }
//    }
//    finally {
//      if (socket != null) {
//        try {
//          socket.close
//        }
//        catch {
//          case e: IOException => {
//          }
//        }
//      }
//    }
//    -1
//  }

}
