package dev.fir3.iwan.engine.vm.instructionsTests.flat

import dev.fir3.iwan.engine.models.EmptyModuleInstance
import dev.fir3.iwan.engine.models.ReferenceNull
import dev.fir3.iwan.engine.vm.instructions.FlatExecutor
import dev.fir3.iwan.engine.vm.stack.DefaultStack
import dev.fir3.iwan.engine.vm.stack.Stack
import kotlin.test.Test
import kotlin.test.assertEquals

class DropTest {
    companion object {
        private inline fun testDrop(preparer: (Stack) -> Unit) {
            // Arrange

            val stack = DefaultStack()
            stack.pushInitializerFrame(
                EmptyModuleInstance,
                resultType = null,
                emptyList()
            )

            preparer(stack)

            // Act

            FlatExecutor.execDrop(stack)

            // Assert

            assertEquals(1, stack.computeElementCount())
        }
    }

    @Test
    fun dropFloat32() = testDrop { stack -> stack.pushFloat32(127.3F) }

    @Test
    fun dropFloat64() = testDrop { stack -> stack.pushFloat64(-17.5) }

    @Test
    fun dropInt32() = testDrop { stack -> stack.pushInt32(5) }

    @Test
    fun dropInt64() = testDrop { stack -> stack.pushInt64(-89L) }

    @Test
    fun dropReference() = testDrop { stack ->
        stack.pushReference(ReferenceNull.EXTERNAL)
    }

    @Test
    fun dropVector128() = testDrop { stack -> stack.pushVector128(79, -35) }
}