import chisel3._
import chisel3.util._

/*
      -- 1 --
     |       |
     6       2
     |       |
      -- 7 --
     |       |
     5       3
     |       |
      -- 4 --
*/
class Decoder extends Module {
  /*
  val io = IO(new Bundle {
    val counter = Input(UInt(4.W))
    val segments = Output(UInt(7.W))
  })

   */
  val counter = IO(Input(UInt(4.W)))
  val segments = IO(Output(UInt(7.W)))
  /*
  always @(*) begin
      case(counter)
          //                7654321
          0:  segments = 7'b0111111;
          1:  segments = 7'b0000110;
          2:  segments = 7'b1011011;
          3:  segments = 7'b1001111;
          4:  segments = 7'b1100110;
          5:  segments = 7'b1101101;
          6:  segments = 7'b1111100;
          7:  segments = 7'b0000111;
          8:  segments = 7'b1111111;
          9:  segments = 7'b1100111;
          default:
              segments = 7'b0000000;
      endcase
  end
   */
  segments := 0.U
  switch (counter) {
    is(0.U) {
      segments := "b0111111".U
    }
    is(1.U) {
      segments := "b0000110".U
    }
    is(2.U) {
      segments := "b1011011".U
    }
    is(3.U) {
      segments := "b1001111".U
    }
    is(4.U) {
      segments := "b1100110".U
    }
    is(5.U) {
      segments := "b1101101".U
    }
    is(6.U) {
      segments := "b1111100".U
    }
    is(7.U) {
      segments := "b0000111".U
    }
    is(8.U) {
      segments := "b1111111".U
    }
    is(9.U) {
      segments := "b1100111".U
    }
  }
}
