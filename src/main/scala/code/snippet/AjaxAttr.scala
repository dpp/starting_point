package code
package snippet

import net.liftweb._
import http._
import js._
import JsCmds._
import JE._
import util._
import Helpers._

object AjaxAttr {
	def render = "* [onclick]" #> SHtml.ajaxCall(JsRaw("this.id"), s => {
		println("Dude... I'm on the server and the parameter was "+s)
		Alert("The id of the clicked elements was "+s)})
}