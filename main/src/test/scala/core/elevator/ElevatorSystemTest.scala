package core.elevator

import org.scalatest.{FunSuite, Inside, Matchers}

/**
  * Created by stas on 26.04.16.
  */
class ElevatorSystemTest extends FunSuite with Matchers with Inside with Loggable {


  test("Initialize system") {
    val haus: Haus = Haus(20, 1)

    haus.step()
    haus.passengerComes(1, 15)
    haus.passengerComes(7, 15)
    haus.passengerComes(9, 20)
    haus.step(18)
    haus.passengerComes(1, 3)
    haus.passengerComes(9, 15)
    haus.step(8)
    haus.passengerComes(5, 1)
    haus.step(20)
    val status = haus.getSystem.status()

    log info s"Status of elevators [$status]"

  }

  test("Verify that elevator is not stuck at last floor") {
    val haus: Haus = Haus(5, 1)

    haus.step()
    haus.passengerComes(5, 1)
    haus.step(20)
    val status = haus.getSystem.status()

    log info s"Status of elevators [$status]"

  }

  test("Two passengers on same floor will get two elevators if they want to go in different directions") {
    val haus: Haus = Haus(9, 2)

    haus.step()
    haus.passengerComes(5, 9)
    haus.passengerComes(5, 1)
    haus.step(5)

    val status = haus.getSystem.status()

    log info s"Status of elevators [$status]"

    // As soon it is only test, I aow myself to use Option.get
    haus.getSystem.status(1).get.floor should be (5)
    haus.getSystem.status(2).get.floor should be (5)
  }

  test("All passengers will sit in same elevator, regardless of their direction. Place to improve"){
    val haus: Haus = Haus(9, 2)

    haus.step()
    haus.passengerComes(5, 9)
    haus.passengerComes(5, 1)
    haus.step(5)


    log info s"Status of elevators [${haus.getSystem.status()}]"

    // It means that both passengers se into one elevator
    haus.getSystem.status().exists(_.direction == NONE) should be (true)

    haus.step(4)
    log info s"Status of elevators [${haus.getSystem.status()}]"
    haus.getSystem.status(1).get.floor should be (9)

    haus.step(9)
    //First elevator took second passenger down
    haus.getSystem.status(1).get.floor should be (1)
    // Second elevator still stands
    haus.getSystem.status(2).get.floor should be (5)
  }

}
