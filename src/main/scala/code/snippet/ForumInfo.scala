package code
package snippet

import model._
import net.liftweb._
import util._
import Helpers._
import sitemap._

object ForumInfo {
  lazy val menu = Menu.i("Forums") / "forums"

  def render = "a" #> (1 to 10).map(Forum.apply).
  map(f => "* [href]" #> AForum.menu.calcHref(f) &
      "* *+" #> f.id)
}
