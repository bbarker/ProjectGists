package org.xsede.jobrunner

object Syntax {

  /**
    * Use like this: discard{ badMutateFun(foo) }
    */
  @specialized def discard[A](evaluateForSideEffectOnly: A): Unit = {
    val _: A = evaluateForSideEffectOnly
    () //Return unit to prevent warning due to discarding value
  }


}
