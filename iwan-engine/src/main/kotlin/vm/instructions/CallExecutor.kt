package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.models.FunctionReference
import dev.fir3.iwan.engine.models.Int32Value
import dev.fir3.iwan.engine.models.ReferenceNull
import dev.fir3.iwan.engine.models.stack.StackValue
import dev.fir3.iwan.engine.vm.Invocation
import dev.fir3.iwan.engine.vm.Stack
import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.io.wasm.models.instructions.CallIndirectInstruction
import dev.fir3.iwan.io.wasm.models.instructions.CallInstruction
import dev.fir3.iwan.io.wasm.models.instructions.UniqueIds

object CallExecutor : InstructionExecutionContainer {
    @InstructionExecutor(UniqueIds.CALL)
    @JvmStatic
    fun exec(stack: Stack, instruction: CallInstruction) {
        val functionAddress = stack
            .currentFrame
            .module
            .functionAddresses[instruction.functionIndex.toInt()]

        Invocation
            .invokeFunction(functionAddress)
            .reversed()
            .map(::StackValue)
            .forEach(Stack::push)
    }

    @InstructionExecutor(UniqueIds.CALL_INDIRECT)
    @JvmStatic
    fun execIndirect(
        store: Store,
        stack: Stack,
        instruction: CallIndirectInstruction
    ) {
        val tableAddress = stack
            .currentFrame
            .module
            .tableAddresses[instruction.tableIndex.toInt()]

        val functionType = stack
            .currentFrame
            .module
            .types[instruction.typeIndex.toInt()]

        val table = store.tables[tableAddress].elements
        val index = ((stack.pop() as StackValue).value as Int32Value).value

        if (index >= table.size) {
            throw IllegalStateException("Invalid indirection")
        }

        val reference = table[index]

        if (reference is ReferenceNull) {
            throw IllegalStateException("Null pointer indirection")
        }

        // TODO: More security checks regarding type.

        val functionAddress = (reference as FunctionReference).functionAddress

        Invocation
            .invokeFunction(functionAddress)
            .reversed()
            .map(::StackValue)
            .forEach(Stack::push)
    }
}