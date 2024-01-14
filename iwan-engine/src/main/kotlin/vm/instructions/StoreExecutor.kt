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
    > execTemplate(
        store: Store,
        stack: Stack,
        instructionOffset: Int,
        valueSize: Int,
        writeFn: (
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
            throw IllegalStateException("Write exceeds memory limit")
        }

        writeFn(memory.data, memoryOffset, value.value)
    }

    @InstructionExecutor(UniqueIds.STORE_FLOAT32)
    @JvmStatic
    fun execFloat32(
        store: Store,
        stack: Stack,
        instruction: Float32StoreInstruction
    ) = execTemplate<Float, Float32Value>(
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
    fun execFloat64(
        store: Store,
        stack: Stack,
        instruction: Float64StoreInstruction
    ) = execTemplate<Double, Float64Value>(
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
    fun execInt32(
        store: Store,
        stack: Stack,
        instruction: Int32StoreInstruction
    ) = execTemplate<Int, Int32Value>(
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

    @InstructionExecutor(UniqueIds.STORE_INT32_8)
    @JvmStatic
    fun execInt32_8(
        store: Store,
        stack: Stack,
        instruction: Int32Store8Instruction
    ) = execTemplate<Int, Int32Value>(
        store,
        stack,
        instruction.offset.toInt(),
        1
    ) { memory, offset, value ->
        memory[offset] = value.toByte()
    }

    @InstructionExecutor(UniqueIds.STORE_INT32_16)
    @JvmStatic
    fun execInt32_16(
        store: Store,
        stack: Stack,
        instruction: Int32Store16Instruction
    ) = execTemplate<Int, Int32Value>(
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
    fun execInt64(
        store: Store,
        stack: Stack,
        instruction: Int64StoreInstruction
    ) = execTemplate<Long, Int64Value>(
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
    fun execInt64_8(
        store: Store,
        stack: Stack,
        instruction: Int64Store8Instruction
    ) = execTemplate<Long, Int64Value>(
        store,
        stack,
        instruction.offset.toInt(),
        1
    ) { memory, offset, value ->
        memory[offset] = value.toByte()
    }

    @InstructionExecutor(UniqueIds.STORE_INT64_16)
    @JvmStatic
    fun execInt64_16(
        store: Store,
        stack: Stack,
        instruction: Int64Store16Instruction
    ) = execTemplate<Long, Int64Value>(
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
    fun execInt64_32(
        store: Store,
        stack: Stack,
        instruction: Int64Store32Instruction
    ) = execTemplate<Long, Int64Value>(
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
