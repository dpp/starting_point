package code
package comet

import net.liftweb._
import http._
import common._
import util._

import js._
import JsCmds._
import JE._

import json._
import JsonDSL._

object MyJxOut extends SessionVar[Box[JxOut]](Empty)

class JxOut extends CometActor {
  private var msgs = Vector(Msg("one", "Welcome"))

  MyJxOut.set(Full(this))

  override def devMode = Props.devMode

  def drawLine = Jx(<li id={JsRaw("it.id")}>{JsRaw("it.msg")}</li>)

  def listJx = Jx(<ul id="jx_out">{
    JxMap(JsRaw("it"), drawLine)
  }</ul>)

  override def lowPriority = {
    case m @ Msg(id, msg) =>
      msgs :+= m
      partialUpdate(
        JsCrVar("lastMsg", JsObj("id" -> id,
                                "msg" -> msg)) &
        JsRaw("messages.push(lastMsg)")&
        JsRaw("jQuery('#jx_out').append(drawLine(lastMsg))"))
  }

  def render = {
    JsCrVar("renderAll", listJx.yieldFunction) &
    JsCrVar("drawLine", drawLine.yieldFunction) &
    JsCrVar("messages", JsArray(msgs.map(m =>
      JsObj("id" -> m.id, 
            "msg" -> m.msg)) :_*)) &
    OnLoad(JsRaw("drawMainArea()")) &
    JsCrVar("drawMainArea", 
            AnonFunc(JsRaw("jQuery('#mainArea').empty()."+
                           "append(renderAll(messages))")))
  }
}

case class Msg(id: String, msg: String)
