package code 
package snippet

import net.liftweb._
import util._
import Helpers._

case class Thing(other: Other)

abstract class Other
case class Other1(text: String) extends Other
case class Other2(number: Int) extends Other

object Things {
 def data = List(
   Thing(Other1("something")),
   Thing(Other2(123)),
   Thing(Other1("another one")),
   Thing(Other2(456)),
   Thing(Other2(456)),
   Thing(Other2(99)),
   Thing(Other2(456))
 )

  def render = "* *" #> data.map{
    case Thing(Other1(str)) => ".other1 ^^" #> true andThen ".text *+" #> str
    case Thing(Other2(num)) => ".other2 ^^" #> true andThen ".number *+" #> num
  }
}
