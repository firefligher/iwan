package dev.fir3.iwan.io.wasm.models.instructions

sealed interface BranchInstruction : Instruction

data class UnconditionalBranch(val labelIndex: UInt): BranchInstruction
data class ConditionalBranch(val labelIndex: UInt): BranchInstruction
data class TableBranch(
    val labelIndices: List<UInt>,
    val tableIndex: UInt
): BranchInstruction
