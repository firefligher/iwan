package dev.fir3.iwan.engine.models.stack

import dev.fir3.iwan.engine.models.EmptyModuleInstance
import dev.fir3.iwan.engine.models.FunctionReference
import dev.fir3.iwan.engine.models.ModuleInstance
import dev.fir3.iwan.engine.models.ReferenceValue
import dev.fir3.iwan.io.wasm.models.instructions.Instruction
import dev.fir3.iwan.io.wasm.models.valueTypes.ValueType

data class Shell(
    // Navigation

    var previous: Shell? = null,
    var next: Shell? = null,

    // Control Flow

    var isLoop: Boolean = false,
    var locals: LocalContainer,
    var module: ModuleInstance,
    var resultTypes: List<ValueType>,

    var previousBody: List<Instruction>,
    var previousBodyIndex: Int = -1,
    var previousFrame: Shell? = null,
    var previousLabel: Shell? = null,

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

    var type: ShellType = ShellType.Float32
) {
    override fun toString(): String {
        var value = "Shell(type=$type"

        when (type) {
            ShellType.Float32 -> value += ", float32=$float32"
            ShellType.Float64 -> value += ", float64=$float64"
            ShellType.Frame -> value += ", locals=" + locals
            ShellType.Int32 -> value += ", int32=$int32"
            ShellType.Int64 -> value += ", int64=$int64"
            ShellType.Label -> {}
            ShellType.Reference -> value += ", reference=$reference"
            ShellType.Vector128 -> {
                value += ", vector128Msb=$vector128Msb"
                value += ", vector128Lsb=$vector128Lsb"
            }
        }

        return "$value)"
    }
}
