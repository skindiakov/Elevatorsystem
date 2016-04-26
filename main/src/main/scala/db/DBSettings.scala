package db

import scalikejdbc.{AutoSession, ConnectionPool, GlobalSettings}


trait DBSettings {

  GlobalSettings.loggingSQLErrors = true
  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")
  implicit val session = AutoSession

}





