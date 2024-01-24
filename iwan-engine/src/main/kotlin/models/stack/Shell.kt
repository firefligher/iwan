package dev.fir3.iwan.engine.models.stack

import dev.fir3.iwan.engine.models.EmptyModuleInstance
import dev.fir3.iwan.engine.models.FunctionReference
import dev.fir3.iwan.engine.models.ModuleInstance
import dev.fir3.iwan.engine.models.ReferenceValue
import dev.fir3.iwan.io.wasm.models.instructions.Instruction
import dev.fir3.iwan.io.wasm.models.valueTypes.ValueType

data class Shell(
    // Navigation

    var previous: Shell?,
    var next: Shell? = null,

    // Control Flow

    var isLoop: Boolean = false,
    var locals: Array<Local> = emptyArray(),
    var module: ModuleInstance = EmptyModuleInstance,
    var resultTypes: List<ValueType> = emptyList(),

    var previousBody: List<Instruction> = emptyList(),
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

    var type: ShellType
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Shell

        if (previous != other.previous) return false
        if (isLoop != other.isLoop) return false
        if (!locals.contentEquals(other.locals)) return false
        if (module != other.module) return false
        if (resultTypes != other.resultTypes) return false
        if (previousBody != other.previousBody) return false
        if (previousBodyIndex != other.previousBodyIndex) return false
        if (previousFrame != other.previousFrame) return false
        if (previousLabel != other.previousLabel) return false
        if (float32 != other.float32) return false
        if (float64 != other.float64) return false
        if (int32 != other.int32) return false
        if (int64 != other.int64) return false
        if (reference != other.reference) return false
        if (vector128Msb != other.vector128Msb) return false
        if (vector128Lsb != other.vector128Lsb) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = previous?.hashCode() ?: 0
        result = 31 * result + isLoop.hashCode()
        result = 31 * result + locals.contentHashCode()
        result = 31 * result + module.hashCode()
        result = 31 * result + resultTypes.hashCode()
        result = 31 * result + previousBody.hashCode()
        result = 31 * result + previousBodyIndex
        result = 31 * result + (previousFrame?.hashCode() ?: 0)
        result = 31 * result + (previousLabel?.hashCode() ?: 0)
        result = 31 * result + float32.hashCode()
        result = 31 * result + float64.hashCode()
        result = 31 * result + int32
        result = 31 * result + int64.hashCode()
        result = 31 * result + reference.hashCode()
        result = 31 * result + vector128Msb.hashCode()
        result = 31 * result + vector128Lsb.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

    override fun toString(): String {
        var value = "Shell(type=$type"

        when (type) {
            ShellType.Float32 -> value += ", float32=$float32"
            ShellType.Float64 -> value += ", float64=$float64"
            ShellType.Frame -> value += ", locals=" + locals.contentToString()
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
