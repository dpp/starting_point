package code
package snippet

import comet._

import net.liftweb._
import http._
import sitemap._
import util._
import Helpers._

object ViewMenu {
  val menu = 
    Menu.param[ChatMessage]("View", "View",
                            s => for {
                              p <- asInt(s)
                              m <- ChatServer.msgAt(p)
                            } yield ChatMessage(m, p),
                            _.pos.toString) / "view"
}

class ViewMsg(msg: ChatMessage) {
  def render = "* *" #> msg.msg
}
