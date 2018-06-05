package edu.cornell.cac.sbh
import utest._

object TestDummy extends TestSuite {
  val tests = Tests {
    'test1 - {
      println("hello from test")
    }
  }
}