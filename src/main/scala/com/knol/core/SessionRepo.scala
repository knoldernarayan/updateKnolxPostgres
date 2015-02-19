package com.knol.core
import com.knol.db.connection._
import scala.collection.mutable.MutableList
import scala.slick.driver.PostgresDriver.simple._
import java.util.Date
import java.text.SimpleDateFormat
import scala.slick.lifted.ProvenShape
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
/**
 * --------------------------------------------------------------------------------------------
 * This is a SessionRepo trait which extends DBConnection class and implements CRUD operations.
 * --------------------------------------------------------------------------------------------
 */
trait SessionRepo extends DBConnection with KnolRepo {
  class KnolSessionTable(tag: Tag) extends Table[KnolSession](tag, "knolSession") {
    def id: Column[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def topic: Column[String] = column[String]("topic", O.NotNull)
    def knolx_Date: Column[Date] = column[Date]("knolx_Date", O.NotNull)
    def knol_Id: Column[Int] = column[Int]("knol_Id", O.NotNull)
    def * = (id, topic, knolx_Date, knol_Id) <> (KnolSession.tupled, KnolSession.unapply)
  }

  val knolSessionTable = TableQuery[KnolSessionTable]
  def knolSessionTableAutoInc = knolSessionTable returning knolSessionTable.map(_.id)
  val dbObjectSes = getObject()
  /**
   * -------------------------------------------------------------------------------------------
   * This is createSession method which takes KnolSession's object and returns AutoIncrement id.
   * -------------------------------------------------------------------------------------------
   */
  def createSession(knolx: KnolSession): Option[Int] = {
    dbObjectSes.withSession { implicit session =>
      val result = knolSessionTableAutoInc.insert(knolx)
      Some(result)
    }
  }
  /**
   * ------------------------------------------------------------------------------------------------------
   * This is update method which takes KnolSession's object as argument and returns number of updated rows.
   * ------------------------------------------------------------------------------------------------------
   */
  def update(knolx: KnolSession): Option[Int] = {
    dbObjectSes.withSession { implicit session =>
      val result = knolSessionTable.filter(_.id === knolx.knolSessionId).update(knolx)
      Some(result)
    }
  }
  /**
   * --------------------------------------------------------------------------------------------------
   * This is delete method which takes KnolSession's id as argument and returns number of deleted rows.
   * --------------------------------------------------------------------------------------------------
   */
  def deleteSession(id: Int): Option[Int] = {
    dbObjectSes.withSession { implicit session =>
      val result = knolSessionTable.filter(_.id === id).delete
      Some(result)
    }
  }
  /**
   * --------------------------------------------------------------------------------------------------------
   * This is getKnolSession method which takes KnolSession's id as argument and returns KnolSession's object.
   * --------------------------------------------------------------------------------------------------------
   */
  def getKnolSession(id: Int): Option[List[KnolSession]] = {
    dbObjectSes.withSession { implicit session =>
      val result = knolSessionTable.filter(_.id === id).list
      Some(result)
    }
  }
  def getJoinKnolSession(): List[KnolJoinSession] = {
    dbObjectSes.withSession { implicit session =>
      val result = knolSessionTable innerJoin knolderTable on (_.knol_Id === _.id) list
      val resultList = result.map {
        case (knolSessionTable, knolderTable) => KnolJoinSession(knolSessionTable.knolSessionId, knolSessionTable.knolId, knolderTable.name, knolSessionTable.topic, knolSessionTable.date)
      }
      resultList
    }

  }
}
/**
 * -------------------------------------------------------------------------
 * This is KnolSession case class which is use for create KnolSession object
 * -------------------------------------------------------------------------
 */
case class KnolSession(val knolSessionId: Int, val topic: String, val date: Date, val knolId: Int)
/**
 * ------------------------------------------------------------------------------------------------
 * This is KnolJoinSession case class which is use for Join query to create KnolJoinSession object.
 * ------------------------------------------------------------------------------------------------
 */
case class KnolJoinSession(val id: Int, val knolId: Int, val name: String, val topic: String, val date: Date)