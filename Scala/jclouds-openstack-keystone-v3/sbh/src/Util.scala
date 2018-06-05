package edu.cornell.cac.sbh

import scala.io.StdIn

object Util {


  def readLine(format: Option[String], args: Any*): Array[Char] = {
    println(fansi.Color.Red(
      "ALERT: no console found, password will be shown on console !!!!"
    ))
    print(String.format(format.getOrElse(""), args))
    val tmp = StdIn.readLine(format.getOrElse(""), args: _*)
    println("finished reading line")
    tmp.toCharArray // FIXME: DEBUG
  }

  def readPassword(format: Option[String], args: Any*): Array[Char] = {
    if (System.console != null) {
      System.console.readPassword(format.getOrElse(""), args)
    }
    else readLine(format, args)
  }
}
