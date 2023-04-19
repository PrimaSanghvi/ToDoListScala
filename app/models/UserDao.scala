package models

import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.db.DBApi
import play.api.Play.current
import play.api.data._
import play.api.mvc._

@javax.inject.Singleton
class UserDao @Inject()(database: Database) (implicit ec: ExecutionContext){

 private val DB = database
    val user ={
        get[String]("username") ~
        get[String]("password") map{
            case username ~ password => User(username , password)
        }
    }


    def lookupUser(username: String): Option[User] = {
        //TODO query your database here
        // print(u.username)
        // if (u.username == "foo" && u.password == "foo") true else false
        DB.withConnection{implicit c =>
          SQL(""" select * from 
          "user" 
          where username = {username}""").on('username -> username).as(user.singleOpt)

        }
    }
def create(user: User) {
   DB.withConnection { implicit c =>
      SQL("""
        insert into "user" (username, password) values (
          {username}, {password}
        )
        """).bind(user).executeUpdate()
    
    }

  }

}


