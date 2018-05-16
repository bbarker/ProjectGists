package io.github.bbarker

import mhtml._
import org.scalajs.dom
import scala.scalajs.js.JSApp
import scala.xml.Node


object Main /*extends JSApp */ {

  val mainDiv: Node = <div>
    Hello world!
  </div>

  //
  //  This works:
  //

  //  def main(): Unit = {
  //    println("Hello from main")
  //    mount(dom.document.body, mainDiv)
  //    ()
  //  }

  def main(): Unit = {
    println("Hello from main")
    mount(dom.document.body, mainDiv)
    ()
  }
}

