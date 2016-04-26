package core.elevator

/**
  * Created by stas on 26.04.16.
  */

sealed trait Direction {
  override def toString: String = this.getClass.getSimpleName
}

object UP extends Direction

object DOWN extends Direction

object NONE extends Direction