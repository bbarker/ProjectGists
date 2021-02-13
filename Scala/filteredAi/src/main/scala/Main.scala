import scala.annotation.tailrec

object Main extends App {
  // TODO: ideally do with streaming or some other lazy data structure
  val inLines = io.Source.stdin.getLines.drop(1)

  def enumerateExps(nn: Int, opens: List[Int]): List[String] = {
    // one way: 1 to 2n -> convert to binary, convert 0 to [ and 1 to ]
    val binStrs = (1 to 2*nn).map(ki => ki.toBinaryString)
      .map(dStr => dStr.reverse.padTo(2*nn, '0').reverse)
    val bracketStrs = binStrs.map(bStr => bStr.replace('0', '[').replace('1', ']'))


    def openBracketsAt(opens: List[Int], bracketExpr: String): String = {
      opens.foldLeft(bracketExpr){(bExp, ix) => bExp.substring(0, ix)
        + '['  + (if (ix + 1 > 2*nn) "" else bExp.substring(ix + 1))
      }
    }
    // There may be some redundancy after this, so we will use (possibly not the
    // most efficient approach: Set)
    bracketStrs.map(bStr => openBracketsAt(opens, bStr)).toSet.toList
  }

  // Rather than making an actual parser, we can use a "stack"
  def parseExp(bracketExpr: String): Boolean = {
    @tailrec
    def go(exprRemain: String, stack: Int): Boolean = {
      val newStack = exprRemain.headOption match {
        case Some(ch) => if (ch == '[') stack + 1 else stack - 1
        case None => stack
      }
      if (newStack < 0) false else go(exprRemain.drop(1), newStack)
    }
    go(bracketExpr, 0)
  }

  def readData(lines: Iterator[String]): (Int, List[Int]) = {
    @tailrec
    def go(linesLeft: Iterator[String], data: List[(Int, List[Int])]): List[(Int, List[Int])] = {
      val nLineOpt = linesLeft.nextOption()
      val vLineOpt = linesLeft.nextOption()
      (nLineOpt, vLineOpt) match {
        case (Some(nLine), Some(vLine)) => {
          val nOpt = nLine.split(' ').map(v => v.toInt).headOption
          val ks = vLine.split(' ').map(v => v.toInt)
          nOpt match  {
            case Some(n) => (n, ks) :: data
            case None => data
          }
        }
      }
    }
    go(lines, Nil).reverse
  }
}













/*
"Square Brackets"

Your solution will be scored against some number of hidden test cases. A sample is provided below.


You are given:

-a positive integer n,
-an integer k, 1<=k<=n,
-an increasing sequence of k integers 0 < s1 < s2 < ... < sk <= 2n.

What is the number of proper bracket expressions of length 2n with opening brackets appearing in positions s1, s2,...,sk?

Illustration

Several PROPER bracket expressions:

[[]][[[]][]]
[[[][]]][][[]]

An IMPROPER bracket expression:

[[[][]]][]][[]]

There is exactly one proper expression of length 8 with opening brackets in positions 2, 5 and 7.

Task

Write a program which for each data set from a sequence of several data sets:

-reads integers n, k and an increasing sequence of k integers from input,
-computes the number of proper bracket expressions of length 2n with opening brackets appearing at positions s1,s2,...,sk,
-writes the result to output.

Input

The first line of the input file contains one integer d, 1 <= d <= 10, which is the number of data sets. The data sets follow. Each data set occupies two lines of the input file. The first line contains two integers n and k separated by single space, 1 <= n <= 19, 1 <= k <= n. The second line contains an increasing sequence of k integers from the interval [1;2n] separated by single spaces.

Output

The i-th line of output should contain one integer - the number of proper bracket expressions of length 2n with opening brackets appearing at positions s1, s2,...,sk.

If there is more than one data set, print the output for other data sets in successive lines(see sample output).

Code evaluation is based on your output, please follow the sample format and do NOT print anything else.

Sample input:

5
1 1
1
1 1
2
2 1
1
3 1
2
4 2
5 7

Sample Output:
1
0
2
3
2


 */
