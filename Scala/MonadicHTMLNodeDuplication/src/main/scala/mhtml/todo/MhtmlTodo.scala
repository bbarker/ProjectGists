// This implementation is mostly copied from Binding.scala TodoMVC example:
// https://github.com/ThoughtWorksInc/todo/blob/master/js/src/main/scala/com/thoughtworks/todo/Main.scala
package mhtml.todo
import scala.scalajs.js.JSApp
import scala.xml.{Elem, Group, Node}
import mhtml._
import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.KeyboardEvent
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.ext.LocalStorage
import org.scalajs.dom.raw.HTMLInputElement
import upickle.default.read
import upickle.default.write

import scala.collection.breakOut

object MhtmlTodo extends JSApp {

  implicit class RxNode(val rxNode: Rx[Node]) extends AnyVal {
    def toNode(errNode: Node = <div>Error/404</div>): Node = {
      val nodeOuter: Elem = <div>{ rxNode.dropRepeats }</div>
      nodeOuter.child.headOption match {
        case Some(nd) => <div class="debug">{ nd }</div>
        case None => errNode
      }
    }
  }

  implicit class IterableMhtml[T, C[X] <: Iterable[X]](val list: C[T]) extends AnyVal {
    def mapToNode(fn: T => Node): Node =
      if (list.size > 1)
        Group( list.map(item => fn(item))(breakOut) )
      else list.headOption match {
        case Some(tv) => fn(tv)
        case None => <div></div>
      }
  }

  /**
    * A recursive, component-level router. In general each component should define its own route
    * if a route is needed at all.
    */
  case class Router(remainingPath: Rx[String], route: Rx[String] => Node) {
    val view: Node = route(remainingPath)

  }
  object Router {

    val routePath: Rx[String] = Rx(dom.window.location.hash).merge{
      val updatedHash = Var(dom.window.location.hash)
      dom.window.onhashchange = (ev: Event) => {
        updatedHash := dom.window.location.hash
      }
      updatedHash.map(hash => hash.replaceFirst("#/", ""))
    }


    //TODO: handle case where not splittable

    /**
      * Utiltiy to split route
      * @param routeInRx
      * @return (current route, child route)
      */
    def splitRoute(routeInRx: Rx[String]): (Rx[String], Rx[String]) = {
      val splitRx = routeInRx.map { routeIn =>
        val (fst, snd) =
          if (routeIn.contains("/"))
            routeIn.splitAt(routeIn.indexOfSlice("/"))
          else (routeIn, "")
        (fst, snd.replaceFirst("/", ""))
      }
      (splitRx.map(tup => tup._1), splitRx.map(tup => tup._2))
    }
  }


  val hello: Rx[Node] = Rx(<h3>Hello!</h3>)

  val dynamicNode: Rx[Node] = hello.map(hi => <div class="hi-div">{ hi } <h2>{ Router.routePath }</h2> </div>)


  // There seems to be no problem here if we only mount View,
  // so it is unlikely to be an issue with toNode
  val View: Node = dynamicNode.toNode()


  def counterNode: xml.Node = {
    val counter = Var(0)
    <div>
      <button onclick={() => counter.update(_ - 1)}>-</button>
      {counter}
      <button onclick={() => counter.update(_ + 1)}>+</button>
    </div>
  }

  private def thisRoute(path: Rx[String]): Node = {
    println("calling thisRoute")
    val (curPathRx, childPathRx) = Router.splitRoute(path)
    val nodeRx: Rx[Node] = curPathRx.map{curPath: String =>
      if (curPath == "counter") counterNode
      else View
      //TODO add check on codebook handle above?
      //else Rx(<div>Make An Error page</div>)
    }
    nodeRx.toNode()
  }

  def main(): Unit = {

    val router = Router(Router.routePath, thisRoute)
    val div = dom.document.getElementById("application-container")
    mount(div, router.view)
  }
}
