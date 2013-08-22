package tv.camfire.media_server.test.e2e.specs

import java.util.concurrent.{TimeUnit, CountDownLatch, ExecutionException}
import tv.camfire.media_server.test.util.e2e.connection.SimpleConnection
import tv.camfire.media_server.test.util.e2e.StandaloneScalatraSpec
import tv.camfire.media_server.test.util.TestUtil._
import tv.camfire.media_server.test.e2e.webrtc.AsyncHttpClientPeerConnectionObserver
import tv.camfire.media_server.signal.Signal
import redis.embedded.RedisServer
import org.specs2.specification.{Step, Fragments}

/**
 * User: jonathan
 * Date: 7/29/13
 * Time: 12:08 AM
 */
class EndToEndSpec extends StandaloneScalatraSpec {

//  val embeddedRedis = new RedisServer(6379)
  //override def map(fs: =>Fragments) = Step(embeddedRedis.start()) ^ fs ^ Step(embeddedRedis.stop())

  // isolated=true, will boot up a separate server instance for each test.

//  def before: Any = {
//    embeddedRedis.start()
//  }
//
//  def after: Any = {
//    embeddedRedis.stop()
//  }

  def is = args(sequential=true)                                                   ^
    "MediaServer End to End specification"                                         ^
      "Requests without a session should"                                          ^
        "result in a failed handshake"                                        ! e1 ^
      "Requests with an existing session should"                                   ^
        "be able to connect to the web socket"                                ! e2 ^
        "get an answer back from after making an offer"                       ! e3 ^
        "get no response when"                                                     ^
          "non JSON signal"                                                   ! e4 ^
          "unrecognized signal"                                               ! e5 ^
          "good offer signal but bad IceCandidate data"                       ! e6

  // TODO: Change expectation to look for message: Invalid handshake response.
  // Cannot figure out how to do this because there is a chained exception. It should look like as follows:
  //    test() must throwA[ExecutionException].like { case e => e must beEqualTo("java.util.concurrent.ExecutionException: java.io.IOException: Invalid handshake response")}
  def e1 = unauthenticatedRequestFailsHandshake

  def e2 = sessionCanConnect

  def e3 = sessionGetsAnswerBackFromOffer

  def e4 = getNothingOnNonJsonRequest

  def e5 = getNothingOnUnrecognizedSignal

  def e6 = getsNothingOnOfferWithInvalidCandidate

  def unauthenticatedRequestFailsHandshake = {
    new SimpleConnection().websocket.close() must throwAn[ExecutionException]
  }

  def sessionCanConnect = {
    val sessionId = getAnonymousSessionId
    createSession(sessionId)
    val connection = new SimpleConnection(sessionId)
    val couldOpen = connection.websocket.isOpen
    connection.websocket.close()
    couldOpen must_== true
  }

  def sessionGetsAnswerBackFromOffer = {
    val sessionId = getAnonymousSessionId
    createSession(sessionId)
    val connection = new SimpleConnection(sessionId)

    val peerConnectionObserver = new AsyncHttpClientPeerConnectionObserver(connection.websocket)
    val peerConnection = webRtcHelper.createPeerConnection(sessionId, peerConnectionObserver)
    val offer = webRtcHelper.makeOffer(peerConnection).getLocalDescription
    val goodOfferDescriptionString = serializationHelper.writeValueAsString(offer)
    val offerString = serializationHelper.writeValueAsString(new Signal("offer", goodOfferDescriptionString))
    connection.websocket.sendTextMessage(offerString)
    new CountDownLatch(1).await(1, TimeUnit.SECONDS)
    connection.websocket.close()
    connection.receivedAMessage must_== true
    connection.webSocketTextListener.receivedExactly("answer", 1)
  }

  def getNothingOnNonJsonRequest = {
    val sessionId = getAnonymousSessionId
    createSession(sessionId)
    val connection = new SimpleConnection(sessionId)
    connection.websocket.sendTextMessage(getAnonymousNonJsonString)
    new CountDownLatch(1).await(1, TimeUnit.SECONDS)
    connection.websocket.close()
    connection.receivedAMessage must_== false
  }

  def getNothingOnUnrecognizedSignal = {
    val sessionId = getAnonymousSessionId
    createSession(sessionId)
    val connection = new SimpleConnection(sessionId)
    connection.websocket.sendTextMessage(getAnonymousUnrecognizedSignalString)
    val l = new CountDownLatch(1)
    l.await(2, TimeUnit.SECONDS)
    connection.websocket.close()
    connection.receivedAMessage must_== false
  }

  def getsNothingOnOfferWithInvalidCandidate = {
    val sessionId = getAnonymousSessionId
    createSession(sessionId)
    val connection = new SimpleConnection(sessionId)
    connection.websocket.sendTextMessage(serializationHelper.createSignalString("offer", getAnonymousString))
    connection.websocket.close()
    connection.receivedAMessage must_== false
  }
}
