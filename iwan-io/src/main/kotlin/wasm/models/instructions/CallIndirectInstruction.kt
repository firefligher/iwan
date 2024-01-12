package dev.fir3.iwan.io.wasm.models.instructions

data class CallIndirectInstruction(
    val typeIndex: UInt,
    val tableIndex: UInt
): Instruction
