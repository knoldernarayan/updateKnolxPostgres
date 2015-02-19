package com.knol.core
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import com.knol.db.connection.DBConnection
import scala.slick.driver.PostgresDriver.simple._
import java.util.Date
import java.text.SimpleDateFormat
import scala.slick.lifted.ProvenShape
class SessionRepoTest extends FunSuite with BeforeAndAfter with DBConnection with SessionRepo {

  val dbOject = getObject()
  before {
    dbObject.withSession { implicit session =>
      (knolSessionTable.ddl ++ knolderTable.ddl).create
      createSession(KnolSession(1, "java", new Date(),1))
       createKnol(Knolder(1, "narayan", "narayan@knoldus.com", "12344567"))
    }
  }
  after {
    dbObject.withSession { implicit session =>
      (knolSessionTable.ddl ++ knolderTable.ddl).drop
    }
  }

  test("Create a knolx") {
    val knolx = KnolSession(1, "scala", new Date(), 1234)
    assert(createSession(knolx) === Some(2))
  }
  test("Update a knolx") {
    val knolx = KnolSession(1, "c++", new Date(), 123445)
    assert(update(knolx) === Some(1))
  }
  test("Delete a knolx") {
    assert(delete(1) === Some(1))
  }
  test("getKnolx a knolx") {
    val result = getKnolSession(1)
    assert(result.iterator.size === 1)
  }
  test("Test for getJoinSession"){
    val result = getJoinKnolSession()
    assert(result.iterator.size===1)
  }
}