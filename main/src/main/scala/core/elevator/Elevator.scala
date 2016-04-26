package core.elevator

/**
  * Created by stas on 26.04.16.
  */
case class Elevator(id: Long, var atFloor: Int = 1, haus: Haus) extends Loggable {

  var destinations: List[Int] = List()
  var passengers: List[Passenger] = List()
  /**
    * Expected that elevator is quite slow and able to move only one floor Up/Down in one point of time
    **/
  def move(): Unit = {
    val step = direction() match {
      case UP =>
        log info s"Elevator [$id] is on floor [$atFloor] and goes UP"
        1
      case DOWN =>
        log info s"Elevator [$id] is on floor [$atFloor] and goes DOWN"
        -1
      case NONE =>
        log info s"Elevator [$id] is on floor [$atFloor] and stands still"
        0
    }
    atFloor += step
  }

  /**
    * Call of step() means that some time had passed in the real world and elevator has to change its state.
    * For simplification I expect that in one unit of time elevator can either move one floor Up/Down or stay at floor
    * and let passengers to go in and out
    **/
  def step(): Unit = {
    if (mustStopAtCurrentFloor) {
      haus.getFloor(atFloor).foreach(arriveToFloor)
    } else {
      move()
    }
  }

  /**
    * When arrives to floor, evevator should remove current floor from destination list,
    * let passengers go out and let new passengers to go in and select their own destinations.
    *
    * @param floor
    */
  def arriveToFloor(floor: Floor): Unit = {
    log info s"Elevator [$id] arrived at floor [${floor.floor}]"
    def atDestination(): Unit = {
      destinations = destinations.filter(_ != floor.floor)
    }
    def passengersExit(): Unit = {
      passengers = passengers.filterNot(_.destination == floor.floor)
    }
    def passengerEnters(floor: Floor): Unit = {
      val elevatorDirection = direction()
      val passengersGiongIntoElevator = floor.waitingPassengers.filter(_.direction == elevatorDirection || elevatorDirection == NONE)
      passengersGiongIntoElevator.foreach { passenger =>
        floor.passengerSitsInElevator(passenger)
        passengers = passengers :+ passenger
        destinations = destinations :+ passenger.destination
      }

    }
    atDestination()
    passengersExit()
    passengerEnters(floor)

  }

  /**
    * Used by ElevatorSystem to call elevator to pick up passenger
    *
    * @param destination - target floor
    */
  def addDestination(destination: Int): Unit = {
    destinations = destinations :+ destination
  }


  def mustStopAtCurrentFloor: Boolean = {
    destinations.contains(atFloor)
  }

  /**
    * At first direction depends on requests of passengers.
    * If there are no passengers elevator checks if it has requests to pick up new passengers
    *
    * @return
    */
  def direction(): Direction = {
    if (passengers.nonEmpty) {
      calculateDirectionByDestination(passengers.map(_.destination))
    } else if (destinations.nonEmpty) {
      calculateDirectionByDestination(destinations)
    } else NONE

  }

  /**
    * Calculates which direction must elevator move to visit required floors
    *
    * @param destinations
    * @return
    */
  def calculateDirectionByDestination(destinations: List[Int]): Direction = {
    destinations match {
      case Nil => NONE
      case _ =>
        val highestFloor = destinations.max
        val lowestFloor = destinations.min
        if (highestFloor > atFloor)
          UP
        else if (lowestFloor < atFloor)
          DOWN
        else
          NONE
    }

  }
}

case class Passenger(floor: Int, destination: Int) {
  /**
    * Calculates which direction must elevator move to visit required floor
    *
    * @return
    */
  def direction(): Direction = {
    if (destination > floor) UP
    else if (destination < floor) DOWN
    else NONE
  }
}


