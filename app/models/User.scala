package models

case class User(
                 id: Long,
                 fullname: String,
                 password: String,
                 email: String,
                 isAdmin: Boolean
               )
