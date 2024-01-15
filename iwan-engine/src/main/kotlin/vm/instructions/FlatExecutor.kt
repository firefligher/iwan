package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.models.Int32Value
import dev.fir3.iwan.engine.models.Int64Value
import dev.fir3.iwan.engine.models.stack.StackElement
import dev.fir3.iwan.engine.models.stack.StackFrame
import dev.fir3.iwan.engine.models.stack.StackValue
import dev.fir3.iwan.engine.vm.Stack
import dev.fir3.iwan.io.wasm.models.instructions.UniqueIds

object FlatExecutor : InstructionExecutionContainer {
    private inline fun execInt32Binary(
        stack: Stack,
        opFn: (a: Int, b: Int) -> Int
    ) {
        val b = (stack.pop() as StackValue).value as Int32Value
        val a = (stack.pop() as StackValue).value as Int32Value
        val result = opFn(a.value, b.value)

        stack.push(StackValue(Int32Value(result)))
    }

    private inline fun execInt32Unary(
        stack: Stack,
        opFn: (x: Int) -> Int
    ) {
        val x = (stack.pop() as StackValue).value as Int32Value
        val result = opFn(x.value)

        stack.push(StackValue(Int32Value(result)))
    }

    private inline fun execInt64Binary(
        stack: Stack,
        opFn: (a: Long, b: Long) -> Long
    ) {
        val b = (stack.pop() as StackValue).value as Int64Value
        val a = (stack.pop() as StackValue).value as Int64Value
        val result = opFn(a.value, b.value)

        stack.push(StackValue(Int64Value(result)))
    }

    @InstructionExecutor(UniqueIds.DROP)
    @JvmStatic
    fun execDrop(stack: Stack) {
        stack.pop()
    }

    @InstructionExecutor(UniqueIds.INT32_ADD)
    @JvmStatic
    fun execInt32Add(stack: Stack) = execInt32Binary(stack) { a, b -> a + b }

    @InstructionExecutor(UniqueIds.INT32_AND)
    @JvmStatic
    fun execInt32And(stack: Stack) = execInt32Binary(stack) { a, b -> a and b }

    @InstructionExecutor(UniqueIds.INT32_DIV_U)
    @JvmStatic
    fun execInt32Div(stack: Stack) = execInt32Binary(stack) { a, b ->
        (a.toUInt() / b.toUInt()).toInt()
    }

    @InstructionExecutor(UniqueIds.INT32_EQ)
    @JvmStatic
    fun execInt32Eq(stack: Stack) = execInt32Binary(stack) { a, b ->
        if(a == b) 1
        else 0
    }

    @InstructionExecutor(UniqueIds.INT32_EQZ)
    @JvmStatic
    fun execInt32Eqz(stack: Stack) = execInt32Unary(stack) { x ->
        if (x == 0) 1
        else 0
    }

    @InstructionExecutor(UniqueIds.INT32_GE_S)
    @JvmStatic
    fun execInt32GeS(stack: Stack) = execInt32Binary(stack) { a, b ->
        if (a >= b) 1
        else 0
    }

    @InstructionExecutor(UniqueIds.INT32_GE_U)
    @JvmStatic
    fun execInt32GeU(stack: Stack) = execInt32Binary(stack) { a, b ->
        if (a.toUInt() >= b.toUInt()) 1
        else 0
    }

    @InstructionExecutor(UniqueIds.INT32_GT_S)
    @JvmStatic
    fun execInt32GtS(stack: Stack) = execInt32Binary(stack) { a, b ->
        if (a > b) 1
        else 0
    }

    @InstructionExecutor(UniqueIds.INT32_GT_U)
    @JvmStatic
    fun execInt32GtU(stack: Stack) = execInt32Binary(stack) { a, b ->
        if (a.toUInt() > b.toUInt()) 1
        else 0
    }

    @InstructionExecutor(UniqueIds.INT32_LT_S)
    @JvmStatic
    fun execInt32LtS(stack: Stack) = execInt32Binary(stack) { a, b ->
        if (a < b) 1
        else 0
    }

    @InstructionExecutor(UniqueIds.INT32_LT_U)
    @JvmStatic
    fun execInt32LtU(stack: Stack) = execInt32Binary(stack) { a, b ->
        if (a.toUInt() < b.toUInt()) 1
        else 0
    }

    @InstructionExecutor(UniqueIds.INT32_MUL)
    @JvmStatic
    fun execInt32Mul(stack: Stack) = execInt32Binary(stack) { a, b -> a * b }

    @InstructionExecutor(UniqueIds.INT32_NE)
    @JvmStatic
    fun execInt32Ne(stack: Stack) = execInt32Binary(stack) { a, b ->
        if (a != b) 1
        else 0
    }

    @InstructionExecutor(UniqueIds.INT32_OR)
    @JvmStatic
    fun execInt32Or(stack: Stack) = execInt32Binary(stack) { a, b -> a or b }

    @InstructionExecutor(UniqueIds.INT32_SHL)
    @JvmStatic
    fun execInt32Shl(stack: Stack) = execInt32Binary(stack) { a, b ->
        a shl (b and 0x1F)
    }

    @InstructionExecutor(UniqueIds.INT32_SHR_S)
    @JvmStatic
    fun execInt32ShrS(stack: Stack) = execInt32Binary(stack) { a, b ->
        a shr (b and 0x1F)
    }

    @InstructionExecutor(UniqueIds.INT32_SUB)
    @JvmStatic
    fun execInt32Sub(stack: Stack) = execInt32Binary(stack) { a, b -> a - b }

    @InstructionExecutor(UniqueIds.INT32_XOR)
    @JvmStatic
    fun execInt32Xor(stack: Stack) = execInt32Binary(stack) { a, b -> a xor b }

    @InstructionExecutor(UniqueIds.INT64_EXTEND_INT32_U)
    @JvmStatic
    fun execInt64ExtendInt32U(stack: Stack) {
        val value = ((Stack.pop() as StackValue).value as Int32Value).value
        val extendedValue = value.toLong()

        Stack.push(StackValue(Int64Value(extendedValue)))
    }

    @InstructionExecutor(UniqueIds.INT64_MUL)
    @JvmStatic
    fun execInt64Mul(stack: Stack) = execInt64Binary(stack) { a, b -> a * b }

    @InstructionExecutor(UniqueIds.RETURN)
    @JvmStatic
    fun execReturn(stack: Stack) {
        val originalCurrentFrame = stack.currentFrame
        val returnValues = originalCurrentFrame
            .arity
            .downTo(1)
            .map { stack.pop() as StackValue }

        var poppedValue: StackElement

        do {
            poppedValue = stack.pop()
        } while (poppedValue !is StackFrame)

        returnValues.reversed().forEach(Stack::push)
    }

    @InstructionExecutor(UniqueIds.SELECT)
    @JvmStatic
    fun execSelect(stack: Stack) {
        val selector = ((Stack.pop() as StackValue).value as Int32Value).value
        val value1 = Stack.pop()
        val value2 = Stack.pop()

        if (selector != 0) Stack.push(value1)
        else Stack.push(value2)
    }
}