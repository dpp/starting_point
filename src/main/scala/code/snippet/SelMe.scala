package code
package snippet

import net.liftweb._
import util._
import Helpers._
import scala.xml._

class SelMe {
  def render = "tbody *" #> ((ns: NodeSeq) => (List(1,2,3).flatMap(i =>
    (".name *" #> i & ".programrow" #> List("A", "B", "C").map(j => ".program *" #> j)).apply(ns))))
}
