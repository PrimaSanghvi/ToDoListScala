package repositories

import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.db.DBApi
import play.api.Play.current
import models.Task


@javax.inject.Singleton
//class TaskRepository @Inject()(dbapi: DBApi)(implicit ec: ExecutionContext) {
class TaskRepository @Inject()(database: Database)(implicit ec: ExecutionContext) {


  //private val DB = dbapi.database("default")
  private val DB = database


  val task = {
    get[Option[Long]]("id") ~
    get[Boolean]("completed") ~
    get[String]("comments") ~
    get[String]("name") map {
      case id ~ completed ~ comments ~ name => Task(id, name, comments, completed)
    }
  }

  def getById(id: Long): Option[Task] = {
        DB.withConnection { implicit c =>
          SQL("select * from task where id = {id}").on('id -> id).as(task.singleOpt)
      }
  }

  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task").as(task *)
  }

  def create(task: Task) {
    DB.withConnection { implicit c =>
      SQL("""
        insert into task (name, comments, completed) values (
          {name}, {comments}, {completed}
        )
        """).bind(task).executeInsert()
    }
  }

  def update(id: Long, task: Task) {
    DB.withConnection { implicit c =>
      SQL("""
        UPDATE task SET name={name}, comments={comments}, completed={completed}
        WHERE id = {id}
        """).bind(task.copy(id = Some(id))).executeUpdate()
    }
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("""
        delete from task where id = {id}
        """).on('id -> id).executeUpdate()
    }
  }

}
