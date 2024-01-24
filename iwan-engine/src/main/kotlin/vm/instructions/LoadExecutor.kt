package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.engine.vm.stack.Stack
import dev.fir3.iwan.io.wasm.models.instructions.*

object LoadExecutor : InstructionExecutionContainer {
    private inline fun <TValue> execLoad(
        store: Store,
        stack: Stack,
        instructionOffset: Int,
        valueSize: Int,
        pusher: (TValue) -> Unit,
        loader: (
            memory: ByteArray,
            memoryOffset: Int
        ) -> TValue
    ) {
        val memoryAddress = stack
            .currentModule
            .memoryAddresses[0]

        val memory = store.memories[memoryAddress]
        val index = stack.popInt32()
        val memoryOffset = index + instructionOffset

        if (memoryOffset + valueSize > memory.data.size) {
            throw IllegalStateException("Load exceeds memory limit")
        }

        pusher(loader(memory.data, memoryOffset))
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
        4,
        stack::pushInt32
    ) { memory, offset ->
        memory[offset].toUByte().toInt() or
                (memory[offset + 1].toUByte().toInt() shl 8) or
                (memory[offset + 2].toUByte().toInt() shl 16) or
                (memory[offset + 3].toUByte().toInt() shl 24)
    }

    @InstructionExecutor(UniqueIds.INT32_LOAD_8S)
    @JvmStatic
    fun execLoadInt32_8s(
        store: Store,
        stack: Stack,
        instruction: Int32Load8SInstruction
    ) = execLoad(
        store,
        stack,
        instruction.offset.toInt(),
        1,
        stack::pushInt32
    ) { memory, offset -> memory[offset].toInt() }

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
        1,
        stack::pushInt32
    ) { memory, offset -> memory[offset].toUByte().toInt() }

    @InstructionExecutor(UniqueIds.INT64_LOAD)
    @JvmStatic
    fun execLoadInt64(
        store: Store,
        stack: Stack,
        instruction: Int64LoadInstruction
    ) = execLoad(
        store,
        stack,
        instruction.offset.toInt(),
        8,
        stack::pushInt64
    ) { memory, offset ->
        memory[offset].toUByte().toLong() or
                (memory[offset + 1].toUByte().toLong() shl 8) or
                (memory[offset + 2].toUByte().toLong() shl 16) or
                (memory[offset + 3].toUByte().toLong() shl 24) or
                (memory[offset + 4].toUByte().toLong() shl 32) or
                (memory[offset + 5].toUByte().toLong() shl 40) or
                (memory[offset + 6].toUByte().toLong() shl 48) or
                (memory[offset + 7].toUByte().toLong() shl 56)
    }

    @InstructionExecutor(UniqueIds.INT64_LOAD_32U)
    @JvmStatic
    fun execLoadInt64_32u(
        store: Store,
        stack: Stack,
        instruction: Int64Load32UInstruction
    ) = execLoad(
        store,
        stack,
        instruction.offset.toInt(),
        4,
        stack::pushInt64
    ) { memory, offset ->
        memory[offset].toUByte().toLong() or
                (memory[offset + 1].toUByte().toLong() shl 8) or
                (memory[offset + 2].toUByte().toLong() shl 16) or
                (memory[offset + 3].toUByte().toLong() shl 24)
    }
}
