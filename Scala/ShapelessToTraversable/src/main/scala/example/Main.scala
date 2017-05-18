package example

import shapeless.PolyDefns.->
import shapeless._
import shapeless.poly._
import shapeless.syntax.std.traversable._
import shapeless.ops.hlist._

class Main {
  def myMethod[DL <: HList](detailsIn: DL)(implicit ev: ToTraversable[DL, List]) = {
    val detail: String = detailsIn.mkString("", "; ", "")
    println(detail)
  }
}

object Main {

  def main(args: Array[String]): Unit = {
    val myMain = new Main
    val details = 5 :: "asf" :: HNil
    myMain.myMethod(details)
  }
}
