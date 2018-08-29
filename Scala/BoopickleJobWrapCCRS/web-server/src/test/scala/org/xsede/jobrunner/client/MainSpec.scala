package org.xsede.jobrunner.model

import org.scalatest._
import org.xsede.jobrunner.model.MainApi._

import scala.concurrent.Future
import scala.language.implicitConversions
import boopickle.shapeless.Default._

@SuppressWarnings(Array("org.wartremover.warts.PublicInference"))
class MainSpec extends FlatSpec with Matchers {
  behavior of "boopickling various API data structures"

  val meta: SysJobMetaData = SysJobMetaData(
    shell = Some("bash"), // TODO: make this a parameter, somewhere
    containerId = None,
    image = None,
    user = None,
    address = None,
    hostname = None,
    url = Some("http://127.0.0.1")
  )
  val oneShot: OneShot = OneShot(JobId(), "ls -l", meta)

  it should "not throw an error" in {
    Pickle.intoBytes(oneShot)
  }

  val oneShotJw = JobWrap(oneShot)

  it should "not throw an error when JobWrapped" in {
    Pickle.intoBytes(oneShotJw)
  }

}
