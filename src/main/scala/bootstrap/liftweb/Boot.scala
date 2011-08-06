package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import js.jquery.JQuery14Artifacts
import sitemap._
import Loc._
import mapper._

import code.model._

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor =
        new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
          Props.get("db.url") openOr
            "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
          Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }

    // Use Lift's Mapper ORM to populate the database
    // you don't need to use Mapper to use Lift... use
    // any ORM you want
    Schemifier.schemify(true, Schemifier.infoF _, User)

    // where to search snippet
    LiftRules.addToPackages("code")

    // Build SiteMap
    def sitemap = SiteMap(
      Menu.i("Home") / "index" >> User.AddUserMenusAfter, // the simple way to declare a menu

      // more complex because this menu allows anything in the
      // /static path to be visible
      Menu(Loc("Static", Link(List("static"), true, "/static/index"),
        "Static Content")))

    def sitemapMutators = User.sitemapMutator

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMapFunc(() => sitemapMutators(sitemap))

    LiftRules.jsArtifacts = JQuery14Artifacts

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    FileUpload.init()

    LiftRules.progressListener = {
      val opl = LiftRules.progressListener
      val ret: (Long, Long, Int) => Unit =
        (a, b, c) => {
          // println("progress listener "+a+" plus "+b+" "+c)
          // Thread.sleep(100) -- demonstrate slow uploads
          opl(a, b, c)
        }
      ret
    }

    // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)
  }
}

import rest._
import json._
import JsonDSL._
object FileUpload extends RestHelper with Logger {
  serve {
    case "upload" :: "thing" :: Nil Post req => {
      val uploads = req.uploadedFiles
      debug("Uploaded files: " + uploads)
      val ojv: List[JObject] =
        uploads.map(fph =>
          ("name" -> fph.fileName) ~
            ("sizef" -> fph.length) ~
            ("delete_url" -> "/delete/thing") ~
            ("delete_type" -> "DELETE"))

      // run callbacks
      //      S.session.map(_.runParams(req))
      // This is a tad bit of a hack, but we need to return text/plain, not JSON
      val jr = JsonResponse(ojv).toResponse.asInstanceOf[InMemoryResponse]
      InMemoryResponse(jr.data, ("Content-Length", jr.data.length.toString) ::
        ("Content-Type", "text/plain") :: S.getHeaders(Nil),
        S.responseCookies, 200)
    }

    case "delete" :: "thing" :: Nil Delete req => {
      debug("TODO: got a delete request, handle it!")
      OkResponse()
    }

  }

  def init() = {
    //rewrite so the rest-callback will be a param instead to be fired with LiftSession.runParams
    LiftRules.statelessRewrite.append {
      case RewriteRequest(ParsePath("upload" :: "thing" :: callback :: Nil, "", true, _), _, _) =>
        RewriteResponse("upload" :: "thing" :: Nil, Map("callback" -> "_"))
    }

    LiftRules.dispatch.append(this)
  }
}
