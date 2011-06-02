package code
package snippet

import net.liftweb._
import http._
import util._
import Helpers._

object BackTo {
  def render = "type=submit" #> SHtml.onSubmitUnit(() => {
    S.notice("I eat yaks for breakfast")
    S.redirectTo("/")
  })
}
