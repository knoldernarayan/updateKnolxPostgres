package slickApp
import scala.slick.driver.PostgresDriver.simple._
import java.util.Date
import java.text.SimpleDateFormat
import scala.slick.lifted.ProvenShape
object KnolRepo extends App{
implicit val util2sqlDateMapper = MappedColumnType.base[java.util.Date, java.sql.Date](
    { utilDate => new java.sql.Date(utilDate.getTime()) },
    { sqlDate => new java.util.Date(sqlDate.getTime()) })
    class KnolderTable(tag: Tag) extends Table[Knolder](tag, "knolder") {
    def id: Column[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name: Column[String] = column[String]("name", O.NotNull)
    def email:Column[String] = column[String]("email",O.NotNull)
    def mobileNo:Column[String]=column[String]("mobileNo",O.NotNull)
    def * = (id,name,email,mobileNo) <> (Knolder.tupled, Knolder.unapply)
  }

   val knolderTable = TableQuery[KnolderTable]

  def knolderTableAutoInc = knolderTable returning knolderTable.map(_.id)

  class KnolSessionTable(tag: Tag) extends Table[KnolSession](tag, "knolSession") {
    def id: Column[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def topic: Column[String] = column[String]("topic", O.NotNull)
    def knolx_Date: Column[Date] = column[Date]("knolx_Date", O.NotNull)
    def knol_Id: Column[Int] = column[Int]("knol_Id", O.NotNull)
    def * = (id,topic,knolx_Date,knol_Id) <> (KnolSession.tupled, KnolSession.unapply)
    def fKey = foreignKey("knol_knolx_fk",knol_Id,knolderTable)(_.id)
  }

  val knolSessionTable = TableQuery[KnolSessionTable]

  def knolSessionTableAutoInc = knolSessionTable returning knolSessionTable.map(_.id)
  Database.forURL(url = "jdbc:postgresql://localhost:5432/knolknolxdb", user = "postgres",
    password = "postgres", driver = "org.postgresql.Driver").withSession { implicit session =>

      // ddl
      //(knolderTable.ddl ++ knolSessionTable.ddl).create(session)

      // insert a record
      knolderTable.insert(Knolder(0,"narayan", "narayan@knoldus.com","234566666666"))
  }

}
case class Knolder(val id :Int,val name:String,val email:String,val mobileNo:String)
case class KnolSession(val knolSessionId :Int,val topic:String,val date:Date,val knolId:Int)
