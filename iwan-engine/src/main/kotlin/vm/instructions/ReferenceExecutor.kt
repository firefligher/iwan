package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.models.FunctionReference
import dev.fir3.iwan.engine.vm.stack.Stack
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
            .currentModule
            .functionAddresses[instruction.functionIndex.toInt()]

        stack.pushReference(FunctionReference(address))
    }
}