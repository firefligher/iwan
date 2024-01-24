package dev.fir3.iwan.engine.extensions

import dev.fir3.iwan.engine.models.ReferenceNull
import dev.fir3.iwan.engine.models.stack.Local
import dev.fir3.iwan.engine.models.stack.LocalType
import dev.fir3.iwan.engine.models.stack.ShellType
import dev.fir3.iwan.io.wasm.models.valueTypes.NumberType
import dev.fir3.iwan.io.wasm.models.valueTypes.ReferenceType
import dev.fir3.iwan.io.wasm.models.valueTypes.ValueType
import dev.fir3.iwan.io.wasm.models.valueTypes.VectorType

val ValueType.defaultLocal get () = when (this) {
    NumberType.Float32 -> Local(type = LocalType.Float32)
    NumberType.Float64 -> Local(type = LocalType.Float64)
    NumberType.Int32 -> Local(type = LocalType.Int32)
    NumberType.Int64 -> Local(type = LocalType.Int64)
    ReferenceType.ExternalReference -> Local(
        type = LocalType.Reference,
        reference = ReferenceNull(ReferenceType.ExternalReference)
    )

    ReferenceType.FunctionReference -> Local(
        type = LocalType.Reference,
        reference = ReferenceNull(ReferenceType.FunctionReference)
    )

    VectorType.Vector128 -> Local(type = LocalType.Vector128)
}

fun ValueType.toShellType() = when (this) {
    NumberType.Float32 -> ShellType.Float32
    NumberType.Float64 -> ShellType.Float64
    NumberType.Int32 -> ShellType.Int32
    NumberType.Int64 -> ShellType.Int64
    ReferenceType.ExternalReference,
    ReferenceType.FunctionReference -> ShellType.Reference
    VectorType.Vector128 -> ShellType.Vector128
}
