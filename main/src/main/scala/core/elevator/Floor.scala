package core.elevator


/**
  * Created by stas on 26.04.16.
  */
case class Floor(elevatorSystem:ElevatorSystem, floor: Int) {

  var waitingPassengers:List[Passenger] = List()
  /**
    * When new passenger comes he calls the elevator with appropriate direction
    * @param passenger
    */
  def passengerComes(passenger: Passenger): Unit = {
    waitingPassengers = waitingPassengers :+ passenger
    elevatorSystem.pickup(floor, passenger.direction())
  }

  def passengerSitsInElevator(passenger: Passenger):Unit = {
    waitingPassengers = waitingPassengers.filter(_ != passenger)
  }

}
