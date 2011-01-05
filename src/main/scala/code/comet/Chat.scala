package code
package comet

import net.liftweb._
import http._
import actor._
import util.Helpers._
import js._
import JsCmds._
import JE._

import js.jquery.JqJsCmds.{AppendHtml, FadeOut, Hide, FadeIn}
import java.util.Date
import scala.xml._
import util.Helpers._

object ChatServer extends LiftActor with ListenerManager {
  private var messages = Vector(Message("Welcome"))

  def createUpdate = messages

  override def lowPriority = {
    case s: String => {
      val m = Message(s)
      messages :+= m
      updateListeners(m -> messages)
    }

    case r @ Remove(guid) => {
      messages = messages.filterNot(_.guid == guid)
      updateListeners(r -> messages)
    }
  }
}

final case class Message(msg: String, when: Date = new Date(), 
                         guid: String = nextFuncName)
final case class Remove(guid: String)

class Chat extends CometActor with CometListener {
  private var msgs: Vector[Message] = Vector()

  def registerWith = ChatServer

  override def lowPriority = {
    case (Remove(guid), v: Vector[Message]) => {
      msgs = v
      partialUpdate(
        FadeOut(guid,TimeSpan(0),TimeSpan(500)) &
        After(TimeSpan(500),Replace(guid, NodeSeq.Empty)))
    }

    case (m: Message, v: Vector[Message]) => {
      msgs = v
      partialUpdate(
        AppendHtml("ul_dude", doLine(m)) &
        Hide(m.guid) & FadeIn(m.guid, TimeSpan(0),TimeSpan(500)))
    }

    case v: Vector[Message] => msgs = v; reRender()
  }

  def render = "ul [id]" #> "ul_dude" & "li" #> msgs.map(doLine)

  private def doLine(m: Message) = {
    <li id={m.guid}>{m.msg} 
    {SHtml.ajaxButton("delete", () => {
      ChatServer ! Remove(m.guid)
      Noop
    })}</li>
  }

  override def fixedRender = {
    <lift:form>
    {
      SHtml.text("", s => {
        ChatServer ! s
        SetValById("chat_box", "")
      }, "id" -> "chat_box")
    }
    <input type="submit" value="Chat"/>
    </lift:form>
  }
}
