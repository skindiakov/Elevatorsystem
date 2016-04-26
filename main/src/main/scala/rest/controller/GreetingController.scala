package rest.controller

import akka.actor.ActorRef
import akka.pattern.ask
import org.scalatra.swagger._

class GreetingController()(implicit val swagger: Swagger)
  extends BaseController {

  get("/", operation(
    apiOperation[String]("getGreeting")
      summary "Show basic greeting"
      notes "Prints 'Hello world'")) {

    "Hello world"
  }

  get("/:name/?", operation(
    apiOperation[String]("getGreetingByName")
      summary "Greet by name"
      notes "Prints 'Hello, %name%'"
      parameter pathParam[String]("name").description("Name to greet"))) {

    val name = params.getOrElse("name", halt(400))
    "Hello " + name
  }

  get("/rest/:name/?", operation(apiOperation[String]("getGreetingByName")
    summary "Greet by name"
    notes "Prints 'Hello, %name%'"
    parameter pathParam[String]("name").description("Name to greet"))) {

    contentType = formats("json")
    val name = params.getOrElse("name", halt(400))
    Map("hello" -> name)
  }

  override protected def applicationDescription: String = "Hello World Controller"
}


