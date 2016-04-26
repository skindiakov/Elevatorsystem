package rest.controller

import akka.pattern.AskTimeoutException
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.swagger.{Swagger, SwaggerSupport}
import org.scalatra.{FutureSupport, RequestTimeout, ScalatraServlet}
import org.slf4j.LoggerFactory
import rest.controller.BaseController.{HasSwagger, TimeoutMessage}

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits._

abstract class BaseController extends ScalatraServlet
  with FutureSupport
  with JacksonJsonSupport
  with SwaggerSupport { self: HasSwagger =>

  before() {
    contentType = formats("json")
  }

  error {
    case e: AskTimeoutException => RequestTimeout(body = TimeoutMessage(e.getMessage))
  }


  override protected implicit def executor: ExecutionContext = global

  protected implicit lazy val jsonFormats: Formats = DefaultFormats.withBigDecimal


  protected val log =  LoggerFactory getLogger getClass
}

object BaseController {

  case class TimeoutMessage(msg: String)

  type HasSwagger = {
    val swagger: Swagger
  }
}
