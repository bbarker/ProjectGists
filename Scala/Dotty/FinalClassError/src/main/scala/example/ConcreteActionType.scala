package example

sealed case class ConcreteActionType(name: String) extends Action(name)