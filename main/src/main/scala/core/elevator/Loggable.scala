package core.elevator

import org.slf4j.LoggerFactory

/**
  * Created by stas on 26.04.16.
  */
trait Loggable {
  protected val log =  LoggerFactory getLogger getClass

}
