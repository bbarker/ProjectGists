using lib "dev.zio::zio-streams:2.0.10"

import zio.*
import zio.stream.*

object StreamExample extends ZIOAppDefault {
  override val run = {
    val points = Chunk(5, 10, 15, 20, 100, 200, 300)

    val natStream = ZStream.iterate(0)(_ + 1)

    // Stream is restarted for each take, as expected
    ZIO.foreach(points)(point => natStream.chunk.runCollect.debug)
  }
}

StreamExample.main(Array.empty)
