
import fi.oph.scalaschema._

import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods

object ExampleApp extends App {


  val schema: Schema = SchemaFactory.default.createSchema[FeedGroupType]
  val schemaAsJson: JValue = schema.toJson
  val schemaAsString = JsonMethods.pretty(schemaAsJson)
  println(schemaAsString)

}


//// FIXME: Uncomment the above doesn't work, while the below example from the readme does work.
////FIXME: Only known difference is that
//import fi.oph.scalaschema._
//
//import org.json4s.JsonAST.JValue
//import org.json4s.jackson.JsonMethods
//
//object ExampleApp extends App {
//  case class Cat(name: String)
//
//  val schema: Schema = SchemaFactory.default.createSchema[Cat]
//  val schemaAsJson: JValue = schema.toJson
//  val schemaAsString = JsonMethods.pretty(schemaAsJson)
//  println(schemaAsString)
//}
//
