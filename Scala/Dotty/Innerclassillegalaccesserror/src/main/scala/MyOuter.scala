import outer._
import inner._
class MyOuter extends Outer {
    val ij = new InnerJava {
      def print(): Unit = println(privStr)
    }
}