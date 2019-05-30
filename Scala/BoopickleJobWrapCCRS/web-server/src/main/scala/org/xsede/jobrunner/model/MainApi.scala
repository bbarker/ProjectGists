package org.xsede.jobrunner.model

import scala.concurrent.Future
import scala.language.implicitConversions
import boopickle.shapeless.Default._

@SuppressWarnings(Array("org.wartremover.warts.PublicInference"))
object MainApi {

  import Job._
  import JobWrap._
  import OneShot._

  sealed trait JobWrap {
    type J
    val job: J
    //val ev: Job[J]
  }
  object JobWrap {
    def apply[JB](jobIn: JB)(implicit evIn: Job[JB]): JobWrap = new JobWrap {
      type J = JB
      val job: JB = jobIn
      //val ev: Job[JB] = evIn
    }

//    def unapply(arg: JobWrap): Option[(arg.J, Job[arg.J])]
//    = Some((arg.job, arg.ev))

    implicit val jwPickler: Pickler[JobWrap] = generatePickler[JobWrap]

  }

  // Value class currently doesn't work with autowire
  final case class JobId(id: String)

  object JobId {
    def apply(): JobId = JobId("this is fake")
  }

  /**
    *
    * @param user  A record of the user name or id (if logged in).
    * @param address IP address of client.
    * @param hostname Hostname of client.
    * @param url Client-supplied url of the page from which the command request
    *            originated.
    */
  sealed trait JobMetaData {
    val user: Option[String] = None
    val address: Option[String] = None
    val hostname: Option[String] = None
    val url: Option[String] = None
  }

  /**
    * A concrete case class for the trait CommandMetaData.
    *
    * @param user  A record of the user name or id (if logged in).
    * @param address IP address of client.
    * @param hostname Hostname of client.
    * @param url Client-supplied url of the page from which the command request
    *            originated.
    */
  final case class PartialJobMetaData(
                                       override val user: Option[String],
                                       override val address: Option[String],
                                       override val hostname: Option[String],
                                       override val url: Option[String],
                                     ) extends JobMetaData

  /**
    *
    * @param shell The specified shell to run in. If none, will be
    *              executed without a shell
    * @param containerId Will attempt to use an existing container if
    *                    specified.
    * @param image The container image that is used. A new container will
    *              be started using the existing image, if containerId is not
    *              specified as well.
    * @param user  A record of the user name or id (if logged in).
    * @param address IP address of client.
    * @param hostname Hostname of client.
    * @param url Client-supplied url of the page from which the command request
    *            originated.
    */
  final case class SysJobMetaData(
                                   shell: Option[String],
                                   containerId: Option[String],
                                   image: Option[String],
                                   override val user: Option[String],
                                   override val address: Option[String],
                                   override val hostname: Option[String],
                                   override val url: Option[String],
                                 ) extends JobMetaData


  /**
    *
    * @param processId Will attempt to run the command in the specified interpreter.
    *                  Note that this is not a system process ID, but the ID given
    *                  to the process by CCRS.  * @param user  A record of the user name or id (if logged in).
    * @param address IP address of client.
    * @param hostname Hostname of client.
    * @param url Client-supplied url of the page from which the command request
    *            originated.
    */
  final case class ReplJobMetaData(
                                    processId: Option[Int],
                                    override val user: Option[String],
                                    override val address: Option[String],
                                    override val hostname: Option[String],
                                    override val url: Option[String],
                                  ) extends JobMetaData

  /**
    * Base Command type. Subclasses of this type specify how the command is run.
    * @param id
    * @param cmd The command to run
    * @param meta
    */
  //@JsonCodec // FIXME
  sealed trait Job[J] {
    def id(jb: J): JobId
    def cmd(jb: J): String
    type M <: JobMetaData
    def meta(jb: J): M
    //def encodeJ: Encoder[J]
  }
  object Job{
    type Aux[J0, M0] = Job[J0] {type M = M0}
    implicit class JobDispatch[J](job: J){
      def id(implicit ev: Job[J]): JobId = ev.id(job)
      def cmd(implicit ev: Job[J]): String = ev.cmd(job)
      def meta(implicit ev: Job[J]): ev.M = ev.meta(job)
      // def encode(implicit ev: Job[J]): Json = ev.encodeJ(job)
      //def encode(enc: Encoder[J]): Json = enc(job)
    }
  }

  // Value classes don't work with autowire
  final case class JobRelPath(value: String) // extends AnyVal

  //sealed trait FileCommand extends Command {
  //  val fileContents: Map[JobRelPath, String]
  //}

  /**
    * The command will be run in a specified environment, and then return.
    */
  final case class OneShot(id: JobId, cmd: String, meta: SysJobMetaData) {
    type M = SysJobMetaData
  }

  object OneShot{
    implicit val jobOneShot: Job.Aux[OneShot, SysJobMetaData] = new Job[OneShot] {
      def id(jb: OneShot): JobId = jb.id
      def cmd(jb: OneShot): String = jb.cmd
      override type M = SysJobMetaData
      def meta(jb: OneShot): M = jb.meta
    }
  }


}
