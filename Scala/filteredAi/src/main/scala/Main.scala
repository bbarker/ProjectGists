import scala.math.Ordering.Implicits.infixOrderingOps
//Hit compile and run to see a sample output.
//Read values from stdin, do NOT hard code input.


object Main extends App {
  // Find the modal values, and for each mode, find the first
  // and last index, and the print the shortest window
  val intLine: String = io.Source.stdin.getLines.drop(1).next()
  val intArray: Array[Int] = intLine.split(' ').map(iStr => iStr.toInt)
  val countMap: Map[Int, Int] = intArray.groupBy(x => x).map(t => (t._1, t._2.length))
  val countRanks: List[(Int, List[Int])] = countMap.toList.map(t => (t._2, t._1))
    // Counts are now first position
    .groupBy(t => t._1).toList.map(tt => (tt._1, tt._2.map(t => t._2)))
    .sortWith(_._1 > _._1)
  countRanks.headOption match {
    case Some((_, values)) => {
      val minSubDegree = values.map(v => subArrayDegree(v, intArray))
        .sortWith(_ < _).headOption
      minSubDegree match {
        case Some(d) => println(d)
        case None => ()
      }
    }
    case None => ()
  }

  def subArrayDegree(elem: Int, arr: Array[Int]): Int = {
    val fst = arr.indexOf(elem)
    val last = arr.lastIndexOf(elem)
    last - fst + 1

  }
}

