package code
package snippet

import model._

import net.liftweb._
import util._
import Helpers._
import sitemap._

object AForum {
  lazy val menu = Menu.param[Forum]("Forum", "Forum", 
                                    Forum.find, _.id.toString) / "forums" / *
}

class AForum(forum: Forum) {
  def m: Loc[(Forum, FThread)] = AThread.menu

  def render = "a" #> (1 to 10).map(i => FThread(i + forum.id)).
  map(t => "* [href]" #> AThread.menu.calcHref((forum, t)) &
      "* *+" #> t.id.toString)
}
