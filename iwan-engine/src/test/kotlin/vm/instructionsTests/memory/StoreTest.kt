package dev.fir3.iwan.engine.vm.instructionsTests.memory

import dev.fir3.iwan.engine.models.MemoryInstance
import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.engine.vm.instructions.StoreExecutor
import dev.fir3.iwan.engine.vm.stack.DefaultStack
import dev.fir3.iwan.engine.vm.stack.Stack
import dev.fir3.iwan.io.wasm.models.MemoryType
import dev.fir3.iwan.io.wasm.models.instructions.*
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class StoreTest {
    companion object {
        private fun <TInstruction : AlignedMemoryInstruction> test(
            executor: (Store, Stack, TInstruction) -> Unit,
            instruction: TInstruction,
            expectedBytes: ByteArray,
            initializer: (Stack) -> Unit
        ) {
            // Arrange

            val module = DummyModuleInstance(memoryAddresses = listOf(0))
            val stack = DefaultStack()
            val offset = 3 + instruction.offset.toInt()
            val memory = ByteArray(offset + expectedBytes.size)

            Store.memories.add(0, MemoryInstance(MemoryType(0u, 0u), memory))

            stack.pushInitializerFrame(module, resultType = null, emptyList())
            stack.pushInt32(3)
            initializer(stack)

            // Act

            executor(Store, stack, instruction)

            // Assert

            assertEquals(1, stack.computeElementCount())
            assertContentEquals(ByteArray(offset) + expectedBytes, memory)
        }
    }

    @Test
    fun execStoreInt32() = test(
        StoreExecutor::execStoreInt32,
        Int32StoreInstruction(align = 0u, offset = 1u),
        expectedBytes = byteArrayOf(0x12, 0x34, 0x56, 0x78)
    ) { stack -> stack.pushInt32(0x78563412) }

    @Test
    fun execStoreInt32_8() = test(
        StoreExecutor::execStoreInt32_8,
        Int32Store8Instruction(align = 0u, offset = 1u),
        expectedBytes = byteArrayOf(0x78)
    ) { stack -> stack.pushInt32(0x12345678) }

    @Test
    fun execStoreInt32_16() = test(
        StoreExecutor::execStoreInt32_16,
        Int32Store16Instruction(align = 0u, offset = 1u),
        expectedBytes = byteArrayOf(0x78, 0x56)
    ) { stack -> stack.pushInt32(0x12345678) }

    @Test
    fun execStoreInt64() = test(
        StoreExecutor::execStoreInt64,
        Int64StoreInstruction(align = 0u, offset = 1u),
        expectedBytes = byteArrayOf(
            0x12, 0x34, 0x56, 0x78, 0x18, 0x27, 0x36, 0x45
        )
    ) { stack -> stack.pushInt64(0x4536271878563412) }

    @Test
    fun execStoreInt64_8() = test(
        StoreExecutor::execStoreInt64_8,
        Int64Store8Instruction(align = 0u, offset = 1u),
        expectedBytes = byteArrayOf(0x12)
    ) { stack -> stack.pushInt64(0x4536271878563412) }

    @Test
    fun execStoreInt64_16() = test(
        StoreExecutor::execStoreInt64_16,
        Int64Store16Instruction(align = 0u, offset = 1u),
        expectedBytes = byteArrayOf(0x12, 0x34)
    ) { stack -> stack.pushInt64(0x4536271878563412) }

    @Test
    fun execStoreInt64_32() = test(
        StoreExecutor::execStoreInt64_32,
        Int64Store32Instruction(align = 0u, offset = 1u),
        expectedBytes = byteArrayOf(0x12, 0x34, 0x56, 0x78)
    ) { stack -> stack.pushInt64(0x4536271878563412) }
}