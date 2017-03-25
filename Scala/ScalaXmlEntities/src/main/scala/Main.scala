import scala.xml._

object Main extends App {
  val entity = EntityRef("&nbsp;")
  val raw = Unparsed("&nbsp;")
  println(s"entity has ${entity.toString}")
  println(s"raw has ${raw.toString}")

  // prints:
  // entity has &&nbsp;;
  // raw has &nbsp;

  val entityElem = <span>{EntityRef("&nbsp;")}</span>
  val rawElem = <span>{Unparsed("&nbsp;")}</span>
  println(s"entityElem has ${entityElem.toString}")
  println(s"rawElem has ${rawElem.toString}")
  // prints:
  // entityElem has <span>&&nbsp;;</span>
  // rawElem has <span>&nbsp;</span>


  val entityElem2 = <span>{EntityRef("nbsp")}</span>
  println(s"entityElem2 has ${entityElem2.toString}")
  // prints
  // entityElem2 has <span>&nbsp;</span>
  // This is correct!


}
