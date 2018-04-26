package org.xsede.jobrunner

object Constants {

  // Error codes (mostly to avoid dealing with Option at the level of RunResult).
  // These should NOT be used in logic, and are meant only as a convenience for
  // reporting.
  val NON_ERROR: Int = -888
  val NONFATAL_APP_ERR: Int = -999
  val FATAL_APP_ERR: Int = -9999

}
