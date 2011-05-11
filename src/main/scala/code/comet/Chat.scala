package code
package comet

import snippet._

import net.liftweb._
import json._
import common._
import http._
import rest._
import actor._
import util._
import Helpers._
import scala.xml._


object ChatServer extends ChatRestServer {
  protected var msgs = Vector("Welcome")
  
  def createUpdate = msgs

  override def lowPriority = ({
    case s: String => 
      msgs :+= s
    updateListeners()  
  }:  PartialFunction[Any, Unit]) orElse 
  super.lowPriority
}

class Chat extends CometActor with CometListener {
  private var msgs: Vector[String] = Vector()
  
  def registerWith = ChatServer

  override def lowPriority = {
    case v: Vector[String] => msgs = v; reRender()
  }

  def render = ClearClearable andThen 
  "li *" #> msgs.zipWithIndex.map(v =>
    "a [href]" #> ViewMenu.menu.calcHref(v) andThen
    "a *" #> v._1)
}


final case class ChatMessage(msg: String, pos: Int)
object ChatMessage {
  implicit def toCM(p: (String, Int)): ChatMessage = 
    ChatMessage(p._1, p._2)
}
final case class Messages(msgs: List[ChatMessage])
final case class GetMessagesAfter(pos: Int, f: Vector[String] => Unit)

trait ChatRestServer extends  LiftActor with ListenerManager {
  protected def msgs: Vector[String]
  private var waiting: Vector[GetMessagesAfter] = Vector()

  override def updateListeners() {
    val len = msgs.length
    waiting = waiting.flatMap {
      case GetMessagesAfter(pos, f) if pos < len => f(msgs) ; Nil
      case gma => List(gma)}
    super.updateListeners()}

  def msgAt(pos: Int) = (this !! pos).collect{case s: String => s}

  override def lowPriority = {
    case i: Int => reply(if (msgs.isDefinedAt(i)) msgs(i) else Empty)

    case gma@ GetMessagesAfter(pos, f) =>
      if (pos < msgs.length) f(msgs)
      else waiting :+= gma}}

object ChatRest extends RestHelper {
  serve {
    case "chat" :: AsInt(pos) :: Nil Get _ =>
      ChatServer.msgAt(pos).flatMap(anyToJValue)

    case "chats" :: AsInt(pos) ::  Nil Get _ => 
      RestContinuation.async {
        reply => ChatServer ! 
        GetMessagesAfter(pos, msgs => 
          reply(vToR(msgs, pos)))}}

  def vToR(v: Vector[String], off: Int): LiftResponse =
    anyToJValue(Messages(v.toList.zipWithIndex.drop(off).
                         map(v => v: ChatMessage))) match {
        case Full(jv) => jv 
        case _ => NotFoundResponse("Could not convert") }}


