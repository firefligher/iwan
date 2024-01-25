package dev.fir3.iwan.engine.vm.instructionsTests.flat

import dev.fir3.iwan.engine.models.EmptyModuleInstance
import dev.fir3.iwan.engine.vm.instructions.FlatExecutor
import dev.fir3.iwan.engine.vm.stack.DefaultStack
import dev.fir3.iwan.engine.vm.stack.Stack
import kotlin.test.Test
import kotlin.test.assertEquals

class IntUnaryTest {
    companion object {
        private inline fun test(
            operator: (Stack) -> Unit,
            initializer: (Stack) -> Unit,
            verifier: (Stack) -> Unit
        ) {
            // Arrange

            val stack = DefaultStack()
            stack.pushInitializerFrame(
                EmptyModuleInstance,
                resultType = null,
                emptyList()
            )

            initializer(stack)

            // Act

            operator(stack)

            // Assert

            verifier(stack)
            assertEquals(1, stack.computeElementCount())
        }
    }

    // execInt32Eqz

    @Test
    fun execInt32Eqz_0() = test(
        FlatExecutor::execInt32Eqz,
        { stack -> stack.pushInt32(-1) },
        { stack -> assertEquals(0, stack.popInt32()) }
    )

    @Test
    fun execInt32Eqz_1() = test(
        FlatExecutor::execInt32Eqz,
        { stack -> stack.pushInt32(1) },
        { stack -> assertEquals(0, stack.popInt32()) }
    )

    @Test
    fun execInt32Eqz_2() = test(
        FlatExecutor::execInt32Eqz,
        { stack -> stack.pushInt32(0) },
        { stack -> assertEquals(1, stack.popInt32()) }
    )

    // execInt32WrapInt64

    @Test
    fun execInt32WrapInt64_0() = test(
        FlatExecutor::execInt32WrapInt64,
        { stack -> stack.pushInt64(0) },
        { stack -> assertEquals(0, stack.popInt32()) }
    )

    @Test
    fun execInt32WrapInt64_1() = test(
        FlatExecutor::execInt32WrapInt64,
        { stack -> stack.pushInt64(1) },
        { stack -> assertEquals(1, stack.popInt32()) }
    )

    @Test
    fun execInt32WrapInt64_2() = test(
        FlatExecutor::execInt32WrapInt64,
        { stack -> stack.pushInt64(-1) },
        { stack -> assertEquals(-1, stack.popInt32()) }
    )

    @Test
    fun execInt32WrapInt64_3() = test(
        FlatExecutor::execInt32WrapInt64,
        { stack -> stack.pushInt64((0xFFFFFFFF_00000000UL).toLong()) },
        { stack -> assertEquals(0, stack.popInt32()) }
    )

    // execInt64Eqz

    @Test
    fun execInt64Eqz_0() = test(
        FlatExecutor::execInt64Eqz,
        { stack -> stack.pushInt64(-1) },
        { stack -> assertEquals(0, stack.popInt32()) }
    )

    @Test
    fun execInt64Eqz_1() = test(
        FlatExecutor::execInt64Eqz,
        { stack -> stack.pushInt64(1) },
        { stack -> assertEquals(0, stack.popInt32()) }
    )

    @Test
    fun execInt64Eqz_2() = test(
        FlatExecutor::execInt64Eqz,
        { stack -> stack.pushInt64(0) },
        { stack -> assertEquals(1, stack.popInt32()) }
    )

    // execInt64ExtendInt32S

    @Test
    fun execInt64ExtendInt32S_0() = test(
        FlatExecutor::execInt64ExtendInt32S,
        { stack -> stack.pushInt32(0) },
        { stack -> assertEquals(0, stack.popInt64()) }
    )

    @Test
    fun execInt64ExtendInt32S_1() = test(
        FlatExecutor::execInt64ExtendInt32S,
        { stack -> stack.pushInt32(1) },
        { stack -> assertEquals(1, stack.popInt64()) }
    )

    @Test
    fun execInt64ExtendInt32S_2() = test(
        FlatExecutor::execInt64ExtendInt32S,
        { stack -> stack.pushInt32(-1) },
        { stack -> assertEquals(-1, stack.popInt64()) }
    )

    // execInt64ExtendInt32U

    @Test
    fun execInt64ExtendInt32U_0() = test(
        FlatExecutor::execInt64ExtendInt32U,
        { stack -> stack.pushInt32(0) },
        { stack -> assertEquals(0, stack.popInt64()) }
    )

    @Test
    fun execInt64ExtendInt32U_1() = test(
        FlatExecutor::execInt64ExtendInt32U,
        { stack -> stack.pushInt32(1) },
        { stack -> assertEquals(1, stack.popInt64()) }
    )

    @Test
    fun execInt64ExtendInt32U_2() = test(
        FlatExecutor::execInt64ExtendInt32U,
        { stack -> stack.pushInt32(-1) },
        { stack -> assertEquals(0xFFFFFFFF, stack.popInt64()) }
    )
}
