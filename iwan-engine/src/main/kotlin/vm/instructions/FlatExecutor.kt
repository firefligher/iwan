package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.vm.stack.Stack
import dev.fir3.iwan.io.wasm.models.instructions.UniqueIds

object FlatExecutor : InstructionExecutionContainer {
    private inline fun execInt32Binary(
        stack: Stack,
        opFn: (a: Int, b: Int) -> Int
    ) {
        val b = stack.popInt32()
        val a = stack.popInt32()
        stack.pushInt32(opFn(a, b))
    }

    private inline fun execInt32Unary(stack: Stack, opFn: (x: Int) -> Int) =
        stack.pushInt32(opFn(stack.popInt32()))

    private inline fun execInt64Binary(
        stack: Stack,
        opFn: (a: Long, b: Long) -> Long
    ) {
        val b = stack.popInt64()
        val a = stack.popInt64()
        stack.pushInt64(opFn(a, b))
    }

    private inline fun execBinaryInt64Test(
        stack: Stack,
        opFn: (a: Long, b: Long) -> Int
    ) {
        val b = stack.popInt64()
        val a = stack.popInt64()
        stack.pushInt32(opFn(a, b))
    }

    @InstructionExecutor(UniqueIds.DROP)
    @JvmStatic
    fun execDrop(stack: Stack) = stack.dropValue()

    @InstructionExecutor(UniqueIds.INT32_ADD)
    @JvmStatic
    fun execInt32Add(stack: Stack) = execInt32Binary(stack) { a, b -> a + b }

    @InstructionExecutor(UniqueIds.INT32_AND)
    @JvmStatic
    fun execInt32And(stack: Stack) = execInt32Binary(stack) { a, b -> a and b }

    @InstructionExecutor(UniqueIds.INT32_DIV_U)
    @JvmStatic
    fun execInt32DivU(stack: Stack) = execInt32Binary(stack) { a, b ->
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

    @InstructionExecutor(UniqueIds.INT32_LE_S)
    @JvmStatic
    fun execInt32LeS(stack: Stack) = execInt32Binary(stack) { a, b ->
        if (a <= b) 1 else 0
    }

    @InstructionExecutor(UniqueIds.INT32_LE_U)
    @JvmStatic
    fun execInt32LeU(stack: Stack) = execInt32Binary(stack) { a, b ->
        if (a.toUInt() <= b.toUInt()) 1 else 0
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

    @InstructionExecutor(UniqueIds.INT32_SHR_U)
    @JvmStatic
    fun execInt32ShrU(stack: Stack) = execInt32Binary(stack) { a, b ->
        (a.toUInt() shr (b and 0x1F)).toInt()
    }

    @InstructionExecutor(UniqueIds.INT32_SUB)
    @JvmStatic
    fun execInt32Sub(stack: Stack) = execInt32Binary(stack) { a, b -> a - b }

    @InstructionExecutor(UniqueIds.INT32_WRAP_INT64)
    @JvmStatic
    fun execInt32WrapInt64(stack: Stack) =
        stack.pushInt32(stack.popInt64().toInt())

    @InstructionExecutor(UniqueIds.INT32_XOR)
    @JvmStatic
    fun execInt32Xor(stack: Stack) = execInt32Binary(stack) { a, b -> a xor b }

    @InstructionExecutor(UniqueIds.INT64_ADD)
    @JvmStatic
    fun execInt64Add(stack: Stack) = execInt64Binary(stack) { a, b -> a + b }

    @InstructionExecutor(UniqueIds.INT64_AND)
    @JvmStatic
    fun execInt64And(stack: Stack) = execInt64Binary(stack) { a, b -> a and b }

    @InstructionExecutor(UniqueIds.INT64_EQ)
    @JvmStatic
    fun execInt64Eq(stack: Stack) = execBinaryInt64Test(stack) { a, b ->
        if (a == b) 1 else 0
    }

    @InstructionExecutor(UniqueIds.INT64_EQZ)
    @JvmStatic
    fun execInt64Eqz(stack: Stack) {
        val x = stack.popInt64()
        val result = if (x == 0L) 1 else 0
        stack.pushInt32(result)
    }

    @InstructionExecutor(UniqueIds.INT64_EXTEND_INT32_S)
    @JvmStatic
    fun execInt64ExtendInt32S(stack: Stack) =
        stack.pushInt64(stack.popInt32().toLong())

    @InstructionExecutor(UniqueIds.INT64_EXTEND_INT32_U)
    @JvmStatic
    fun execInt64ExtendInt32U(stack: Stack) =
        stack.pushInt64(stack.popInt32().toUInt().toLong())

    @InstructionExecutor(UniqueIds.INT64_GE_S)
    @JvmStatic
    fun execInt64GeS(stack: Stack) = execBinaryInt64Test(stack) { a, b ->
        if (a >= b) 1 else 0
    }

    @InstructionExecutor(UniqueIds.INT64_GT_U)
    @JvmStatic
    fun execInt64GtU(stack: Stack) = execBinaryInt64Test(stack) { a, b ->
        if (a.toULong() > b.toULong()) 1
        else 0
    }

    @InstructionExecutor(UniqueIds.INT64_LE_S)
    @JvmStatic
    fun execInt64LeS(stack: Stack) = execBinaryInt64Test(stack) { a, b ->
        if (a <= b) 1 else 0
    }

    @InstructionExecutor(UniqueIds.INT64_LT_U)
    @JvmStatic
    fun execInt64LtU(stack: Stack) = execBinaryInt64Test(stack) { a, b ->
        if (a.toULong() < b.toULong()) 1
        else 0
    }

    @InstructionExecutor(UniqueIds.INT64_MUL)
    @JvmStatic
    fun execInt64Mul(stack: Stack) = execInt64Binary(stack) { a, b -> a * b }

    @InstructionExecutor(UniqueIds.INT64_NE)
    @JvmStatic
    fun execInt64Ne(stack: Stack) = execBinaryInt64Test(stack) { a, b ->
        if (a != b) 1 else 0
    }

    @InstructionExecutor(UniqueIds.INT64_OR)
    @JvmStatic
    fun execInt64Or(stack: Stack) = execInt64Binary(stack) { a, b -> a or b }

    @InstructionExecutor(UniqueIds.INT64_SHL)
    @JvmStatic
    fun execInt64Shl(stack: Stack) = execInt64Binary(stack) { a, b ->
        a shl (b and 0x3F).toInt()
    }

    @InstructionExecutor(UniqueIds.INT64_SHR_U)
    @JvmStatic
    fun execInt64ShrU(stack: Stack) = execInt64Binary(stack) { a, b ->
        (a.toULong() shr (b and 0x3F).toInt()).toLong()
    }

    @InstructionExecutor(UniqueIds.INT64_SUB)
    @JvmStatic
    fun execInt64Sub(stack: Stack) = execInt64Binary(stack) { a, b -> a - b }

    @InstructionExecutor(UniqueIds.INT64_XOR)
    @JvmStatic
    fun execInt64Xor(stack: Stack) = execInt64Binary(stack) { a, b -> a xor b }

    @InstructionExecutor(UniqueIds.RETURN)
    @JvmStatic
    fun execReturn(stack: Stack) = stack.dropFrame(isReturn = true)

    @InstructionExecutor(UniqueIds.SELECT)
    @JvmStatic
    fun execSelect(stack: Stack) {
        val selector = stack.popInt32()

        if (selector == 0) stack.dropPreviousValue()
        else stack.dropValue()
    }
}