package core.elevator

import scala.collection.mutable.ListBuffer

/**
  * Created by stas on 26.04.16.
  */
class ElevatorSystem( elevators: List[Elevator]) extends Loggable {


  private val pendingPickUpRequests  = new ListBuffer[PickUpRequest]


  /**
    * Mehod simulates bypassing of some time amount
    */
  def step(): Unit = {
    def reSubmitPickUpRequests: Unit = {
      val req  = pendingPickUpRequests.toArray
      pendingPickUpRequests.clear()
      req.foreach { r =>
        pickup(r.floor, r.direction)
      }
    }
    log info s"Next time unit"
    reSubmitPickUpRequests
    elevators.foreach(_.step())
  }

  /**
    * Get status of concrete elevator.
    * @param elevatorId
    * @return
    */
  def status(elevatorId: Long): Option[ElevatorStatus] = {
    elevators.find(_.id == elevatorId).map { e =>
      ElevatorStatus(e.id, e.atFloor, e.direction().toString)
    }
  }

  /**
    * Returns status of all available elevators
    * @return
    */
  def status(): Seq[ElevatorStatus] = {
    elevators.map { e =>
      ElevatorStatus(e.id, e.atFloor, e.direction().toString)
    }
  }

  /**
    * System receives request from user to bring elevator
    * @param floor
    * @param direction
    */
  def pickup(floor: Int, direction: Direction): Unit = {
    log info s"Passenger is on floor [$floor] and wants to go [$direction]"
    getNearestElevatorWithMatchingDirection(floor, direction) match {
      case Some(e) =>
        log info s"System selected elevator [${e.id}] to pick him up"
        e.addDestination(floor)
      case None =>
        savePendingRequest(PickUpRequest(floor, direction))
    }
  }

  /**
    * System must choose the most appropriate elevator.
    * If there are no available elevators, system must save request and repeat next time
    * @param floor
    * @param direction
    * @return
    */
  def getNearestElevatorWithMatchingDirection(floor: Int, direction: Direction): Option[Elevator] = {
    val elevatorsWithRightDirection: List[Elevator] = elevators.filter(e => e.direction() == direction || e.direction() == NONE)
    elevatorsWithRightDirection match {
      case Nil => None
      case _ => Option(elevatorsWithRightDirection.minBy(e => (e.atFloor - floor).abs))
    }
  }

  def savePendingRequest(pickUpRequest: PickUpRequest): Unit = {
    pendingPickUpRequests += pickUpRequest
  }


}

case class PickUpRequest(floor: Int, direction: Direction)

/**
  * Class for improving of readability of Elevator Status
  * @param id
  * @param floor
  * @param direction
  */
case class ElevatorStatus(id: Long, floor:Int, direction: String ){
  override  def toString(): String = {
    s"Elevtor [$id] is on floor [$floor], going [$direction]"
  }
}
