package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.models.Int32Value
import dev.fir3.iwan.engine.models.stack.StackLabel
import dev.fir3.iwan.engine.models.stack.StackValue
import dev.fir3.iwan.engine.vm.Stack
import dev.fir3.iwan.io.wasm.models.instructions.*
import dev.fir3.iwan.io.wasm.models.instructions.blockTypes.EmptyBlockType
import dev.fir3.iwan.io.wasm.models.instructions.blockTypes.FunctionBlockType
import dev.fir3.iwan.io.wasm.models.instructions.blockTypes.InlineBlockType

object BlockExecutor : InstructionExecutionContainer {
    fun execBlockType(
        stack: Stack,
        instruction: BlockTypeInstruction,
        body: List<Instruction>,
        isLoop: Boolean
    ) {
        val parameterCount: Int
        val resultCount: Int

        when (val blockType = instruction.type) {
            is EmptyBlockType -> {
                parameterCount = 0
                resultCount = 0
            }

            is FunctionBlockType -> {
                val type = stack
                    .currentFrame
                    .module
                    .types[blockType.value.toInt()]

                parameterCount = type.parameterTypes.size
                resultCount = type.resultTypes.size
            }

            is InlineBlockType -> {
                parameterCount = 0
                resultCount = 1
            }
        }

        val parameters = parameterCount
            .downTo(1)
            .map { Stack.pop() }

        stack.push(StackLabel(resultCount, body, 0, isLoop))
        parameters.reversed().forEach(Stack::push)
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
        val condition = ((stack.pop() as StackValue).value as Int32Value).value

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
