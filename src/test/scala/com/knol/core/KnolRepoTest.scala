package com.knol.core

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import com.knol.db.connection.DBConnection
import scala.slick.driver.PostgresDriver.simple._
import java.util.Date
import java.text.SimpleDateFormat
import scala.slick.lifted.ProvenShape

class KnolRepoTest extends FunSuite with BeforeAndAfter with DBConnection with KnolRepo {
  /**
   *
   */
  val dbOject = getObject()
  before {
    dbObject.withSession { implicit session =>
      (knolderTable.ddl).create
      createKnol(Knolder(1, "narayan", "narayan@knoldus.com", "12344567"))
    }
  }
  after {
    dbObject.withSession { implicit session =>
      (knolderTable.ddl).drop
    }
  }

  test("Create a knol") {
    val knol = Knolder(1, "shyam", "narayan@knoldus.com", "vvmvsv")
    assert(createKnol(knol) === Some(2))
  }
  test("Update a knol") {
    val knol = Knolder(1, "teena", "narayan@knoldus.com", "12344567")
    assert(update(knol) === Some(1))
  }
  test("Delete a knol") {
    assert(delete(1) === Some(1))
  }
  test("getKnol a knol") {
    val result = getKnol(1)
    assert(result.iterator.size === 1)
  }
}