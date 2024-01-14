package dev.fir3.iwan.engine.models

sealed interface Value

sealed interface NumberValue<TValue> : Value {
    val value: TValue
}
data class Float32Value(override val value: Float): NumberValue<Float>
data class Float64Value(override val value: Double): NumberValue<Double>
data class Int32Value(override val value: Int): NumberValue<Int>
data class Int64Value(override val value: Long): NumberValue<Long>

sealed interface ReferenceValue : Value
data class ExternalReference(val externalAddress: Int): ReferenceValue
data class FunctionReference(val functionAddress: Int): ReferenceValue
object ReferenceNull : ReferenceValue

sealed interface VectorValue : Value
data class Vector128Value(val lsb: Long, val msb: Long): VectorValue
