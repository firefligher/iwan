package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.engine.vm.stack.Stack
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
            .currentModule
            .memoryAddresses[0]

        val dataAddress = stack
            .currentModule
            .data[instruction.dataIndex.toInt()]

        val memory = store.memories[memoryAddress].data
        val data = store.data[dataAddress].data

        val count = stack.popInt32()
        val srcIndex = stack.popInt32()
        val dstIndex = stack.popInt32()

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