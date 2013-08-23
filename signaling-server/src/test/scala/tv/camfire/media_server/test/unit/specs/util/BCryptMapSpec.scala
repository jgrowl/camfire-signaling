package tv.camfire.media_server.test.unit.specs.util

import org.specs2.mutable.Specification
import tv.camfire.media_server.util.{BCryptUtils, BCryptMap}

/**
 * User: jonathan
 * Date: 6/12/13
 * Time: 5:31 PM
 */
class BCryptMapSpec extends Specification {

  "A BCryptMap" should {
    "actualKeyExistsTest" in {
      val (map, actualKey, actualValue) = getBCryptMapWithOneKeyAndValue
      map.get(actualKey) must_== actualValue
    }

    "invalidKeyDoesNotExistTest" in {
          val (map, actualKey, actualValue) = getBCryptMapWithOneKeyAndValue
          map.get(actualKey + "-invalid-sufix") must_!= actualValue
    }

    "saltedAndHashedKeyExistsTest" in {
          val (map, actualKey, actualValue) = getBCryptMapWithOneKeyAndValue
          val saltedAndHashedKey = BCryptUtils.hashAndSalt(actualKey)
          map.get(saltedAndHashedKey) must_== actualValue
    }

    //
    ////  @Test
    ////  def manualTest() {
    ////    val test = "3552af58ad3f8283f1cb4c254a232b89"
    ////    val test2 = "$2a$10$RSommU6IpKw/bP9bEWRqEu0dwLUWLEZxWYpATtP0gGP2pVKoUZIrK"
    ////    Assert.assertTrue(BCrypt.checkpw(test, test2))
    ////  }
    //

  }

  def getBCryptMapWithOneKeyAndValue: (BCryptMap[String], String, Any) = {
    val map = new BCryptMap[String]
    val actualKey = "my-test-string"
    val actualValue = "my-throw-away-value"
    map.put(actualKey, actualValue)
    (map, actualKey, actualValue)
  }

  def getBCryptMapWithFewKeys: (BCryptMap[String], String, Any) = {
    val map = new BCryptMap[String]
    val actualKey = "my-test-string"
    val actualValue = "my-throw-away-value"
    map.put("a7a295de01c2576fdf96b14b63ddaa00", "none")
    map.put(actualKey, actualValue)
    map.put("4378898036c1879f81cfec5fd29dbfb9", "none")
    (map, actualKey, actualValue)
  }
}
