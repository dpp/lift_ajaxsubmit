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

object user extends SessionVar[User](User.currentUser.get)

class Users {
    def render(xhtml: NodeSeq): NodeSeq = {
        User.findAll.flatMap(u => 
       	   bind ("c", xhtml,
  	         "name" -> Text(u.niceName),
       		 "edit" -> SHtml.a(() => { user.set(u);S.runTemplate(List("_edit")).map(ns => ModalDialog(ns)) openOr Alert("Error") }, Text("Edit"))))
    }
}

class Edit extends StatefulSnippet {
    
    def dispatch = {
        case _ => render _
    }
    
    def render (xhtml: NodeSeq): NodeSeq = {
        def doSave() = {
        	user.is.save
        	redirectTo("index.html")
        }
        
        bind("form", xhtml,
             "firstName" -> SHtml.text(user.is.firstName.toString, v => user.is.firstName(v)),
             "lastName" -> SHtml.text(user.is.lastName.toString, v => user.is.lastName(v)),
             "save" -> SHtml.ajaxSubmit("Save", doSave),
             "close" -> SHtml.ajaxButton(Text("Close"), () => Unblock))
    }
}

