package com.foo.bar

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration.Inf
import scala.concurrent.{Await, Future}
import scalacache._
import scalacache.caffeine._
import scalacache.modes.sync._

object Main {

  def main(args: Array[String]): Unit = {

    implicit val caffeineCache: Cache[Future[String]] = CaffeineCache[Future[String]]

    val cpt = new java.util.concurrent.atomic.AtomicInteger()
    def stringF: Future[String] = Future{ cpt.incrementAndGet(); "titi"}


    Seq.fill(1000)(0).map(_ => caching("Toto")(ttl = None)(stringF))


    println(cpt.get) //returns 1
  }


}
