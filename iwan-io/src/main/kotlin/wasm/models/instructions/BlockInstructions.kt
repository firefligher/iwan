package dev.fir3.iwan.io.wasm.models.instructions

import dev.fir3.iwan.io.wasm.models.instructions.blockTypes.BlockType

sealed interface BlockTypeInstruction : Instruction {
    val type: BlockType
}

data class BlockInstruction(
    override val type: BlockType,
    val body: List<Instruction>
) : BlockTypeInstruction

data class ConditionalBlockInstruction(
    override val type: BlockType,
    val ifBody: List<Instruction>,
    val elseBody: List<Instruction>?
) : BlockTypeInstruction

data class LoopInstruction(
    override val type: BlockType,
    val body: List<Instruction>
) : BlockTypeInstruction
