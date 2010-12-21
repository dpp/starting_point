package code
package comet

import net.liftweb._
import http._
import common._
import util._
import Helpers._
import js.JsCmds._

import scala.xml.NodeSeq

private object userPage extends SessionVar("login")

/**
 * ChangeBlock is a comet block that the app can use to show whatever
content it needs to display.
 */
class Froo extends CometActor
{
  //override def defaultPrefix = Full("cBlock")

 /**
  * Renders an initial value (template-login).
  */
 def render = "#primaryContent *" #> pageContent


 def pageContent = <lift:embed what={"/template-" + userPage}/>

 /**
  * The handler for pageUpdate events.
  */
 private def pageUpdateHandler(event: String): Unit = {
   println("[ChangeBlock.pageUpdateHandler] enter.")

   val temp = event
   println("[ChangeBlock.pageUpdateHandler] new page is: " + temp)
   userPage.set(temp)

   // We have to do the actual update in one of the actor event handling routines.
   ActorPing.schedule(this, CBTick, shortTick)

   println("[ChangeBlock.pageUpdateHandler] exit.")
 }

 def shortTick = 3.seconds

 /**
  * The actor event handler that sends the actual page change to the client.
  */
 override def lowPriority = {
   case CBTick => {
     println("[ChangeBlock.lowPriority] Got CBTick " + new java.util.Date())
     partialUpdate(SetHtml("primaryContent", pageContent))
     ActorPing.schedule(this, CBTick, shortTick)
   }
 }

  ActorPing.schedule(this, CBTick, shortTick)
}

/**
 * A utility object for sending events to the comet event handling
part of the actor.
 */
case object CBTick
