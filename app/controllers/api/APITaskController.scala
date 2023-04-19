package controllers.api

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import models.Task
import repositories.TaskRepository
import play.api.libs.json.Json



/**
  * Takes HTTP requests and produces JSON.
  */
@Singleton
class APITaskController @Inject()(taskService: TaskRepository, val cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  private val logger = Logger(getClass)

  val taskForm = Form(
    mapping(
      "id" -> ignored(None: Option[Long]),
      "name" -> nonEmptyText,
      "comments" -> text,
      "completed" -> boolean
    )(Task.apply)(Task.unapply)
  )

  def index = Action { implicit request: Request[AnyContent] =>
    logger.trace("index: ")
    val tasks = taskService.all
    Ok(Json.toJson(tasks))
  }

  def show(id: Long) = Action { implicit request: Request[AnyContent] =>
    logger.trace("index: ")
    val task = taskService.getById(id)
    Ok(Json.toJson(task.get))
  }

  def create = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(errors.errorsAsJson),
      task => {
        taskService.create(task)
        Ok(Json.toJson(task))
      }
    )
  }

  def delete(id: Long) = Action { implicit request: Request[AnyContent] =>
    taskService.delete(id)
    Ok("DELETED")
  }

}
