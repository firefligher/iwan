package dev.fir3.iwan.engine.vm.instructionsTests.flat

import dev.fir3.iwan.engine.vm.instructions.FlatExecutor
import dev.fir3.iwan.engine.vm.stack.DefaultStack
import dev.fir3.iwan.engine.vm.stack.Stack
import kotlin.test.Test
import kotlin.test.assertEquals

class Int64BinaryOperatorTest {
    companion object {
        private val TESTED_METHODS =
            listOf<Pair<(Stack) -> Unit, (Long, Long) -> Long>>(
                Pair(FlatExecutor::execInt64Add) { a, b -> a + b },
                Pair(FlatExecutor::execInt64And) { a, b -> a and b },
                Pair(FlatExecutor::execInt64Mul) { a, b -> a * b },
                Pair(FlatExecutor::execInt64Or) { a, b -> a or b },
                Pair(FlatExecutor::execInt64Sub) { a, b -> a - b },
                Pair(FlatExecutor::execInt64Xor) { a, b -> a xor b },
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

                val expectation = simulator(a, b)
                val result = stack.popInt64()

                assertEquals(expectation, result)
            }
    }

    @Test
    fun testZeroOne() = testAll { Pair(0, 1) }

    @Test
    fun testOneMinusOne() = testAll { Pair(1, -1) }

    @Test
    fun testMinusOneOne() = testAll { Pair(-1, 1) }

    @Test
    fun testZeroMinusOne() = testAll { Pair(0, -1) }
}