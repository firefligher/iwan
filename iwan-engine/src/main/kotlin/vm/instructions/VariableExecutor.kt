package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.models.stack.StackValue
import dev.fir3.iwan.engine.vm.Stack
import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.io.wasm.models.instructions.*

object VariableExecutor : InstructionExecutionContainer {
    @InstructionExecutor(UniqueIds.GLOBAL_GET)
    @JvmStatic
    fun execGlobalGet(
        store: Store,
        stack: Stack,
        instruction: GlobalGetInstruction
    ) {
        val globalAddress = stack
            .currentFrame
            .module
            .globalAddresses[instruction.globalIndex.toInt()]

        val value = store.globals[globalAddress].value
        stack.push(StackValue(value))
    }

    @InstructionExecutor(UniqueIds.GLOBAL_SET)
    @JvmStatic
    fun execGlobalSet(
        store: Store,
        stack: Stack,
        instruction: GlobalSetInstruction
    ) {
        val globalAddress = stack
            .currentFrame
            .module
            .globalAddresses[instruction.globalIndex.toInt()]

        store.globals[globalAddress].value = (Stack.pop() as StackValue).value
    }

    @InstructionExecutor(UniqueIds.LOCAL_GET)
    @JvmStatic
    fun execLocalGet(stack: Stack, instruction: LocalGetInstruction) {
        val value = stack
            .currentFrame
            .locals[instruction.localIndex.toInt()]

        stack.push(StackValue(value))
    }

    @InstructionExecutor(UniqueIds.LOCAL_SET)
    @JvmStatic
    fun execLocalSet(stack: Stack, instruction: LocalSetInstruction) {
        stack.currentFrame.locals[instruction.localIndex.toInt()] =
            (Stack.pop() as StackValue).value
    }

    @InstructionExecutor(UniqueIds.LOCAL_TEE)
    @JvmStatic
    fun execLocalTee(stack: Stack, instruction: LocalTeeInstruction) {
        val stackValue = Stack.pop() as StackValue

        Stack.push(stackValue)
        stack.currentFrame.locals[instruction.localIndex.toInt()] =
            stackValue.value
    }
}
