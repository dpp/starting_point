package code.comet

import code.model._
import code.snippet._
import net.liftweb._
import common._
import http._
import util._
import Helpers._
import js._
import JsCmds._
import JE._
import js.jquery._
import JqJsCmds._

/**
 * Created by IntelliJ IDEA.
 * User: dpp
 * Date: 2/14/12
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */

class PersonComet extends CometActor with CometListener {
  private var lastId: Box[Long] = Empty
  def registerWith = Person

  override def lowPriority = {
    case SavedPerson(id) => lastId = Full(id); reRender()
  }

  def render = lastId.flatMap(Person.find) match {
    case _: EmptyBox => <i>Nobody</i>
    case Full(person) =>
      CurrentPerson.set(Full(person))
      val id = person.id.is
      <xml:group>
        The Person: {person.name.is}
        {SHtml.ajaxButton("Edit 1", () => ModalDialog(<lift:EditPerson1/>))}
        {SHtml.ajaxButton("Edit 2", () => ModalDialog(<lift:EditPerson2 ajax="true"/>))}
      </xml:group>
  }
}