package dev.fir3.iwan.engine.vm.instructionsTests.flat

import dev.fir3.iwan.engine.vm.instructions.FlatExecutor
import dev.fir3.iwan.engine.vm.stack.DefaultStack
import dev.fir3.iwan.engine.vm.stack.Stack
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Int32BinaryTestTest {
    companion object {
        private val TESTED_METHODS =
            listOf<Pair<(Stack) -> Unit, (Int, Int) -> Boolean>>(
                Pair(FlatExecutor::execInt32Eq) { a, b -> a == b },
                Pair(FlatExecutor::execInt32GeS) { a, b -> a >= b },
                Pair(FlatExecutor::execInt32GeU) { a, b ->
                    a.toUInt() >= b.toUInt()
                },

                Pair(FlatExecutor::execInt32GtS) { a, b -> a > b },
                Pair(FlatExecutor::execInt32GtU) { a, b ->
                    a.toUInt() > b.toUInt()
                },

                Pair(FlatExecutor::execInt32LeS) { a, b -> a <= b },
                Pair(FlatExecutor::execInt32LeU) { a, b ->
                    a.toUInt() <= b.toUInt()
                },

                Pair(FlatExecutor::execInt32LtS) { a, b -> a < b },
                Pair(FlatExecutor::execInt32LtU) { a, b ->
                    a.toUInt() < b.toUInt()
                },

                Pair(FlatExecutor::execInt32Ne) { a, b -> a != b }
            )

        private inline fun testAll(supplier: () -> Pair<Int, Int>) =
            TESTED_METHODS.forEach { (operator, simulator) ->
                // Arrange

                val (a, b) = supplier()
                val stack = DefaultStack()
                stack.pushInt32(a)
                stack.pushInt32(b)

                // Act

                operator(stack)

                // Assert

                val expectation = if (simulator(a, b)) 1 else 0
                val result = stack.popInt32()

                assertTrue(result == 0 || result == 1)
                assertEquals(expectation, result)
            }
    }

    @Test
    fun testZeroZero() = testAll { Pair(0, 0) }

    @Test
    fun testZeroOne() = testAll { Pair(0, 1) }

    @Test
    fun testOneZero() = testAll { Pair(1, 0) }

    @Test
    fun testMinusOneZero() = testAll { Pair(-1, 0) }

    @Test
    fun testZeroMinusOne() = testAll { Pair(0, -1) }
}
