package dev.fir3.iwan.engine.models.stack

import dev.fir3.iwan.engine.models.FunctionReference
import dev.fir3.iwan.engine.models.ReferenceValue

data class Local(
    // Numbers

    var float32: Float = 0F,
    var float64: Double = 0.0,
    var int32: Int = 0,
    var int64: Long = 0,

    // Reference type

    var reference: ReferenceValue = FunctionReference(0),

    // Vector128

    var vector128Msb: Long = 0,
    var vector128Lsb: Long = 0,

    val type: LocalType
)
