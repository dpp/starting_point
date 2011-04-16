package code
package snippet

import net.liftweb.common.Box
import lib.DependencyFactory
import java.util.Date
import net.liftweb.util.Helpers
import Helpers._

class HelloWorld {
  lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

  // replace the contents of the element with id "time" with the date
  def howdy = "#time *" #> date.map(_.toString)
}
