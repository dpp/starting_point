package code.lib

/**
 * Created by IntelliJ IDEA.
 * User: dpp
 * Date: 2/6/12
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */

import net.liftweb._
import http._
import sitemap._
import common._
import scala.xml._
import sitemap.Menu.ParamsMenuable._

case class Album(name: String)

case class Foto(album: Album, name: String)

case class Topic(album: Album, name: String)

object AlbumMenu {
  // define the foto menu
  lazy val foto = Menu.params[Foto]("Foto", "Foto", {
    case album :: name :: Nil => Full(Foto(Album(album), name))
    case _ => Empty
  }, a => List(a.name)) / "foto" / * / *

  // define the topic menu
  lazy val topic = Menu.params[Topic]("Topic", "Topic", {
    case album :: name :: Nil => Full(Topic(Album(album), name))
    case _ => Empty
  }, a => List(a.name)) / "topic" / * / *

  // define the album which has topic and foto as children
  lazy val album = Menu.params[Album]("Album", Loc.LinkText(album =>
    Text("Album " + album.name)), {
    case name :: Nil => Full(Album(name))
    case _ => Empty
  }, a => List(a.name)) / "album" >> Loc.CalcValue(currentAlbumValue) submenus (topic, foto)

  // based on the current location, extract the album out of potential sub-menus
  def currentAlbumValue(): Box[Album] =
    for {
      loc <- S.location
      res <- loc.currentValue match {
        case Full(Foto(album, _)) => Full(album)
        case Full(Topic(album, _)) => Full(album)
        case _ => Empty
      }
    } yield res
}
