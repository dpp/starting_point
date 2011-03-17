package code
package snippet

import net.liftweb._
import util._
import http._
import common._

class UploadScreen extends LiftScreen {

  val upload = new Field {
    /**
     * Set to true if this field is part of a multi-part mime upload
     */
    override def uploadField_? = true

    type ValueType = Array[Byte]

    def manifest = scala.Predef.manifest[Array[Byte]]

    def name = "Upload"

    def default = new Array[Byte](0)

    override def toForm = Full(SHtml.fileUpload(fph => set(fph.file)))

  }

  def finish() {
    S.notice("Finished "+upload.get.length+" bytes")
  }
}
