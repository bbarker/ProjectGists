package edu.cornell.cac.sbh

import java.io.BufferedReader
import java.io.InputStreamReader
import scala.io.StdIn

object Util {


  private def readLine(format: Option[String], args: Any*): String = {
    println(fansi.Color.Red(
      "ALERT: no console found, password will be shown on console !!!!"
    ))
    print(String.format(format.getOrElse(""), args))
    val tmp = StdIn.readLine(format.getOrElse(""), args: _*)
    println("finished reading line")
    tmp // FIXME: DEBUG
  }

  def readPassword(format: Option[String], args: Any*): Array[Char] = {
    if (System.console != null) {
      System.console.readPassword(format.getOrElse(""), args)
    }
    else readLine(format, args).toCharArray
  }
}
