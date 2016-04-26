package rest.controller

import akka.actor.ActorSystem
import core.elevator.Haus
import org.scalatra.swagger._

class ElevatorSystemController()(implicit val swagger: Swagger)
  extends BaseController {

  var haus:Haus =  Haus(20, 3)
  import java.util.concurrent._

  val ex = new ScheduledThreadPoolExecutor(1)
  val task = new Runnable {
    def run() = haus.step()
  }
  val f = ex.scheduleAtFixedRate(task, 1, 1, TimeUnit.SECONDS)



  get("/status", operation(
    apiOperation[String]("getStatus")
      summary "gets status of all elevators")) {
    contentType = formats("json")
    haus.getSystem.status()
  }

  /**
    * Method uses GET request for simplifictation of testing
    * For production use must be POST
    */
  get("/step/:number/?", operation(
    apiOperation[Int]("step")
      summary "Makes time pass"
      parameter pathParam[String]("number").description("Number of steps"))) {

    val number:Int = params.getOrElse("number", halt(400)).toInt
    haus.step(number)
    contentType = formats("json")
    Map("result" -> "ok")
  }

  /**
    * Method uses GET request for simplifictation of testing
    * For production use must be POST
    */
  get("/passenger/:floor/:destination/?") {

    contentType = formats("json")
    val floor = params.getOrElse("floor", halt(400)).toInt
    val destination = params.getOrElse("destination", halt(400)).toInt
    haus.passengerComes(floor, destination)
    Map("result" -> "ok")
  }

  get("/haus/:floors/:elevators/?") {

    contentType = formats("json")
    val floors = params.getOrElse("floors", halt(400)).toInt
    val elevators = params.getOrElse("elevators", halt(400)).toInt
    haus = Haus(floors, elevators)
    Map("result" -> "ok")
  }

  /**
    * Made possible to stop automated time passing and do all in manual way by calling get("/step/?") controller
    */
  get("/stopTimer/?") {
    contentType = formats("json")
    f.cancel(false)
    Map("result" -> "ok")
  }



  override protected def applicationDescription: String = "Elevator System Controller"
}


