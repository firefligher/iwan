package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.models.Int32Value
import dev.fir3.iwan.engine.models.stack.StackValue
import dev.fir3.iwan.engine.vm.Stack
import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.io.wasm.models.instructions.MemoryInitInstruction
import dev.fir3.iwan.io.wasm.models.instructions.UniqueIds

object MemoryExecutor : InstructionExecutionContainer {
    @InstructionExecutor(UniqueIds.MEMORY_INIT)
    @JvmStatic
    fun execInit(
        store: Store,
        stack: Stack,
        instruction: MemoryInitInstruction
    ) {
        val memoryAddress = stack
            .currentFrame
            .module
            .memoryAddresses[0]

        val dataAddress = stack
            .currentFrame
            .module
            .data[instruction.dataIndex.toInt()]

        val memory = store.memories[memoryAddress].data
        val data = store.data[dataAddress].data

        val count = ((stack.pop() as StackValue).value as Int32Value).value
        val srcIndex = ((stack.pop() as StackValue).value as Int32Value).value
        val dstIndex = ((Stack.pop() as StackValue).value as Int32Value).value

        val trap = (srcIndex + count > data.size) or
                (dstIndex + count > memory.size)

        if (trap) {
            throw IllegalStateException(
                "Memory initialization exceeds boundaries"
            )
        }

        data.copyInto(memory, dstIndex, srcIndex, srcIndex + count)
    }
}