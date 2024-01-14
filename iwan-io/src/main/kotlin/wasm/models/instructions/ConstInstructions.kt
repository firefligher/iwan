package dev.fir3.iwan.io.wasm.models.instructions

import dev.fir3.iwan.io.wasm.serialization.instructions.ConstInstructionStrategy
import dev.fir3.iwan.io.wasm.serialization.instructions.InstructionInfo

sealed interface ConstInstruction<TValue> : Instruction {
    val constant: TValue
}

@InstructionInfo(0x43u, ConstInstructionStrategy::class)
data class Float32ConstInstruction(
    override val constant: Float
) : ConstInstruction<Float> {
    override val uniqueId: Int = UniqueIds.FLOAT32_CONST
}

@InstructionInfo(0x44u, ConstInstructionStrategy::class)
data class Float64ConstInstruction(
    override val constant: Double
) : ConstInstruction<Double> {
    override val uniqueId: Int = UniqueIds.FLOAT64_CONST
}

@InstructionInfo(0x41u, ConstInstructionStrategy::class)
data class Int32ConstInstruction(
    override val constant: Int
) : ConstInstruction<Int> {
    override val uniqueId: Int = UniqueIds.INT32_CONST
}

@InstructionInfo(0x42u, ConstInstructionStrategy::class)
data class Int64ConstInstruction(
    override val constant: Long
) : ConstInstruction<Long> {
    override val uniqueId: Int = UniqueIds.INT64_CONST
}
