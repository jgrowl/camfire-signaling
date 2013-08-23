package tv.camfire.test.midway.model

//import org.scalatest._
//import redis.clients.jedis.JedisPool
//import org.apache.commons.pool.impl.GenericObjectPool.Config
//import tv.camfire.config.Properties
//import tv.camfire.test.util.TestUtil
//import TestUtil._
//import java.util
//import org.scalatest.matchers.ShouldMatchers
//import tv.camfire.model.SessionInfo
//import tv.camfire.model.StreamInfo
//
//
///**
// * User: jonathan
// * Date: 7/11/13
// * Time: 8:09 PM
// */
//class SessionInfoTest extends FlatSpec with BeforeAndAfter with ShouldMatchers with Properties {
//
//  protected val jedisPool = new JedisPool(new Config(), redisHost, redisPort)
//  JOhm.setPool(jedisPool)
//  protected val jedis = jedisPool.getResource
//
//  before {
//    jedis.flushDB()
//  }
//
//  "A Session" should "be savable, searchable, and delete-able" in {
//    val session = new SessionInfo()
//    val sessionId = getAnonymousSessionId
//    session.sessionId = sessionId
//    JOhm.save(session)
//
//    val stream: StreamInfo = new StreamInfo()
//    JOhm.save(stream)
//
//    session.stream = stream
//    JOhm.save(session)
//
//    val savedSessions: util.ArrayList[SessionInfo] = JOhm.find(classOf[SessionInfo],
//      "sessionId", sessionId).asInstanceOf[util.ArrayList[SessionInfo]]
//    var savedSession = savedSessions.get(0)
//    savedSession should not be null
//
//    savedSession = JOhm.get(classOf[SessionInfo], 1)
//    savedSession should not be null
//
//    var savedStream: StreamInfo = JOhm.get(classOf[StreamInfo], 1)
//    savedStream should not be null
//
//    JOhm.delete(classOf[SessionInfo], 1, true, true)
//
//    savedSession = JOhm.get(classOf[SessionInfo], 1)
//    savedSession should be(null)
//
//    savedStream = JOhm.get(classOf[StreamInfo], 1)
//    savedStream should be(null)
//  }
//}




