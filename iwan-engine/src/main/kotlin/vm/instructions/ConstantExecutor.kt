package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.models.Float32Value
import dev.fir3.iwan.engine.models.Float64Value
import dev.fir3.iwan.engine.models.Int32Value
import dev.fir3.iwan.engine.models.Int64Value
import dev.fir3.iwan.engine.models.stack.StackValue
import dev.fir3.iwan.engine.vm.Stack
import dev.fir3.iwan.io.wasm.models.instructions.*

object ConstantExecutor : InstructionExecutionContainer {
    @InstructionExecutor(UniqueIds.FLOAT32_CONST)
    @JvmStatic
    fun execFloat32(stack: Stack, instruction: Float32ConstInstruction) =
        stack.push(StackValue(Float32Value(instruction.constant)))

    @InstructionExecutor(UniqueIds.FLOAT64_CONST)
    @JvmStatic
    fun execFloat64(stack: Stack, instruction: Float64ConstInstruction) =
        stack.push(StackValue(Float64Value(instruction.constant)))

    @InstructionExecutor(UniqueIds.INT32_CONST)
    @JvmStatic
    fun execInt32(stack: Stack, instruction: Int32ConstInstruction) =
        stack.push(StackValue(Int32Value(instruction.constant)))

    @InstructionExecutor(UniqueIds.INT64_CONST)
    @JvmStatic
    fun execInt64(stack: Stack, instruction: Int64ConstInstruction) =
        stack.push(StackValue(Int64Value(instruction.constant)))
}
