using lib "dev.zio::zio-streams:2.0.9" // Doesn't work in 2.0.10
using lib "dev.zio::zio-test:2.0.9"

// 2.0.9
// val sample: ZStream[Any, Nothing, Option[Sample[Any, PromoCodeClaim]]]
//
//
// 2.0.10
// val sample: ZStream[Any, Nothing, Sample[Any, PromoCodeClaim]]

import zio.*
import zio.stream.*
import zio.test.*

object ZIOSampleOptionFlatten extends ZIOAppDefault {

  override val run = {
    for
      intSampleOpt <- Gen
        .int(0, 10)
        .sample
        .take(1)
        .runCollect
      // .map(_.headOption.flatten)
      intSample <- ZIO
        .fromOption(intSampleOpt.headOption)
        .mapError(_ => new RuntimeException("bam"))
        .orDie
    yield intSample.value
  }
}

ZIOSampleOptionFlatten.main(Array.empty)
