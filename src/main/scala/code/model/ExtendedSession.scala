package code
package model

import net.liftweb._
import mapper._
import common._

class ExtendedSession extends LongKeyedMapper[ExtendedSession] with ProtoExtendedSession[ExtendedSession] {
  def getSingleton = ExtendedSession
}

object ExtendedSession extends ExtendedSession with 
LongKeyedMetaMapper[ExtendedSession] with
MetaProtoExtendedSession[ExtendedSession] {
  def recoverUserId: Box[String] = User.currentUserId

  type UserType = User

  def logUserIdIn(uid: String) {
    User.logUserIdIn(uid)
  }
}

