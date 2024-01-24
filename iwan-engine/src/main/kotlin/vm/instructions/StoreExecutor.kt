package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.engine.vm.stack.Stack
import dev.fir3.iwan.io.wasm.models.instructions.*

object StoreExecutor : InstructionExecutionContainer {
    private inline fun <TValue> execStore(
        store: Store,
        stack: Stack,
        instructionOffset: Int,
        valueSize: Int,
        popper: () -> TValue,
        storer: (
            memory: ByteArray,
            memoryOffset: Int,
            value: TValue
        ) -> Unit
    ) {
        val memoryAddress = stack
            .currentModule
            .memoryAddresses[0]

        val memory = store.memories[memoryAddress]
        val value = popper()
        val index = stack.popInt32()
        val memoryOffset = index + instructionOffset

        if (memoryOffset + valueSize > memory.data.size) {
            throw IllegalStateException("Store exceeds memory limit")
        }

        storer(memory.data, memoryOffset, value)
    }

    @InstructionExecutor(UniqueIds.STORE_FLOAT32)
    @JvmStatic
    fun execStoreFloat32(
        store: Store,
        stack: Stack,
        instruction: Float32StoreInstruction
    ) = execStore(
        store,
        stack,
        instruction.offset.toInt(),
        4,
        stack::popFloat32
    ) { memory, offset, value ->
        val bits = value.toBits()
        memory[offset] = bits.toByte()
        memory[offset + 1] = (bits shr 8).toByte()
        memory[offset + 2] = (bits shr 16).toByte()
        memory[offset + 3] = (bits shr 24).toByte()
    }

    @InstructionExecutor(UniqueIds.STORE_FLOAT64)
    @JvmStatic
    fun execStoreFloat64(
        store: Store,
        stack: Stack,
        instruction: Float64StoreInstruction
    ) = execStore(
        store,
        stack,
        instruction.offset.toInt(),
        8,
        stack::popFloat64
    ) { memory, offset, value ->
        val bits = value.toBits()
        memory[offset] = bits.toByte()
        memory[offset + 1] = (bits shr 8).toByte()
        memory[offset + 2] = (bits shr 16).toByte()
        memory[offset + 3] = (bits shr 24).toByte()
        memory[offset + 4] = (bits shr 32).toByte()
        memory[offset + 5] = (bits shr 40).toByte()
        memory[offset + 6] = (bits shr 48).toByte()
        memory[offset + 7] = (bits shr 56).toByte()
    }

    @InstructionExecutor(UniqueIds.STORE_INT32)
    @JvmStatic
    fun execStoreInt32(
        store: Store,
        stack: Stack,
        instruction: Int32StoreInstruction
    ) = execStore(
        store,
        stack,
        instruction.offset.toInt(),
        4,
        stack::popInt32
    ) { memory, offset, value ->
        if (offset == 65596) {
            println()
        }

        memory[offset] = value.toByte()
        memory[offset + 1] = (value shr 8).toByte()
        memory[offset + 2] = (value shr 16).toByte()
        memory[offset + 3] = (value shr 24).toByte()
    }

    @InstructionExecutor(UniqueIds.STORE_INT32_8)
    @JvmStatic
    fun execStoreInt32_8(
        store: Store,
        stack: Stack,
        instruction: Int32Store8Instruction
    ) = execStore(
        store,
        stack,
        instruction.offset.toInt(),
        1,
        stack::popInt32
    ) { memory, offset, value ->
        memory[offset] = value.toByte()
    }

    @InstructionExecutor(UniqueIds.STORE_INT32_16)
    @JvmStatic
    fun execStoreInt32_16(
        store: Store,
        stack: Stack,
        instruction: Int32Store16Instruction
    ) = execStore(
        store,
        stack,
        instruction.offset.toInt(),
        2,
        stack::popInt32
    ) { memory, offset, value ->
        memory[offset] = value.toByte()
        memory[offset + 1] = (value shr 8).toByte()
    }

    @InstructionExecutor(UniqueIds.STORE_INT64)
    @JvmStatic
    fun execStoreInt64(
        store: Store,
        stack: Stack,
        instruction: Int64StoreInstruction
    ) = execStore(
        store,
        stack,
        instruction.offset.toInt(),
        8,
        stack::popInt64
    ) { memory, offset, value ->
        memory[offset] = value.toByte()
        memory[offset + 1] = (value shr 8).toByte()
        memory[offset + 2] = (value shr 16).toByte()
        memory[offset + 3] = (value shr 24).toByte()
        memory[offset + 4] = (value shr 32).toByte()
        memory[offset + 5] = (value shr 40).toByte()
        memory[offset + 6] = (value shr 48).toByte()
        memory[offset + 7] = (value shr 56).toByte()
    }

    @InstructionExecutor(UniqueIds.STORE_INT64_8)
    @JvmStatic
    fun execStoreInt64_8(
        store: Store,
        stack: Stack,
        instruction: Int64Store8Instruction
    ) = execStore(
        store,
        stack,
        instruction.offset.toInt(),
        1,
        stack::popInt64
    ) { memory, offset, value ->
        memory[offset] = value.toByte()
    }

    @InstructionExecutor(UniqueIds.STORE_INT64_16)
    @JvmStatic
    fun execStoreInt64_16(
        store: Store,
        stack: Stack,
        instruction: Int64Store16Instruction
    ) = execStore(
        store,
        stack,
        instruction.offset.toInt(),
        2,
        stack::popInt64
    ) { memory, offset, value ->
        memory[offset] = value.toByte()
        memory[offset + 1] = (value shr 8).toByte()
    }

    @InstructionExecutor(UniqueIds.STORE_INT64_32)
    @JvmStatic
    fun execStoreInt64_32(
        store: Store,
        stack: Stack,
        instruction: Int64Store32Instruction
    ) = execStore(
        store,
        stack,
        instruction.offset.toInt(),
        4,
        stack::popInt64
    ) { memory, offset, value ->
        memory[offset] = value.toByte()
        memory[offset + 1] = (value shr 8).toByte()
        memory[offset + 2] = (value shr 16).toByte()
        memory[offset + 3] = (value shr 24).toByte()
    }
}
