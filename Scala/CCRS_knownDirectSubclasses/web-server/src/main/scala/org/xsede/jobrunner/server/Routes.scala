package org.xsede.jobrunner.server

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.actor.ActorSystem
import akka.http.scaladsl.model.RemoteAddress

import scala.concurrent.ExecutionContext

object Routes extends Directives {
  def apply()(implicit asys: ActorSystem, mat: Materializer, exc: ExecutionContext): Route = {
    pathSingleSlash {
      getFromResource("static/index.html")
    } ~
    path(Segment) { segment =>
      getFromResource(s"static/$segment")
    } ~
    path("target" / Segment) { segment =>
      getFromResource(s"static/target/$segment")
    } ~
    path("api" / Segments) { segments => extractClientIP { ip =>
      post(AutowireServer.dispatch(segments, ip: RemoteAddress))
    }}
  }
}
