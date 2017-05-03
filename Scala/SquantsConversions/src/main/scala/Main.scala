//https://github.com/typelevel/squants/issues/229

import squants.time.Time
import squants.time.TimeConversions._

class Main {
  var conceptionTime: Time = -1.0 nanoseconds
}

object Main {

  def main(args: Array[String]): Unit = {
    val thisMain = new Main
  }

}
