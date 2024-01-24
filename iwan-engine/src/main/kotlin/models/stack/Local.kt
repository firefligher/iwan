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

    var type: LocalType = LocalType.Float32
) {
    override fun toString(): String {
        var value = "Local(type=$type"

        when (type) {
            LocalType.Float32 -> value += ", float32=$float32"
            LocalType.Float64 -> value += ", float64=$float64"
            LocalType.Int32 -> value += ", int32=$int32"
            LocalType.Int64 -> value += ", int64=$int64"
            LocalType.Reference -> value += ", reference=$reference"
            LocalType.Vector128 -> {
                value += ", vector128Msb=$vector128Msb"
                value += ", vector128Lsb=$vector128Lsb"
            }
        }

        return "$value)"
    }
}
