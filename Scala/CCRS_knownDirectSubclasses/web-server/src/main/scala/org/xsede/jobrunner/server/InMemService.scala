package org.xsede.jobrunner.server

import akka.actor.ActorSystem
import org.xsede.jobrunner.model._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable
import scala.concurrent.Future
import InMemService._

class InMemService(meta: CommandMetaData)(implicit asys: ActorSystem) extends Api {

  def exec(cmdsIn: List[Command]): Future[List[RunResult]] = ???

  def poll(id: CommandId): Future[RunResult] = Future {
    asys.log.info(s"Polling memStorage for command ${id.id}")
    memStorage(id).result
  }
}
object InMemService {
  def apply(meta: CommandMetaData)(implicit asys: ActorSystem): InMemService =
    new InMemService(meta)(asys)

  private val memStorage: mutable.Map[CommandId, CommandWithResult] = mutable.Map()



}
