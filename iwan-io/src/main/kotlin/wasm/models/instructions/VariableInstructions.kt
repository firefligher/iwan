package dev.fir3.iwan.io.wasm.models.instructions

sealed interface VariableInstruction : Instruction

data class LocalGetInstruction(val localIndex: UInt) : VariableInstruction
data class LocalSetInstruction(val localIndex: UInt) : VariableInstruction
data class LocalTeeInstruction(val localIndex: UInt) : VariableInstruction

data class GlobalGetInstruction(val globalIndex: UInt) : VariableInstruction
data class GlobalSetInstruction(val globalIndex: UInt) : VariableInstruction
