package models

import anorm._
import play.api.libs.json._

case class User (
    username: String,
    password: String
)

object User{
 implicit def toParameters: ToParameterList[User] = Macro.toParameters[User]

implicit val implicitWrites = new Writes[User] {
    def writes(user: User): JsValue = {
      Json.obj(
       "username" -> user.username,
       "password" -> user.password,
      )
    }
  }
}