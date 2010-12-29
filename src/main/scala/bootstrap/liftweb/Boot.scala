package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
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


//    List().map(_.toMenu)

    // Build SiteMap
    def sitemap = SiteMap(
      Menu.i("Home") / "index" >> User.AddUserMenusAfter, // the simple way to declare a menu
      
      // Menu.i("dev") / "dev" / "dog" ,
      Menu(new DevLoc),
//       Menu.param[PageData]("dev", "dev", x => Full(new PageData(x)), _ => "dev") / "dev" / ** ,

      // more complex because this menu allows anything in the
      // /static path to be visible
      Menu(Loc("Static", Link(List("static"), true, "/static/index"), 
	       "Static Content")))

    def sitemapMutators = User.sitemapMutator

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMapFunc(() => sitemapMutators(sitemap))

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

    // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)
  }
}

class DevLoc extends Loc[PageData] {
  // the name of the page
  def name = "Dev"
        
  // the default parameters (used for generating the menu listing)
  def defaultValue = Empty

  // no extra parameters
  def params = Nil

  /**
   * What's the text of the link?
   */
  def text = "dev"

  override def overrideValue = Full(PageData("foo"))

  lazy val link = new Loc.Link[PageData](List("dev"), true)

  /*
        val link = new Loc.Link[T](ParamMenuable.this.path, 
                                   ParamMenuable.this.headMatch) {
          override def createLink(in: T) = 
            Full(Text(ParamMenuable.this.path.mkString("/", "/", "/")+
                      urlEncode(ParamMenuable.this.encoder(in))))
        }

        /**
         * Rewrite the request and emit the type-safe parameter
         */
        override val rewrite: LocRewrite =
          Full(NamedPF("Wiki Rewrite") {
            case RewriteRequest(ParsePath(x, _, _,_), _, _) 
            if x.dropRight(1) == ParamMenuable.this.path && 
            x.takeRight(1).headOption.flatMap(a => 
              ParamMenuable.this.parser(a)).isDefined
            =>
              (RewriteResponse(x.dropRight(1)), 
               x.takeRight(1).headOption.flatMap(a => 
                 ParamMenuable.this.parser(a)).get) 
              
          })
  */
}
