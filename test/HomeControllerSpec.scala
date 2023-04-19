package controllers

import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._
import repositories.TaskRepository
import org.scalatestplus.play.guice._


class HomeControllerSpec extends PlaySpec with GuiceOneAppPerSuite {

  def homeController = app.injector.instanceOf(classOf[HomeController])

  "HomeController GET" should {

    "redirect index page" in {
      val home = homeController.index().apply(FakeRequest(GET, "/"))
      status(home) mustBe 303
    }

  }

}
