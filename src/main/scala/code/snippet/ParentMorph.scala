package code.snippet

/**
 * Created by IntelliJ IDEA.
 * User: dpp
 * Date: 2/3/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */

import net.liftweb._
import util._
import Helpers._
import scala.xml._

class ParentMorph {
  def testNode(ns: NodeSeq, cssSel: String): Boolean = {
    var ret = false // does the NodeSeq have any nodes that match the CSS Selector

    (cssSel #> ((ignore: NodeSeq) => {ret = true; NodeSeq.Empty}))(ns)

    ret
  }

  def render =
  "ul [class+]" #>
    (((ns: NodeSeq) => Some("has_mondo").filter(ignore => testNode(ns, ".mondo"))): IterableFunc)
}