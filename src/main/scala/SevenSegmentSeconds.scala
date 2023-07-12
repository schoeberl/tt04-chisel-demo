import chisel3._

/**
 * Example design in Chisel.
 * A redesign of the Tiny Tapeout example.
 */
class SevenSegmentSeconds(MaxCount: Int = 10000000) extends Module {
  val io = IO(new Bundle {
    val switches = Input(UInt(8.W))   // Dedicated inputs - connected to the input switches
    val sevenSeg = Output(UInt(8.W))  // Dedicated outputs - connected to the 7 segment display
    val in = Input(UInt(8.W))          // IOs: Bidirectional Input path
    val out = Output(UInt(8.W))       // IOs: Bidirectional Output path
    val outEnable = Output(UInt(8.W)) // IOs: Bidirectional Enable path (active high: 0=input, 1=output)
    val ena = Input(Bool())           // will go high when the design is enabled
  })
/*
input  wire [7:0] ui_in,    // Dedicated inputs - connected to the input switches
output wire [7:0] uo_out,   // Dedicated outputs - connected to the 7 segment display
input  wire [7:0] uio_in,   // IOs: Bidirectional Input path
output wire [7:0] uio_out,  // IOs: Bidirectional Output path
output wire [7:0] uio_oe,   // IOs: Bidirectional Enable path (active high: 0=input, 1=output)
input  wire       ena,      // will go high when the design is enabled
 */
  // have default outputs
  io.sevenSeg := 0.U
  io.out := 0.U
  // use bi-directionals as outputs
  io.outEnable := "hff".U

  // external clock is 10 MHz, so we need a 24 bit counter
  val secondCnterReg = RegInit(0.U(24.W))
  val digitReg = RegInit(0.U(4.W))

  secondCnterReg := secondCnterReg + 1.U
  when (secondCnterReg === MaxCount.U) {
    secondCnterReg := 0.U
    digitReg := digitReg + 1.U
    when (digitReg === 9.U) {
      digitReg := 0.U
    }
  }

  val dec = Module(new Decoder)
  dec.counter := digitReg
  io.sevenSeg := 0.U ## dec.segments
}

object SevenSegmentSeconds extends App {
  emitVerilog(new SevenSegmentSeconds(), Array("--target-dir", "src"))
}