package code.snippet

/**
 * Created by IntelliJ IDEA.
 * User: dpp
 * Date: 10/18/11
 * Time: 11:19 AM
 * To change this template use File | Settings | File Templates.
 */

import net.liftweb._
import http._
import common._
import scala.xml._

class UploadScreen extends LiftScreen {
  override protected def hasUploadField = true

  val name = field("Name", "")
  val file = makeField[Array[Byte], Nothing]("File", new Array[Byte](0),
    field => SHtml.fileUpload(fph => field.set(fph.file)),
    NothingOtherValueInitializer)

  def finish() {
    S.notice("Thanks for uploading a file of " + file.get.length + " bytes ")
  }
}