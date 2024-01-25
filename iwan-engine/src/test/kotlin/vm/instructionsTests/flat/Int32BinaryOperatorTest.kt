package dev.fir3.iwan.engine.vm.instructionsTests.flat

import dev.fir3.iwan.engine.vm.instructions.FlatExecutor
import dev.fir3.iwan.engine.vm.stack.DefaultStack
import dev.fir3.iwan.engine.vm.stack.Stack
import kotlin.test.Test
import kotlin.test.assertEquals

class Int32BinaryOperatorTest {
    companion object {
        private val TESTED_METHODS =
            listOf<Pair<(Stack) -> Unit, (Int, Int) -> Int>>(
                Pair(FlatExecutor::execInt32Add) { a, b -> a + b },
                Pair(FlatExecutor::execInt32And) { a, b -> a and b },
                Pair(FlatExecutor::execInt32DivU) { a, b ->
                    (a.toUInt() / b.toUInt()).toInt()
                },

                Pair(FlatExecutor::execInt32Mul) { a, b -> a * b },
                Pair(FlatExecutor::execInt32Or) { a, b -> a or b },
                Pair(FlatExecutor::execInt32Sub) { a, b -> a - b },
                Pair(FlatExecutor::execInt32Xor) { a, b -> a xor b }
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

                val expectation = simulator(a, b)
                val result = stack.popInt32()

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
