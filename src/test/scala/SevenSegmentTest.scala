import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import chisel3.util.HasBlackBoxPath

import scala.collection.immutable.Seq

// Should hav a shared abstract class for the testing
class SevenSegmentWrapper extends SevenSegmentSeconds {
  val bb = Module(new tt_um_seven_segment_top)

  val myReset = Wire(UInt(1.W))
  myReset := reset
  bb.io.ui_in := io.switches
  io.sevenSeg := bb.io.uo_out
  bb.io.uio_in := io.in
  io.out := bb.io.uio_out
  io.outEnable := bb.io.uio_oe
  bb.io.ena := io.ena
  bb.io.clk := clock
  bb.io.rst_n := !myReset
}
class tt_um_seven_segment_top extends HasBlackBoxPath {

  val io = IO(new Bundle {
    val ui_in = Input(UInt(8.W)) // Dedicated inputs - connected to the input switches
    val uo_out = Output(UInt(8.W)) // Dedicated outputs - connected to the 7 segment display
    val uio_in = Input(UInt(8.W)) // IOs: Bidirectional Input path
    val uio_out = Output(UInt(8.W)) // IOs: Bidirectional Output path
    val uio_oe = Output(UInt(8.W)) // IOs: Bidirectional Enable path (active high: 0=input, 1=output)
    val ena = Input(Bool()) // will go high when the design is enabled
    val clk = Input(Clock())
    val rst_n = Input(Bool())
  })
  addPath("./src/seven_segment_top.v")
  addPath("./src/SevenSegmentSeconds.v")
}
class SevenSegmentTest extends AnyFlatSpec with ChiselScalatestTester {
  val segments = Array(63, 6, 91, 79, 102, 109, 124, 7, 127, 103)

  "Seven segment" should "pass" in {
    test(new SevenSegmentSeconds(10))  {
      dut => {
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

  "Seven segment Verilog Top" should "pass" in {
    SevenSegmentSeconds.main(Array(""))
    emitVerilog(new SevenSegmentSeconds(10), Array("--target-dir", "src"))

    test(new SevenSegmentWrapper()).withAnnotations(Seq(VerilatorBackendAnnotation)) {
      dut => {
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
