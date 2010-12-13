package code
package comet

import net.liftweb._
import http._
import util._
import Helpers._

class Clock extends CometActor {
  override def lowPriority = {
    case Ping() => reRender(); schedulePing()
  }

  private def schedulePing() {
    ActorPing.schedule(() => this ! Ping(), 10 seconds)
  }

  schedulePing()

  def render = <b>The time is {new java.util.Date}</b>
}

final case class Ping()
