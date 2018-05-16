package edu.cornell.cac.sbh

import edu.cornell.cac.sbh.Util.readPassword

import utest._

object TestReadline extends TestSuite {
  val tests = Tests {
    'readLine - {
       print("Password> ")
       readPassword(None)
        print("Finished")
    }
  }
}