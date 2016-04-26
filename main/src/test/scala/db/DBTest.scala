package db

/**
  * Created by stas on 24.04.16.
  * Test proves h2 connection
  */

import org.scalatest.{FunSuite, Inside, Matchers}
import scalikejdbc._

class DBTest extends  FunSuite with Matchers with Inside with DBSettings {


   test("Bsic DB connection test") {
    // table creation, you can run DDL by using #execute as same as JDBC
    sql"""
      create table members (
        id serial not null primary key,
        name varchar(64),
        created_at timestamp not null
      )
      """.execute.apply()

    // insert initial data
    Seq("Alice", "Bob", "Chris") foreach { name =>
      sql"insert into members (name, created_at) values (${name}, current_timestamp)".update.apply()
    }

    // for now, retrieves all data as Map value
    val entities: List[Map[String, Any]] = sql"select * from members".map(_.toMap).list.apply()

    // defines entity object and extractor


    // find all members
    val members: List[Member] = sql"select * from members".map(rs => Member(rs)).list.apply()

    // use paste mode (:paste) on the Scala REPL
    val m = Member.syntax("m")
    val name = "Alice"
    val alice: Option[Member] = withSQL {
      select.from(Member as m).where.eq(m.name, name)
    }.map(rs => Member(rs)).single.apply()

     alice.isDefined should be (true)
  }

  import org.joda.time._
  case class Member(id: Long, name: Option[String], createdAt: DateTime)
  object Member extends SQLSyntaxSupport[Member] {
    override val tableName = "members"

    def apply(rs: WrappedResultSet) = new Member(
      rs.long("id"), rs.stringOpt("name"), rs.jodaDateTime("created_at"))
  }
}
