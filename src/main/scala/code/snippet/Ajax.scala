package code.snippet

/**
 * Created by IntelliJ IDEA.
 * User: dpp
 * Date: 10/17/11
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */

import net.liftweb._
import http._
import js._
import JsCmds._
import util._
import Helpers._

class Ajax {
  def render = {
    var foo = 0
    var bar = 0
    var baz = 0

    def drawMe() = {
      SetHtml("foobarbaz", <span>foo = {foo} bar = {bar} and baz = {baz}</span>)
    }

    "#foo [onclick]" #> SHtml.ajaxInvoke(() => {foo += 1; drawMe()}) &
    "#bar [onclick]" #> SHtml.ajaxInvoke(() => {bar += 1; drawMe()}) &
    "#baz [onclick]" #> SHtml.ajaxInvoke(() => {baz += 1; drawMe()})
  }
}