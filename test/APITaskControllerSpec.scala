package controllers.api

import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._
import play.api.db.Databases
import play.api.db.evolutions._
import repositories.TaskRepository
import org.scalatestplus.play.guice._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.{ AbstractController, ControllerComponents }
import scala.concurrent.ExecutionContext.Implicits.global



class APITaskControllerSpec extends PlaySpec with GuiceOneAppPerSuite {

  val database = Databases(
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql://localhost:5432/scala_todo_test"
  )
  Evolutions.cleanupEvolutions(database)
  Evolutions.applyEvolutions(database)

  val injector = new GuiceApplicationBuilder().injector()
  val controllerComponents = injector.instanceOf[ControllerComponents]

  def taskService: TaskRepository = new TaskRepository(database)
  def taskController: APITaskController = new APITaskController(taskService, controllerComponents)


  "APITaskController index" should {

    "render the index view" in {
      val indexView = taskController.index().apply(FakeRequest(GET, "/api/tasks"))
      status(indexView) mustBe OK
      contentType(indexView) mustBe Some("application/json")
      contentAsString(indexView) must equal("[]")
    }
  }

  "TaskController create" should {

    "successfully create a task and redirect to the index view" in {
      val result = taskController.create(
        FakeRequest(POST, "/api/tasks").withFormUrlEncodedBody("name" -> "Test Task", "comments" -> "Test Comments", "completed" -> "false")
      )
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      val bodyText: String = contentAsString(result)
      bodyText must equal("""{"id":null,"name":"Test Task","comments":"Test Comments","completed":false}""")

      // verify new task shows up on index view
      val list = taskController.index()(FakeRequest())
      status(list) must equal(OK)
      contentType(list) mustBe Some("application/json")
      val listText: String = contentAsString(list)
      listText must equal("""[{"id":1,"name":"Test Task","comments":"Test Comments","completed":false}]""")
    }

    "fail trying to create a task with no data passed" in {
      val badResult = taskController.create(FakeRequest())
      status(badResult) must equal(BAD_REQUEST)
    }
  }

  "TaskController delete" should {

    "successfully delete a task and redirect to the index view" in {
      val result = taskController.delete(1).apply(FakeRequest(POST, "/api/tasks/1/delete"))
      status(result) mustBe OK
      contentAsString(result) must equal("DELETED")

      // verify task does not show up on index view
      val list = taskController.index()(FakeRequest())
      status(list) must equal(OK)
      contentAsString(list) must equal("[]")
    }
  }

}
