package code
package snippet

import net.liftweb._
import http._
import js._
import JsCmds._
import JE._

import util._

import comet._

object Input {
  def render = SHtml.onSubmit(s => {
    MyJxOut.is.foreach(_ ! Msg(Helpers.nextFuncName, s))
    SetValById("thing", "")
  })
}
