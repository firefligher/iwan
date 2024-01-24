package dev.fir3.iwan.engine.models

import dev.fir3.iwan.io.wasm.models.valueTypes.ValueType

data class GlobalInstance(
    val isMutable: Boolean,
    val type: ValueType,

    // Numbers

    var float32: Float = 0F,
    var float64: Double = 0.0,
    var int32: Int = 0,
    var int64: Long = 0,

    // References

    var reference: ReferenceValue = FunctionReference(0),

    // Vectors

    var vector128Msb: Long = 0,
    var vector128Lsb: Long = 0
)
