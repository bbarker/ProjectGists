import Utils._


trait FeedGroupType {
  override def toString = FeedGroupType.mapToStr(this)
}
object FeedGroupType {
  val mapToStr: Map[FeedGroupType, String] = Map(
    HeiferFeedGroup -> "Heifer",
    DryCowFeedGroup -> "Dry Cow",
    LactatingFeedGroup -> "Lactating"
  )
  val mapFromStr: Map[String, FeedGroupType] = revMap(mapToStr)
  def apply(value: String): FeedGroupType = mapFromStr.get(value) match {
    case Some(tpe: FeedGroupType) => tpe
    case what => throw new RuntimeException(
      s"fromString returned unexpected value $what for input $value"
    )
  }
  lazy val values: Iterable[FeedGroupType] = mapToStr.keys
}
case object HeiferFeedGroup extends FeedGroupType
case object DryCowFeedGroup extends FeedGroupType
case object LactatingFeedGroup extends FeedGroupType



