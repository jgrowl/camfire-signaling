package tv.camfire.util

import javax.naming.InitialContext

/**
 * User: jonathan
 * Date: 6/29/13
 * Time: 11:10 PM
 */
object JndiHelper {
  def getJndiObject[A](jndiName: String, clazz: Class[A]): A = {
    try {
      val ic = new InitialContext()
      val obj = ic.lookup(jndiName)
      obj.asInstanceOf[A]
    } catch {
      case e: Exception =>
        throw new IllegalStateException("Unable to find instance in JNDI location " + jndiName + " : " + e.getMessage, e)
    }
  }

//  def getServer : Server = {
//    getJndiObject("media-server/server", classOf[Server])
//  }
}
