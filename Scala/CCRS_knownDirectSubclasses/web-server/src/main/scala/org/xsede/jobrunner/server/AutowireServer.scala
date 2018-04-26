package org.xsede.jobrunner.server

import org.xsede.jobrunner.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{RequestContext, RouteResult}
import akka.util.ByteString
import akka.http.scaladsl.model.StatusCodes._
import boopickle.Default._
import java.nio.ByteBuffer

import akka.actor.ActorSystem
import akka.http.scaladsl.model.RemoteAddress

import scala.concurrent.{ExecutionContext, Future}

object AutowireServer extends autowire.Server[ByteBuffer, Pickler, Pickler]  {
  override def read[R: Pickler](p: ByteBuffer) = Unpickle[R].fromBytes(p)
  override def write[R: Pickler](r: R) = Pickle.intoBytes(r)
  
  def dispatch(url: List[String], ip: RemoteAddress)
  (implicit exc: ExecutionContext, asys: ActorSystem): RequestContext => Future[RouteResult] =
    entity(as[ByteString]) { entity =>
      ip.toOption match {
        case Some(addr) =>
          implicit val serverMetaData = PartialCmdMetaData(
            address = Some(addr.getHostAddress),
            hostname = Some(addr.getCanonicalHostName)
          )
          val service = InMemService(serverMetaData)
          val body = Unpickle[Map[String, ByteBuffer]].fromBytes(entity.asByteBuffer)
          val request: Future[ByteBuffer] = AutowireServer.route[Api](service)(autowire.Core.Request(url, body))
          onSuccess(request)(buffer => complete(ByteString(buffer)))
        case None =>
          val message = "Couldn't ascertain client IP!"
          asys.log.warning(message)
          complete((Forbidden, message))
      }
    }

}
