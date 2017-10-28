import com.softwaremill.quicklens._


case class SimulationConfig(estConfig1: String, testConfig2: Option[Int])
class TestModifiers {

  private var globalConfig = Option(SimulationConfig("hi", Some(5)))

  def scModify[U](path: scala.Function1[SimulationConfig, U], cfgItem: U): Unit = {
    globalConfig match {
      case Some(sc) =>
        globalConfig = Option(sc.modify(path).setTo(cfgItem))

      case None => throw new RuntimeException("no SimulationConfig found for update!")
    }
  }
}
