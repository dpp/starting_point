package code
package snippet

import net.liftweb._
import http._
import util._
import Helpers._

/**
 * A snippet that demonstrates
 * memoizing a template and then
 * doing an ajax update
 */
object ShowMemoize {
  def render =
    "div" #> SHtml.idMemoize(
      outer => 
      // redraw the whole div when this button is pressed
      "@refresh_all [onclick]" #> SHtml.ajaxInvoke(outer.setHtml _) &

      // deal with the "one" div
      "@one" #> SHtml.idMemoize(
        one => 
          "span *+" #> now.toString & // display the time
        "button [onclick]" #> SHtml.ajaxInvoke(one.setHtml _)) & // redraw 

      // deal with the "two" div
      "@two" #> SHtml.idMemoize(
        two => // the "two" div
          // display a bunch of items
          "ul *" #> (0 to randomInt(6)).map(i => "li *+" #> i) &
        // update the "two" div on button press
        "button [onclick]" #> SHtml.ajaxInvoke(two.setHtml _)))
}
