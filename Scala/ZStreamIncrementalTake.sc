using lib "dev.zio::zio-streams:2.0.10"

import zio.*
import zio.stream.*

object StreamExample extends ZIOAppDefault {
  def customChunks[R, E, A](
      chunkSizes: List[Int] // Should be positive ints
  )(
      inStream: ZStream[R, E, A]
  ) =
    inStream
      .map(Chunk(_))
      .mapAccum((chunkSizes, Chunk.empty[A]))((current, elem) => {
        val chunkSizes = current._1
        val currentChunk = current._2
        val sizeRemaining = chunkSizes.headOption match
          case Some(sz) => sz - 1
          case None     => 0
        val chunkSizesNext = chunkSizes match
          case Nil => Nil
          case _ :: tail =>
            if sizeRemaining == 0 then tail
            else sizeRemaining :: tail
        val chunkNext =
          if sizeRemaining == 0 then Chunk.empty else currentChunk ++ elem
        val elemNext =
          if sizeRemaining == 0 then currentChunk ++ elem else Chunk.empty
        ((chunkSizesNext, chunkNext), elemNext)
      })
      .filter(_.nonEmpty)

  def takeChunks[R, E, A](
      chunkSizes: List[Int] // Should be positive ints
  )(
      inStream: ZStream[R, E, A]
  ) = customChunks(chunkSizes)(inStream).take(chunkSizes.size)

  override val run = {
    val points = List(1, 2, 5)

    val natStream = ZStream.iterate(0)(_ + 1)

    // Stream is restarted for each take, as expected
    // ZIO.foreach(points)(point => natStream.chunk.runCollect.debug)
    takeChunks(points)(natStream).runCollect.debug

  }
}

StreamExample.main(Array.empty)
