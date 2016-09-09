package dao

import scala.concurrent.{Await, Future}
import javax.inject.Inject

import models.User
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.duration.Duration

class UserDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  private val Users = TableQuery[UsersTable]

  def all(): Future[Seq[User]] = db.run(Users.result)

  def findById(id: Long): Future[Option[User]] = db.run(Users.filter(_.id === id).result.headOption)

  def insert(user: User): Future[Long] = {
    val currentId = this.all().map(_.map(_.id).max)
    for {
      id <- currentId
      _ <- db.run(Users += user.copy(id + 1))
    } yield {
      id
    }
  }

  def update(id: Long, upUser: User): Future[Unit] = {
    //val userToUpdate: User = upUser.copy(id)
    db.run(Users.filter(_.id === id).update(upUser)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] =
    db.run(Users.filter(_.id === id).delete).map(_ => ())

  private class UsersTable(tag: Tag) extends Table[User](tag, "USER") {
    def id = column[Long]("ID", O.PrimaryKey)
    def fullname = column[String]("FULLNAME")
    def password = column[String]("PASSWORD")
    def email = column[String]("EMAIL")
    def isAdmin = column[Boolean]("ISADMIN")

    def * = (id, fullname, password, email, isAdmin) <> (User.tupled, User.unapply _)
  }
}

