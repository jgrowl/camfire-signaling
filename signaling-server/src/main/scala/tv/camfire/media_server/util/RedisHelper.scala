package tv.camfire.util

import com.redis.RedisClient

/**
 * User: jonathan
 * Date: 7/11/13
 * Time: 3:14 PM
 */
object RedisHelper {

  // Clean this bad boy up
  val redisClient = new RedisClient("localhost", 6379)

}
