import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class SevenSegmenntTest extends AnyFlatSpec with ChiselScalatestTester {
  "Seven segment" should "pass" in {
    test(new SevenSegmentSeconds(10))  {
      dut => {
        val segments = Array(63, 6, 91, 79, 102, 109, 124, 7, 127, 103)

        for (i <- 0 until 10) {
          dut.clock.step(10)
          // check all segments
          dut.io.sevenSeg.expect(segments(i))
          // all bidirectionals are set to output
          dut.io.outEnable.expect(0xff)
        }
      }
    }
  }
}
