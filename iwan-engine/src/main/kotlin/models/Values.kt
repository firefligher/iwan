package dev.fir3.iwan.engine.models

import dev.fir3.iwan.io.wasm.models.valueTypes.NumberType
import dev.fir3.iwan.io.wasm.models.valueTypes.ReferenceType
import dev.fir3.iwan.io.wasm.models.valueTypes.ValueType
import dev.fir3.iwan.io.wasm.models.valueTypes.VectorType

sealed interface Value {
    val type: ValueType
}

sealed interface NumberValue<TValue> : Value {
    val value: TValue
}
data class Float32Value(override val value: Float): NumberValue<Float> {
    override val type = NumberType.Float32
}

data class Float64Value(override val value: Double): NumberValue<Double> {
    override val type = NumberType.Float64
}

data class Int32Value(override val value: Int): NumberValue<Int> {
    override val type = NumberType.Int32
}

data class Int64Value(override val value: Long): NumberValue<Long> {
    override val type = NumberType.Int64
}

sealed interface ReferenceValue : Value
data class ExternalReference(val externalAddress: Int): ReferenceValue {
    override val type: ValueType = ReferenceType.ExternalReference
}
data class FunctionReference(val functionAddress: Int): ReferenceValue {
    override val type = ReferenceType.ExternalReference
}

class ReferenceNull(override val type: ValueType) : ReferenceValue {
}

sealed interface VectorValue : Value
data class Vector128Value(val lsb: Long, val msb: Long): VectorValue {
    override val type = VectorType.Vector128
}
