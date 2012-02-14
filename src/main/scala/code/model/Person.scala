package code.model

/**
 * Created by IntelliJ IDEA.
 * User: dpp
 * Date: 2/14/12
 * Time: 10:51 AM
 * To change this template use File | Settings | File Templates.
 */

import net.liftweb.mapper._
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.actor._

case class SavedPerson(id: Long)

/**
 * The singleton that has methods for accessing the database
 */
object Person extends Person with LongKeyedMetaMapper[Person] with LiftActor with ListenerManager with CRUDify[Long, Person] {
  override def fieldOrder = List(id, name, age)

  override def afterSave = sendToActor _ :: super.afterSave

  def sendToActor(p: Person) {
    DB.appendPostTransaction(saved => if (saved) this ! SavedPerson(p.id))
  }

  def createUpdate = lastId.map(SavedPerson(_)) openOr Nil

  @volatile private var lastId: Box[Long] = Empty

  override def lowPriority = {
    case s@SavedPerson(id) => lastId = Full(id); updateListeners(s)
  }
}

/**
 * An O-R mapped "User" class that includes first name, last name, password and we add a "Personal Essay" to it
 */
class Person extends LongKeyedMapper[Person] with IdPK {
  def getSingleton = Person // what's the "meta" server

  object name extends MappedPoliteString(this, 32) {
    override def validations = valMinLen(4, "Must be 4 chars") _ :: super.validations
  }
  object age extends MappedInt(this) {

    def valMin(min: Int, msg: => String)(test: Int): List[FieldError] = {
    if (min <= test) Nil else
      List(FieldError(this, xml.Text(msg)))
    }

    override def validations = valMin(16, "You must be 16") _ :: super.validations
  }
}