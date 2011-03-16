package code
package model

import net.liftweb._
import common._
import util._

case class Forum(id: Int)

object Forum {
  def find(id: String): Box[Forum] = Helpers.asInt(id).map(Forum.apply)

  def unapply(id: String): Option[Forum] = find(id)

  def unapply(in: Any): Option[Int] = in match {
    case f: Forum => Some(f.id)
    case _ => None
  }
}
