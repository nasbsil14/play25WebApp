package dao

import scala.concurrent.Future

import javax.inject.Inject
import models.User
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

class UserDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  private val Users = TableQuery[UsersTable]

  def all(): Future[Seq[User]] = db.run(Users.result)

  def findById(id: Long): Future[Option[User]] = db.run(Users.filter(_.id === id).result.headOption)

  def insert(user: User): Future[Unit] = db.run(Users += user).map { _ => () }

  private class UsersTable(tag: Tag) extends Table[User](tag, "USER") {
    def id = column[Long]("ID", O.PrimaryKey)
    def fullname = column[String]("FULLNAME")
    def password = column[String]("PASSWORD")
    def email = column[String]("EMAIL")
    def isAdmin = column[Boolean]("ISADMIN")

    def * = (id, fullname, password, email, isAdmin) <> (User.tupled, User.unapply _)
  }
}

