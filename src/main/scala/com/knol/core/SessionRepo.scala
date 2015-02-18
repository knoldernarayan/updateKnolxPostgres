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
 * This is a SessionRepo trait which defines set of methods which is going to implement by KnolSessionImpl class
 */
trait SessionRepo extends DBConnection {
  class KnolSessionTable(tag: Tag) extends Table[KnolSession](tag, "knolSession") {
    def id: Column[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def topic: Column[String] = column[String]("topic", O.NotNull)
    def knolx_Date: Column[Date] = column[Date]("knolx_Date", O.NotNull)
    def knol_Id: Column[Int] = column[Int]("knol_Id", O.NotNull)
    def * = (id, topic, knolx_Date, knol_Id) <> (KnolSession.tupled, KnolSession.unapply)
  }

  val knolSessionTable = TableQuery[KnolSessionTable]
  def knolSessionTableAutoInc = knolSessionTable returning knolSessionTable.map(_.id)
  val dbObject = getObject()
  def createSession(knolx: KnolSession): Option[Int] = {
    dbObject.withSession { implicit session =>
      val result = knolSessionTableAutoInc.insert(knolx)
      Some(result)
    }
  }
  def update(knolx: KnolSession): Option[Int] = {
    dbObject.withSession { implicit session =>
      val result = knolSessionTable.filter(_.id === knolx.knolSessionId).update(knolx)
      Some(result)
    }
  }
  def delete(id: Int): Option[Int] = {
    dbObject.withSession { implicit session =>
      val result = knolSessionTable.filter(_.id === id).delete
      Some(result)
    }
  }
  def getKnolSession(id: Int): Option[List[KnolSession]] = {
    dbObject.withSession { implicit session =>
      val result = knolSessionTable.filter(_.id === id).list
      Some(result)
    }
  }
}
/**
 * This is KnolSession case class which is use for create KnolSession object
 */
case class KnolSession(val knolSessionId: Int, val topic: String, val date: Date, val knolId: Int)