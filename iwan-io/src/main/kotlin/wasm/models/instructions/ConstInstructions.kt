package dev.fir3.iwan.io.wasm.models.instructions

sealed interface ConstInstruction<TValue> : Instruction {
    val constant: TValue
}

data class Float32ConstInstruction(
    override val constant: Float
) : ConstInstruction<Float>

data class Float64ConstInstruction(
    override val constant: Double
) : ConstInstruction<Double>

data class Int32ConstInstruction(
    override val constant: Int
) : ConstInstruction<Int>

data class Int64ConstInstruction(
    override val constant: Long
) : ConstInstruction<Long>
