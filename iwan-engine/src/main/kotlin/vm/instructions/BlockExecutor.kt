package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.vm.stack.Stack
import dev.fir3.iwan.io.wasm.models.instructions.*
import dev.fir3.iwan.io.wasm.models.instructions.blockTypes.EmptyBlockType
import dev.fir3.iwan.io.wasm.models.instructions.blockTypes.FunctionBlockType
import dev.fir3.iwan.io.wasm.models.instructions.blockTypes.InlineBlockType

object BlockExecutor : InstructionExecutionContainer {
    private fun execBlockType(
        stack: Stack,
        instruction: BlockTypeInstruction,
        body: List<Instruction>,
        isLoop: Boolean
    ) {
        val resultTypes = when (val blockType = instruction.type) {
            is EmptyBlockType -> emptyList()
            is FunctionBlockType -> {
                val type = stack
                    .currentModule
                    .types[blockType.value.toInt()]

                check(type.parameterTypes.isEmpty()) { "Block has parameters" }
                type.resultTypes
            }

            is InlineBlockType -> {
                listOf(blockType.valueType)
            }
        }

        stack.pushLabel(resultTypes, body, isLoop)
    }

    @InstructionExecutor(UniqueIds.BLOCK)
    @JvmStatic
    fun execBlock(stack: Stack, instruction: BlockInstruction) =
        execBlockType(stack, instruction, instruction.body, false)

    @InstructionExecutor(UniqueIds.CONDITIONAL_BLOCK)
    @JvmStatic
    fun execConditional(
        stack: Stack,
        instruction: ConditionalBlockInstruction
    ) {
        val condition = stack.popInt32()

        if (condition != 0) {
            execBlockType(stack, instruction, instruction.ifBody, false)
        } else {
            instruction.elseBody?.let { body ->
                execBlockType(stack, instruction, body, false)
            }
        }
    }

    @InstructionExecutor(UniqueIds.LOOP)
    @JvmStatic
    fun execLoop(stack: Stack, instruction: LoopInstruction) =
        execBlockType(stack, instruction, instruction.body, true)
}
