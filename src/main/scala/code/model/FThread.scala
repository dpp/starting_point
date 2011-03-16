package code
package model

import net.liftweb._
import common._
import util._

case class FThread(id: Int)

object FThread {
  def find(id: String): Box[FThread] = Helpers.asInt(id).map(FThread.apply)

  def unapply(id: String): Option[FThread] = find(id)

  def unapply(ft: Any): Option[Int] = ft match {
    case ft: FThread => Some(ft.id)
    case _ => None
  }
}
