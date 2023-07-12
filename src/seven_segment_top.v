`default_nettype none

module seven_segment_top (
    input  wire [7:0] ui_in,    // Dedicated inputs - connected to the input switches
    output wire [7:0] uo_out,   // Dedicated outputs - connected to the 7 segment display
    input  wire [7:0] uio_in,   // IOs: Bidirectional Input path
    output wire [7:0] uio_out,  // IOs: Bidirectional Output path
    output wire [7:0] uio_oe,   // IOs: Bidirectional Enable path (active high: 0=input, 1=output)
    input  wire       ena,      // will go high when the design is enabled
    input  wire       clk,      // clock
    input  wire       rst_n     // reset_n - low to reset
);

    wire reset = !rst_n;
    // Just wrap the Chisel generated Verilog
    SevenSeg SevenSegmentSeconds(.clock(clk),
      .reset(reset),
      .io_switches(ui_in),
      .io_sevenSeg(uo_out),
      .io_in(uio_in),
      .io_out(uio_out),
      .io_outEnable(uio_oe),
      .io_ena(ena));

endmodule
