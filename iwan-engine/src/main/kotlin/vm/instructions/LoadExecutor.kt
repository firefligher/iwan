package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.models.Int32Value
import dev.fir3.iwan.engine.models.NumberValue
import dev.fir3.iwan.engine.models.stack.StackValue
import dev.fir3.iwan.engine.vm.Stack
import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.io.wasm.models.instructions.Int32Load8UInstruction
import dev.fir3.iwan.io.wasm.models.instructions.Int32LoadInstruction
import dev.fir3.iwan.io.wasm.models.instructions.UniqueIds

object LoadExecutor : InstructionExecutionContainer {
    private inline fun <
            reified TValue,
            reified TWrapperValue : NumberValue<TValue>
    > execLoad(
        store: Store,
        stack: Stack,
        instructionOffset: Int,
        valueSize: Int,
        loadFn: (
            memory: ByteArray,
            memoryOffset: Int
        ) -> TWrapperValue
    ) {
        val memoryAddress = stack
            .currentFrame
            .module
            .memoryAddresses[0]

        val memory = store.memories[memoryAddress]
        val index = (stack.pop() as StackValue).value as Int32Value
        val memoryOffset = index.value + instructionOffset

        if (memoryOffset + valueSize > memory.data.size) {
            throw IllegalStateException("Load exceeds memory limit")
        }

        stack.push(StackValue(loadFn(memory.data, memoryOffset)))
    }

    @InstructionExecutor(UniqueIds.INT32_LOAD)
    @JvmStatic
    fun execLoadInt32(
        store: Store,
        stack: Stack,
        instruction: Int32LoadInstruction
    ) = execLoad(
        store,
        stack,
        instruction.offset.toInt(),
        4
    ) { memory, offset ->
        val value = memory[offset].toUByte().toInt() or
                (memory[offset + 1].toUByte().toInt() shl 8) or
                (memory[offset + 2].toUByte().toInt() shl 16) or
                (memory[offset + 3].toUByte().toInt() shl 24)

        Int32Value(value)
    }

    @InstructionExecutor(UniqueIds.INT32_LOAD_8U)
    @JvmStatic
    fun execLoadInt32_8u(
        store: Store,
        stack: Stack,
        instruction: Int32Load8UInstruction
    ) = execLoad(
        store,
        stack,
        instruction.offset.toInt(),
        1
    ) { memory, offset ->
        Int32Value(memory[offset].toUByte().toInt())
    }
}
