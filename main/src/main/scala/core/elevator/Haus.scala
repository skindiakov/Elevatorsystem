package core.elevator

import scala.collection.mutable.ListBuffer

/**
  * Created by stas on 26.04.16.
  * Main object,to which belongs Elevator System
  */
class Haus() {
  val floors = new ListBuffer[Floor]()
  var elevatorSystem: ElevatorSystem = _

  def addFloor(floor: Floor) = floors += floor

  def getFloor(floorNumber: Int): Option[Floor] = {
    floors.find(_.floor == floorNumber)
  }

  def getSystem = elevatorSystem

  def setSystem(system: ElevatorSystem): Unit = {
    elevatorSystem = system
  }

  def passengerComes(floor: Int, destinationFloor: Int): Unit = {
    val passenger = Passenger(floor, destinationFloor)
    getFloor(floor).foreach(_.passengerComes(passenger))
  }

  def step(count: Int = 1): Unit = {
    for (i <- 1 to count) elevatorSystem.step()
  }

}

/**
  * Companion object for Haus initialization
  */
object Haus {
  def apply(numFlors: Int, numElevators: Int): Haus = {
    val haus = new Haus()
    val elevators: List[Elevator] = (for (id <- 1 to numElevators) yield Elevator(id, haus = haus)).toList
    val elevatorSystem = new ElevatorSystem(elevators)
    val floors = (for (floor <- 1 to numFlors) yield Floor(elevatorSystem, floor)).toList
    floors.foreach(haus.addFloor)
    haus.setSystem(elevatorSystem)
    haus

  }
}
