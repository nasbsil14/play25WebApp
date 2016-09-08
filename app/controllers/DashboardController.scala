package controllers

import javax.inject._

import dao.UserDao
import models.User
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

import scala.concurrent.Future

@Singleton
class DashboardController @Inject()(dao: UserDao) extends Controller {

  def top = Action {
    Ok(views.html.top())
  }

  def userList = Action.async {
    val resultUsers: Future[Seq[User]] = dao.all()
    resultUsers.map(users => Ok(views.html.list(users)))
  }

  val userForm = Form(
    mapping(
      "id" -> longNumber,
      "fullname" -> nonEmptyText,
      "password" -> nonEmptyText,
      "email" -> nonEmptyText,
      "isAdmin" -> boolean)(User.apply)(User.unapply))

  def userDetail(id: Long) = Action.async {
    val resultUser: Future[Option[User]] = dao.findById(id)
    resultUser.map(user => Ok(views.html.detail(id, userForm.fill(user.getOrElse(User(99, "", "", "", false))))))
  }
}
