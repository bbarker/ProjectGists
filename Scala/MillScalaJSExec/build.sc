import mill._
import mill.scalalib._
import mill.scalajslib._

object client extends ScalaJSModule {
  def scalaVersion = "2.12.4"
  def scalaJSVersion = "0.6.22"
  def ivyDeps = Agg(
    ivy"org.scala-js::scalajs-dom::0.9.4",
    ivy"in.nvilla::monadic-html::0.4.0-RC1"
  )
}