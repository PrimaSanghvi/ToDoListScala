
import org.scalatestplus.play._
import play.api.db.Databases
import play.api.db.evolutions._
import scala.concurrent.ExecutionContext.Implicits.global
import repositories.TaskRepository



class TaskRepositorySpec extends PlaySpec {

  val database = Databases(
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql://localhost:5432/scala_todo_test"
  )
  Evolutions.cleanupEvolutions(database)
  Evolutions.applyEvolutions(database)

  def taskService: TaskRepository = new TaskRepository(database)
//  def taskService: TaskRepository = app.injector.instanceOf(classOf[TaskRepository])


  "TaskRepository" should {

    val task1 = models.Task(None: Option[Long], "Task One", "This is a comment", false)
    val task2 = models.Task(None: Option[Long], "Task Two", "Another comment", false)
    val task3 = models.Task(None: Option[Long], "Task Three", "Third comment", true)

    taskService.create(task1)
    taskService.create(task2)
    taskService.create(task3)

    "return all tasks" in {
      val allTasks = taskService.all()
      allTasks must have length(3)
      allTasks(0).name must equal("Task One")
      allTasks(0).comments must equal("This is a comment")
      allTasks(0).completed must equal(false)
      allTasks(1).name must equal("Task Two")
      allTasks(1).comments must equal("Another comment")
      allTasks(1).completed must equal(false)
      allTasks(2).name must equal("Task Three")
      allTasks(2).comments must equal("Third comment")
      allTasks(2).completed must equal(true)
    }

    "retrieve a task by id" in {
      val aTask = taskService.getById(1L)
      val task = aTask.get
      task.name must equal("Task One")
      task.comments must equal("This is a comment")
      task.completed must equal(false)
    }

    "create a task" in {
      val beforeTasks = taskService.all()
      beforeTasks must have length(3)

      val newTask = models.Task(None: Option[Long], "New Task", "A new comment", false)
      taskService.create(newTask)

      val afterTasks = taskService.all()
      afterTasks must have length(4)
    }

    "update a task" in {
      val aTask = taskService.getById(1L)
      val task = aTask.get.copy(name="Updated Name")
      taskService.update(task.id.get, task)

      val afterTask = taskService.getById(1L)
      val bTask = afterTask.get
      bTask.name must equal("Updated Name")
    }

    "delete a task" in {
      val beforeTasks = taskService.all()
      beforeTasks must have length(4)

      taskService.delete(2L)

      val afterTasks = taskService.all()
      afterTasks must have length(3)
    }

  }
}
