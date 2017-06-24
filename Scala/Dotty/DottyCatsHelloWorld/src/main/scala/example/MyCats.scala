import cats._
import cats.instances.all._
import cats.syntax.eq._

// Imports work but use of === causes Scala2Unpickler issue
object HelloWorld {
  def main(args: Array[String]): Unit = {
    val hello = "hello"
    if ("hello" === hello) {
      println("Hello, world!")
    }
  }
}