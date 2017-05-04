//https://github.com/typelevel/squants/issues/231
import squants.mass.{Kilograms, Mass}
import squants.mass.MassConversions.MassNumeric
import squants.market.{Money, USD}
import squants.market.MoneyConversions.MoneyNumeric

class Main {}
object Main {
  def main(args: Array[String]): Unit = {
    val massList = List(Kilograms(1), Kilograms(2))
    val moneyList = List(USD(1), USD(2))
    val massSum = massList.sum
    val moneySum = moneyList.sum
  }

}
