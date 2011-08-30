package code
package snippet

import net.liftweb._
import common._
import http._
import util._
import Helpers._
import builtin.snippet._

object SaveClassPlace extends TransientRequestVar[Box[String]](Empty)

object BodyClass {
  def render = "body [class+]" #> SaveClassPlace.is
}

object save_class {
  def render = {
    SaveClassPlace.set(S.attr("body_class"))
    PassThru
  }
}
