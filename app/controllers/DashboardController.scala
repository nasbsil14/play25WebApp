package controllers

import javax.inject._

import dao.UserDao
import models.User
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

import scala.concurrent.Future

@Singleton
class DashboardController @Inject()(dao: UserDao) extends Controller {

  val Home = Redirect(routes.DashboardController.top())

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
    resultUser.map(user => Ok(views.html.detail(id, userForm.fill(user.getOrElse(User(Long.MaxValue, "", "", "", false))))))
  }

  def insert = Action.async {implicit rs =>
    userForm.bindFromRequest.fold(
      formWithErrors => Future(BadRequest(views.html.detail(Long.MaxValue, formWithErrors))),
      user => dao.insert(user).map(id => Home.flashing("success" -> "New User created id:%s".format(id)))
      )
  }

  def update(id: Long) = Action.async {implicit rs =>
    userForm.bindFromRequest.fold(
      formWithErrors => Future(BadRequest(views.html.detail(id, formWithErrors))),
      user => dao.update(id, user).map(_ => Home.flashing("success" -> "User updated id:%s".format(id)))
      )
  }

  def delete(id: Long) = Action.async { implicit rs =>
    dao.delete(id).map(_ => Home.flashing("success" -> "User deleted id:%s".format(id)))
  }
}
