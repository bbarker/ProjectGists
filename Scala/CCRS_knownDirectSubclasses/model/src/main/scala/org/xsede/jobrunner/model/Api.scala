package org.xsede.jobrunner.model

import java.util.UUID

import scala.concurrent.Future

import org.xsede.jobrunner.Constants._

trait Api {
  def exec(data: List[Command]): Future[List[RunResult]]

  def poll(id: CommandId): Future[RunResult]
}


// Value class currently doesn't work with autowire
case class CommandId(id: UUID) // extends AnyVal

object CommandId {
  def apply() = new CommandId(UUID.randomUUID)
}

sealed trait CommandMetaData {
  val user: Option[String] = None
  val address: Option[String] = None
  val hostname: Option[String] = None
  val url: Option[String] = None
}

final case class PartialCmdMetaData(
  override val user: Option[String] = None,
  override val address: Option[String] = None,
  override val hostname: Option[String] = None,
  override val url: Option[String] = None,
) extends CommandMetaData


final case class SysCmdMetaData(
  shell: Option[String] = None,
  containerId: Option[String] = None,
  image: Option[String] = None,
  override val user: Option[String] = None,
  override val address: Option[String] = None,
  override val hostname: Option[String] = None,
  override val url: Option[String] = None,
) extends CommandMetaData

sealed trait Command {
  val id: CommandId
  val body: String
  type M <: CommandMetaData
  val meta: M
}

// Value classes don't work with autowire
case class JobRelPath(value: String) // extends AnyVal

sealed trait FileCommand extends Command {
  val fileContents: Map[JobRelPath, String]
}

final case class ExecFile(
  id: CommandId,
  body: String,
  meta: SysCmdMetaData,
  fileContents: Map[JobRelPath, String]
) extends FileCommand {
  override type M = SysCmdMetaData
}

case class RunResult(out: String, err: String, exitCode: Int, appErr: String = "")

object RunResult {


  def fakeResult(error: String, code: Int = NON_ERROR): RunResult =
    RunResult(out = "", err = "", exitCode = code, appErr = error)

}

case class CommandWithResult(command: Command, result: RunResult)
