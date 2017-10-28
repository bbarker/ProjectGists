object Utils {

  def revMap[K,V](map: Map[K,V]) = {
    val rMap = map.map(_.swap)
    assert(rMap.size == map.size)
    rMap
  }


}
