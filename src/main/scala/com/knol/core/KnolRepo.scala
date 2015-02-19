package com.knol.core
import com.knol.db.connection._
import scala.collection.mutable.MutableList
import scala.slick.driver.PostgresDriver.simple._
import java.util.Date
import java.text.SimpleDateFormat
import scala.slick.lifted.ProvenShape
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
/**-----------------------------------------------------------------------------------------------------
 * This is KnolRepo trait which extends DBConnection and implements CURD operations.
 * -----------------------------------------------------------------------------------------------------
 */
trait KnolRepo extends DBConnection{
  class KnolderTable(tag: Tag) extends Table[Knolder](tag, "knolder") {
    def id: Column[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name: Column[String] = column[String]("name", O.NotNull)
    def email: Column[String] = column[String]("email", O.NotNull)
    def mobileNo: Column[String] = column[String]("mobileNo", O.NotNull)
    def * = (id, name, email, mobileNo) <> (Knolder.tupled, Knolder.unapply)
  }
  val knolderTable = TableQuery[KnolderTable]

  def knolderTableAutoInc = knolderTable returning knolderTable.map(_.id)
  val dbObject = getObject()
  /**----------------------------------------------------------------------------------------------
   * createKnol is one of the method of KnolRepo Trait which takes Knolder's object as argument and
   * returns AutoIncr Id value.
   * ----------------------------------------------------------------------------------------------
   */
  def createKnol(knol: Knolder): Option[Int] = {
    dbObject.withSession { implicit session =>
      val result = knolderTableAutoInc.insert(knol)
      Some(result)
    }
  }
  /**------------------------------------------------------------------------------------------
   * update is one of the method of KnolRepo Trait which takes Knolder's object as argument and
   * returns number of effected rows.
   * ------------------------------------------------------------------------------------------
   */
  def update(knol: Knolder):Option [Int]={
    dbObject.withSession { implicit session =>
      val result = knolderTable.filter (_.id ===knol.id).update(knol)
      Some(result)
    }
  }
  /**--------------------------------------------------------------------------------------------
   * delete is a method which takes Knolder's id as argument and returns number of effected rows.
   * --------------------------------------------------------------------------------------------
   */
  def delete(id: Int): Option[Int]={
     dbObject.withSession { implicit session =>
      val result = knolderTable.filter (_.id ===id).delete
      Some(result)
    }
  }
  /**---------------------------------------------------------------------------------------------
   * getKnol is a method which tales Knolder's id as argument and returns number of effected rows.
   * ---------------------------------------------------------------------------------------------
   */
  def getKnol(id: Int):Option[List[ Knolder]] ={
    dbObject.withSession { implicit session =>
      val result = knolderTable.filter(_.id === id).list
      Some(result)
    }
  }
   

}
/**------------------------------------------------------------------------
 * This is Knolder's case class which is used for create knolder's object.
 * ------------------------------------------------------------------------
 */
case class Knolder(val id: Int, val name: String,val email: String,val mobileNo: String)