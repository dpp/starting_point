package code
package model

import net.liftweb._
import mapper._
import util._
import common._
import sitemap._
import Loc._

class Frog extends LongKeyedMapper[Frog] with IdPK {
  def getSingleton = Frog

  object name extends MappedString(this, 50)

  object owner extends MappedLongForeignKey(this, User) {
    // we use open_! here because the ability to create a new
    // record depends on being logged in, so setting the default
    // will intentionally result in an exception being thrown
    // if no user is logged in
    override def defaultValue = User.currentUser.open_!.id.is
  }
}

object Frog extends Frog with LongKeyedMetaMapper[Frog] with
CRUDify[Long, Frog] 
{
  override def createMenuLocParams: List[Loc.AnyLocParam] = 
    List(If(User.loggedIn_? _, "Not logged in"))
  
  override def findForList(start: Long, cnt: Int): List[Frog] = 
    findAll(StartAt(start), MaxRows(cnt), By(owner, User.currentUser))


  override def findForParam(in: String): Box[Frog] = 
    for {
      user <- User.currentUser
      id <- Helpers.asLong(in)
      frog <- find(By(this.id, id), By(owner, user))
    } yield frog
}

