package code
package snippet

import comet.ChatServer

import net.liftweb._
import http._
import js._
import JsCmds._
import JE._

object ChatIn {
  def render = SHtml.onSubmit(s => {
    ChatServer ! s
    SetValById("chat_in", "")
  })
}
