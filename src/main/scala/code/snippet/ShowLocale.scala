package code
package snippet


import net.liftweb._
import http._

class ShowLocale {
  def render = <span>{S.locale.toString}</span>
}

