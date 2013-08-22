package tv.camfire.media_server.util

import bcrypt_jruby.BCrypt
import java.util
import java.util.Comparator

/**
 * User: jonathan
 * Date: 6/12/13
 * Time: 6:06 PM
 */
object BCryptUtils {
  def hashAndSalt(secret: String): String = {
    BCrypt.hashpw(secret, BCrypt.gensalt())
  }
}

class BCryptMap[T] extends util.TreeMap[String, T](new BCryptComparator) {}

class BCryptComparator extends Comparator[String] {
  // TODO: Optimize this! Very inefficient as it does not actually order anything.
  def compare(o1: String, o2: String): Int = {
    try {
      if (o1.equals(o2) || BCrypt.checkpw(o2, o1)) {
        return 0
      }
    } catch {
      case e: Exception =>
    }

    1
  }
}
