package code.snippet

/**
 * Created by IntelliJ IDEA.
 * User: dpp
 * Date: 1/16/12
 * Time: 1:38 PM
 * To change this template use File | Settings | File Templates.
 */

import net.liftweb._
import http._
import js._
import JsCmds._
import JE._
import util._
import Helpers._

class Moose {
  def render = "* [onclick]" #> SHtml.ajaxInvoke(() => Replace("moose", <span id="moose">{(new java.util.Date).toString}</span>))
}