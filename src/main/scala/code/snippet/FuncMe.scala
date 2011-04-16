package code
package snippet

import net.liftweb._
import http._
import util._
import Helpers._

/**
 * Attach a function to the uploaded file.
 */
object FuncMe {
  def render = "type=file [name]" #> 
  SHtml.fileUpload(fph => println("Got a file "+fph.fileName)).attribute("name").get
}
