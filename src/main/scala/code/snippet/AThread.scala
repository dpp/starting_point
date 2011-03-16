package code
package snippet

import model._

import net.liftweb._
import common._
import util._
import Helpers._
import sitemap._

object AThread {
  lazy val menu = 
    Menu.params[(Forum, FThread)]("Thread", "Thread", 
                                  {
                                    case Forum(fid) :: 
                                    FThread(tid) :: _ =>
                                      Full((fid, tid))
                                    case _ => Empty
                                  },
                                  ft => List(ft._1.id.toString,
                                             ft._2.id.toString)
                                ) / "forums" / * / *
}

class AThread(ft: (Forum, FThread)) {
  
  def render = <div>Welcome to Forum {ft._1.id} and Thread {ft._2.id}</div>
}
