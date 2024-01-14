package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.models.*
import dev.fir3.iwan.engine.models.stack.StackValue
import dev.fir3.iwan.engine.vm.Stack
import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.io.wasm.models.instructions.*

object StoreExecutor : InstructionExecutionContainer {
    private inline fun <
            reified TValue,
            reified TWrapperValue : NumberValue<TValue>
    > execStore(
        store: Store,
        stack: Stack,
        instructionOffset: Int,
        valueSize: Int,
        storeFn: (
            memory: ByteArray,
            memoryOffset: Int,
            value: TValue
        ) -> Unit
    ) {
        val memoryAddress = stack
            .currentFrame
            .module
            .memoryAddresses[0]

        val memory = store.memories[memoryAddress]
        val value = (stack.pop() as StackValue).value as TWrapperValue
        val index = (stack.pop() as StackValue).value as Int32Value
        val memoryOffset = index.value + instructionOffset

        if (memoryOffset + valueSize > memory.data.size) {
            throw IllegalStateException("Store exceeds memory limit")
        }

        storeFn(memory.data, memoryOffset, value.value)
    }

    @InstructionExecutor(UniqueIds.STORE_FLOAT32)
    @JvmStatic
    fun execStoreFloat32(
        store: Store,
        stack: Stack,
        instruction: Float32StoreInstruction
    ) = execStore<Float, Float32Value>(
        store,
        stack,
        instruction.offset.toInt(),
        4
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
    ) = execStore<Double, Float64Value>(
        store,
        stack,
        instruction.offset.toInt(),
        8
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
    ) = execStore<Int, Int32Value>(
        store,
        stack,
        instruction.offset.toInt(),
        4
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
    ) = execStore<Int, Int32Value>(
        store,
        stack,
        instruction.offset.toInt(),
        1
    ) { memory, offset, value ->
        memory[offset] = value.toByte()
    }

    @InstructionExecutor(UniqueIds.STORE_INT32_16)
    @JvmStatic
    fun execStoreInt32_16(
        store: Store,
        stack: Stack,
        instruction: Int32Store16Instruction
    ) = execStore<Int, Int32Value>(
        store,
        stack,
        instruction.offset.toInt(),
        2
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
    ) = execStore<Long, Int64Value>(
        store,
        stack,
        instruction.offset.toInt(),
        8
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
    ) = execStore<Long, Int64Value>(
        store,
        stack,
        instruction.offset.toInt(),
        1
    ) { memory, offset, value ->
        memory[offset] = value.toByte()
    }

    @InstructionExecutor(UniqueIds.STORE_INT64_16)
    @JvmStatic
    fun execStoreInt64_16(
        store: Store,
        stack: Stack,
        instruction: Int64Store16Instruction
    ) = execStore<Long, Int64Value>(
        store,
        stack,
        instruction.offset.toInt(),
        2
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
    ) = execStore<Long, Int64Value>(
        store,
        stack,
        instruction.offset.toInt(),
        4
    ) { memory, offset, value ->
        memory[offset] = value.toByte()
        memory[offset + 1] = (value shr 8).toByte()
        memory[offset + 2] = (value shr 16).toByte()
        memory[offset + 3] = (value shr 24).toByte()
    }
}
