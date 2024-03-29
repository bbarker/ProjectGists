package edu.cornell.cac.sbh.repeater.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn

object RepeatAll {
  def main(args: Array[String]) {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val route = extractRequest { request =>
      scribe.info(request.toString)
      scribe.info(request.headers.toString)
      complete(HttpEntity(
        ContentTypes.`text/plain(UTF-8)`,
        request.toString()
      ))
    }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 5001)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}