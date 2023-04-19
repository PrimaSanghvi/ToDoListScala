package controllers

import javax.inject.Inject
import models.{Global, User, UserDao}
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._

class UserController @Inject()(
    cc: MessagesControllerComponents,
    userDao: UserDao
) extends MessagesAbstractController(cc) {

    private val logger = play.api.Logger(this.getClass)

    val Home = Redirect(routes.TaskController.index)

    val form: Form[User] = Form (
        mapping(
            "username" -> nonEmptyText,
            "password" -> nonEmptyText
        )(User.apply)(User.unapply)
    )

    private val formSubmitUrl = routes.UserController.processLoginAttempt

    def showLoginForm = Action { implicit request: MessagesRequest[AnyContent] =>
        Ok(views.html.task.userLogin(form, formSubmitUrl))
    }

    def processLoginAttempt = Action { implicit request: MessagesRequest[AnyContent] =>
        val errorFunction = { formWithErrors: Form[User] =>
            // form validation/binding failed...
            BadRequest(views.html.task.userLogin(formWithErrors, formSubmitUrl))
        }
        val successFunction = { user: User =>
            // form validation/binding succeeded ...
            val ausername = userDao.lookupUser(user.username) 
            if (ausername != null) {
                Redirect(routes.TaskController.index)
                    .flashing("success" -> "You are logged in.")
                    .withSession(Global.SESSION_USERNAME_KEY -> user.username)
            } else {
                userDao.create(user)
                Redirect(routes.TaskController.index)
      }
        
        }
        val formValidationResult: Form[User] = form.bindFromRequest
        formValidationResult.fold(
            errorFunction,
            successFunction
        )
    }

}
