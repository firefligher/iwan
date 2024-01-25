package dev.fir3.iwan.engine.vm.instructionsTests.flat

import dev.fir3.iwan.engine.vm.instructions.FlatExecutor
import dev.fir3.iwan.engine.vm.stack.DefaultStack
import dev.fir3.iwan.engine.vm.stack.Stack
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Int64BinaryTestTest {
    companion object {
        private val TESTED_METHODS =
            listOf<Pair<(Stack) -> Unit, (Long, Long) -> Boolean>>(
                Pair(FlatExecutor::execInt64Eq) { a, b -> a == b },
                Pair(FlatExecutor::execInt64GeS) { a, b -> a >= b },
                Pair(FlatExecutor::execInt64GtU) { a, b ->
                    a.toULong() > b.toULong()
                },

                Pair(FlatExecutor::execInt64LeS) { a, b -> a <= b },
                Pair(FlatExecutor::execInt64LtU) { a, b ->
                    a.toULong() < b.toULong()
                },

                Pair(FlatExecutor::execInt64Ne) { a, b -> a != b }
            )

        private inline fun testAll(supplier: () -> Pair<Long, Long>) =
            TESTED_METHODS.forEach { (operator, simulator) ->
                // Arrange

                val (a, b) = supplier()
                val stack = DefaultStack()
                stack.pushInt64(a)
                stack.pushInt64(b)

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