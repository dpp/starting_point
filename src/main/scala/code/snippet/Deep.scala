package code
package snippet

import net.liftweb._
import util._
import http._
import sitemap._

case class Thing(id: Long)

object DeepMenu {
  lazy val menu = Menu.param[Thing]("Foo", "Bar", 
                                    str => Helpers.asLong(str).map(Thing(_)),
                                    thing => thing.id.toString) / "deep" / "place"
}

class Deep(thing: Thing) {
  def render = <b>{thing}</b>
}
