//https://github.com/typelevel/squants/issues/230

import squants.time.Time
import squants.time.TimeConversions._

class Main{}
object Main {

  def main(args: Array[String]): Unit = {
    val initTime: Time = 1.0 seconds
    val initTime2: Time = initTime.seconds
    println(initTime)
    println(initTime2)
  }
}
