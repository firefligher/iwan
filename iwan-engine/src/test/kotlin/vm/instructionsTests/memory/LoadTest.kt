package dev.fir3.iwan.engine.vm.instructionsTests.memory

import dev.fir3.iwan.engine.models.MemoryInstance
import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.engine.vm.instructions.LoadExecutor
import dev.fir3.iwan.engine.vm.stack.DefaultStack
import dev.fir3.iwan.engine.vm.stack.Stack
import dev.fir3.iwan.io.wasm.models.MemoryType
import dev.fir3.iwan.io.wasm.models.instructions.*
import kotlin.test.Test
import kotlin.test.assertEquals

class LoadTest {
    companion object {
        private fun <TInstruction : AlignedMemoryInstruction> test(
            executor: (Store, Stack, TInstruction) -> Unit,
            instruction: TInstruction,
            bytes: ByteArray,
            validator: (Stack) -> Unit
        ) {
            // Arrange

            val module = DummyModuleInstance(memoryAddresses = listOf(0))
            val stack = DefaultStack()
            val offset = 3 + instruction.offset.toInt()
            val memory = ByteArray(offset + bytes.size)

            bytes.copyInto(destination = memory, destinationOffset = offset)
            Store.memories.add(0, MemoryInstance(MemoryType(0u, 0u), memory))

            stack.pushInitializerFrame(module, resultType = null, emptyList())
            stack.pushInt32(3)

            // Act

            executor(Store, stack, instruction)

            // Assert

            validator(stack)
            assertEquals(1, stack.computeElementCount())
        }
    }

    // execLoadInt32

    @Test
    fun execLoadInt32() = test(
        LoadExecutor::execLoadInt32,
        Int32LoadInstruction(align = 0u, offset = 1u),
        byteArrayOf(0x12, 0x34, 0x56, 0x78)
    ) { stack ->
        assertEquals(0x78563412, stack.popInt32())
    }

    // execLoadInt32_8s

    @Test
    fun execLoadInt32_8s_0() = test(
        LoadExecutor::execLoadInt32_8s,
        Int32Load8SInstruction(align = 0u, offset = 1u),
        byteArrayOf(123)
    ) { stack ->
        assertEquals(123, stack.popInt32())
    }

    @Test
    fun execLoadInt32_8s_1() = test(
        LoadExecutor::execLoadInt32_8s,
        Int32Load8SInstruction(align = 0u, offset = 1u),
        byteArrayOf(-1)
    ) { stack ->
        assertEquals(-1, stack.popInt32())
    }

    @Test
    fun execLoadInt32_8s_2() = test(
        LoadExecutor::execLoadInt32_8s,
        Int32Load8SInstruction(align = 0u, offset = 1u),
        byteArrayOf(-35)
    ) { stack ->
        assertEquals(-35, stack.popInt32())
    }

    // execLoadInt32_8u

    @Test
    fun execLoadInt32_8u_0() = test(
        LoadExecutor::execLoadInt32_8u,
        Int32Load8UInstruction(align = 0u, offset = 1u),
        byteArrayOf(123)
    ) { stack ->
        assertEquals(123, stack.popInt32())
    }

    @Test
    fun execLoadInt32_8u_1() = test(
        LoadExecutor::execLoadInt32_8u,
        Int32Load8UInstruction(align = 0u, offset = 1u),
        byteArrayOf(-1)
    ) { stack ->
        assertEquals(255, stack.popInt32())
    }

    // execLoadInt64

    @Test
    fun execLoadInt64() = test(
        LoadExecutor::execLoadInt64,
        Int64LoadInstruction(align = 0u, offset = 1u),
        byteArrayOf(0x12, 0x34, 0x56, 0x78, 0x18, 0x27, 0x36, 0x45)
    ) { stack ->
        assertEquals(0x4536271878563412, stack.popInt64())
    }

    // execLoadInt64_32u

    @Test
    fun execLoadInt64_32u_0() = test(
        LoadExecutor::execLoadInt64_32u,
        Int64Load32UInstruction(align = 0u, offset = 1u),
        byteArrayOf(0x12, 0x34, 0x56, 0x87.toByte())
    ) { stack ->
        assertEquals(0x87563412, stack.popInt64())
    }

    @Test
    fun execLoadInt64_32u_1() = test(
        LoadExecutor::execLoadInt64_32u,
        Int64Load32UInstruction(align = 0u, offset = 1u),
        byteArrayOf(0x87.toByte(), 0x65, 0x43, 0x21)
    ) { stack ->
        assertEquals(0x21436587, stack.popInt64())
    }
}
