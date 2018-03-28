package com.foo.bar

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration.Inf
import scala.concurrent.{Await, Future}
import scalacache._
import scalacache.caffeine._
import scalacache.modes.sync._

object Main {

  def main(args: Array[String]): Unit = {

    implicit val caffeineCache: Cache[String] = CaffeineCache[String]

    val cpt = new java.util.concurrent.atomic.AtomicInteger()
    def stringF: Future[String] = Future{ cpt.incrementAndGet(); "titi"}

    def outerFun: String = Await.result(stringF, Inf)



    Seq.fill(1000)(0).map(_ => caching("Toto")(ttl = None)(outerFun))


    println(cpt.get) //returns 1
  }


}
