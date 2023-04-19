package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import models.Task
import repositories.TaskRepository

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's Task page.
 */
@Singleton
class TaskController @Inject()(taskService: TaskRepository, val cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  val Home = Redirect(routes.TaskController.index)

  val taskForm = Form(
    mapping(
      "id" -> ignored(None: Option[Long]),
      "name" -> nonEmptyText,
      "comments" -> text,
      "completed" -> boolean
    )(Task.apply)(Task.unapply)
  )

  def index = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.task.index(taskService.all(), taskForm))
  }

  def create = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.task.index(taskService.all(), errors)),
      task => {
        taskService.create(task)
        Home.flashing("success" -> "Task %s has been created".format(task.name))
      }
    )
  }

  def edit(id: Long) = Action { implicit request =>
    val atask = taskService.getById(id)
    val task = atask.get
    Ok(views.html.task.edit(task.id.get, taskForm.fill(task)))
  }

  def update(id: Long) = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.task.edit(id, formWithErrors))
      },
      task => {
        taskService.update(id, task)
        Redirect(routes.TaskController.index)
      }
    )
  }

  def delete(id: Long) = Action { implicit request: Request[AnyContent] =>
    taskService.delete(id)
    Redirect(routes.TaskController.index)
  }
}
