//Hit compile and run to see a sample output.
//Read values from stdin, do NOT hard code input.


// We can iteratively remove each word in the original list
// from other words, trating the first as a substring. If the length
// reaches zero, we have found a candidate.

//
// We need a slightly complex sort, storing the following information
// in order of highest precedence:
// First Filter:
//  The length of the string after all replacements < original word length
//
// Then Sort:
// 1. The original string length
// 2. The original string (sorted without case)
// 3. The position of the string in the input
//
//

import scala.annotation.tailrec
import scala.math.Ordered.orderingToOrdered
import scala.math.Ordering.Implicits._

object Main extends App {
  case class WordInfo(original: String, pos: Int)
    extends Ordered[WordInfo] {
    def compare(that: WordInfo): Int = {
      (-1 * this.original.length, (this.original.toLowerCase, this.pos)) compare
        (-1 * that.original.length, (that.original.toLowerCase, that.pos))
    }
  }

  def replace(sb: StringBuilder, sub: String): StringBuilder = {
    val startIx = sb.indexOf(sub)
    val finIx = startIx + sub.length
    if (startIx >= 0 && finIx >= 0) {
      sb.replace(startIx, finIx, "")
    }
    sb
  }

  val words = io.Source.stdin.getLines.drop(1).map(s => s.trim()).toArray
  val wordRemovers = words.map(w => new StringBuilder(999, w.toLowerCase))
  val choppedWords: Array[String] = (0 until words.length).foldLeft(wordRemovers){ (wRems, ix) => {
    val subWord = words(ix)
    wRems.zipWithIndex.map{case (sb, sbIx) =>
      if (ix != sbIx) replace(sb, subWord) else sb
    }
  }}.map(x => x.toString)


  val candidateWords = choppedWords.zip(words.zipWithIndex)
    .map{case (chopped, (orig, ix)) =>
      if (chopped == orig.toLowerCase) None else Some(WordInfo(orig, ix))
    }.flatten
  candidateWords.sorted.map(wi => wi.original).headOption match {
    case Some(w) => println(w)
    case None => println("None")
  }

}


// One complication occurs if a word is a substring of another word that is
// yet a substring of another word. E.g.: "test", "tester", "testerFoo"
// in this case, if we remove "test" first we have "", "er", "erFoo"
// so at this point, we must try to remove "er" rather than "tester" to
// make progress.
//
// (Actually this is not a complication, we do not care about this.)

// Another slight complication is if the same string occurs in a word multiple times
// (at least, depending on the replacement method being used). For this we can create
// idemReplace (for idempotent replacement)

/* Again, actually, we don't need idemReplace - the amount a word is composed by other words
  doesn't matter, other than it has to be partly composed */
/*

    @tailrec
    def idemReplace(sb: StringBuilder, sub: String): StringBuilder = {
        val startIx = sb.indexOf(sub)
        val finIx = startIx + sub.length
        val sbBefore = sb.toString
        if (startIx >= 0 && finIx >= 0) {
            sb.replace(startIx, finIx, "")
        }
        val sbAfter = sb.toString
        if (sbBefore == sbAfter) sb else idemReplace(sb, sub)
    }

*/






