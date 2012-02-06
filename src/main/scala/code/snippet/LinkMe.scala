package code.snippet

/**
 * Created by IntelliJ IDEA.
 * User: dpp
 * Date: 2/6/12
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */

import net.liftweb._
import http._
import js._
import JsCmds._
import util._
import Helpers._

class LinkMe {
  def render = "a [onclick]" #> SHtml.ajaxInvoke(() =>
  {
    println("Server got ajax click at "+now);
    Alert("Thanks for clicking")})
}