package code
package snippet

import net.liftweb._
import http._
import js._
import JsCmds._
import util._
import Helpers._

import scala.xml.NodeSeq

object ShowButton {
  def render(in: NodeSeq): NodeSeq = {
    bind("form", in,
         "button" -> SHtml.ajaxButton("button", 
                                      () =>{
                                        println("clicked"); 
                                        Alert("clicked")}))
}
}
