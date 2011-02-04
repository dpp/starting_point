package code 
package snippet 

import net.liftweb._
import util._
import http._

object MakeOne {
  def render = <lift:comet type="Tick" name={S.param("name") openOr "Woof"}/>
}
