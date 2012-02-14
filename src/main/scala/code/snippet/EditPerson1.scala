package code.snippet


import code.model._
import net.liftweb._
import http._
import util._
import Helpers._
import common._
import js._
import JsCmds._
import JE._
import js.jquery._
import JqJsCmds._

/**
 * Created by IntelliJ IDEA.
 * User: dpp
 * Date: 2/14/12
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */

object CurrentPerson extends RequestVar[Box[Person]](Empty)

class EditPerson1 {
  val thePerson = CurrentPerson.is openOr Person.create

  def doSave(): JsCmd = {
    thePerson.validate match {
      case Nil => thePerson.saveMe; Unblock
      case errors => S.error(errors); Replace("show_me", render)
    }
  }

  def render = <div id="show_me" data-lift="form.ajax">

    Name:
    {SHtml.text(thePerson.name.is, thePerson.name.set)}<br/>
    Age:
    {SHtml.text(thePerson.age.is.toString, str => asInt(str).foreach(thePerson.age.set))}<br/>
    {SHtml.hidden(doSave)}{SHtml.submit("Save", () => {})}
  </div>
}

class EditPerson2 extends LiftScreen {

  object MyPerson extends ScreenVar(CurrentPerson.is openOr Person.create)

  override def localSetup() {
    super.localSetup()
    MyPerson.set(CurrentPerson.is openOr Person.create)
  }

  addFields(() => MyPerson.is)

  def finish() {
    MyPerson.is.save
  }

  override def calcAjaxOnDone = Unblock
}