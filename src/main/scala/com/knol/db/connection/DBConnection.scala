package com.knol.db.connection
import scala.slick.driver.PostgresDriver.simple._
import java.util.Date
import java.text.SimpleDateFormat
import scala.slick.lifted.ProvenShape
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
trait DBConnection {
  implicit val util2sqlDateMapper = MappedColumnType.base[java.util.Date, java.sql.Date](
    { utilDate => new java.sql.Date(utilDate.getTime()) },
    { sqlDate => new java.util.Date(sqlDate.getTime()) })
  val logger = LoggerFactory.getLogger(this.getClass)
  /**
   * This is one of the method of DBConnection trait which return Database object.
   */
  def getObject():Database ={
      
        val connection = Database.forURL(url = "jdbc:postgresql://localhost:5432/knolknolxdb1", user = "postgres",
          password = "postgres", driver = "org.postgresql.Driver")
        logger.debug("Connection created")
        connection
      
    }
}