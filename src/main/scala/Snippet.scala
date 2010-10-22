/*
 * 2010 Vlad Seryakov
 * 
 */

package example.snippet

import scala.xml._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb.http.js.JE._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.jquery._
import net.liftweb.http.js.jquery.JqJsCmds._
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.mapper._
import Helpers._
import example.model._

object user extends RequestVar[User](User.currentUser.get)

class Users {
    def render(xhtml: NodeSeq): NodeSeq = {
        User.findAll.flatMap(u => 
       	   bind ("c", xhtml,
  	         "name" -> u.niceName,
       		 "edit" -> SHtml.a(() => { user.doWith(u) {
                   S.runTemplate(List("_edit")).map(ns => ModalDialog(ns)) openOr Alert("Error") }}, 
                                   Text("Edit"))))
    }
}

class Edit {
  def render (xhtml: NodeSeq): NodeSeq = {
    val u = user.is

    def doSave() = {
      u.save
      Unblock & RedirectTo("/index")
    }
    
    bind("form", xhtml,
         "firstName" -> u.firstName.toForm,
         "lastName" -> u.lastName.toForm,
         "save" -> (SHtml.hidden(doSave) ++ SHtml.submit("Save", () => {})),
         "close" -> SHtml.ajaxButton(Text("Close"), () => Unblock))
  }
}

