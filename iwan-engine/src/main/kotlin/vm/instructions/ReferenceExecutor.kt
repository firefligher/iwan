package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.models.FunctionReference
import dev.fir3.iwan.engine.models.stack.StackValue
import dev.fir3.iwan.engine.vm.Stack
import dev.fir3.iwan.io.wasm.models.instructions.ReferenceFunctionInstruction
import dev.fir3.iwan.io.wasm.models.instructions.UniqueIds

object ReferenceExecutor : InstructionExecutionContainer {
    @InstructionExecutor(UniqueIds.REFERENCE_FUNCTION)
    @JvmStatic
    fun execReferenceFunction(
        stack: Stack,
        instruction: ReferenceFunctionInstruction
    ) {
        val address = stack
            .currentFrame
            .module
            .functionAddresses[instruction.functionIndex.toInt()]

        stack.push(StackValue(FunctionReference(address)))
    }
}