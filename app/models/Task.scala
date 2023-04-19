package models

import anorm._
import play.api.libs.json._


case class Task(
    id: Option[Long] = None,
    name: String,
    comments: String,
    completed: Boolean)

object Task {
  implicit def toParameters: ToParameterList[Task] = Macro.toParameters[Task]

  /**
    * Mapping to write a Task out as a JSON value.
    */
  implicit val implicitWrites = new Writes[Task] {
    def writes(task: Task): JsValue = {
      Json.obj(
        "id" -> task.id,
        "name" -> task.name,
        "comments" -> task.comments,
        "completed" -> task.completed
      )
    }
  }
}

class TaskId private (val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object TaskId {
  def apply(raw: String): TaskId = {
    require(raw != null)
    new TaskId(Integer.parseInt(raw))
  }
}
