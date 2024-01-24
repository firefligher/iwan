package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.vm.stack.Stack
import dev.fir3.iwan.io.wasm.models.instructions.*

object ConstantExecutor : InstructionExecutionContainer {
    private inline fun <TValue> exec(
        instruction: ConstInstruction<TValue>,
        pusher: (TValue) -> Unit
    ) = pusher(instruction.constant)

    @InstructionExecutor(UniqueIds.FLOAT32_CONST)
    @JvmStatic
    fun execFloat32(stack: Stack, instruction: Float32ConstInstruction) =
        exec(instruction, stack::pushFloat32)

    @InstructionExecutor(UniqueIds.FLOAT64_CONST)
    @JvmStatic
    fun execFloat64(stack: Stack, instruction: Float64ConstInstruction) =
        exec(instruction, stack::pushFloat64)

    @InstructionExecutor(UniqueIds.INT32_CONST)
    @JvmStatic
    fun execInt32(stack: Stack, instruction: Int32ConstInstruction) =
        exec(instruction, stack::pushInt32)

    @InstructionExecutor(UniqueIds.INT64_CONST)
    @JvmStatic
    fun execInt64(stack: Stack, instruction: Int64ConstInstruction) =
        exec(instruction, stack::pushInt64)
}
