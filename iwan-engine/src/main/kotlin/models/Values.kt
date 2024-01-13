package dev.fir3.iwan.engine.models

sealed interface Value

sealed interface NumberValue : Value
data class Float32Value(val value: Float): NumberValue
data class Float64Value(val value: Double): NumberValue
data class Int32Value(val value: Int): NumberValue
data class Int64Value(val value: Long): NumberValue

sealed interface ReferenceValue : Value
data class ExternalReference(val externalAddress: Int): ReferenceValue
data class FunctionReference(val functionAddress: Int): ReferenceValue
object ReferenceNull : ReferenceValue

sealed interface VectorValue : Value
data class Vector128Value(val lsb: Long, val msb: Long): VectorValue
