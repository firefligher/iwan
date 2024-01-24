package dev.fir3.iwan.engine.models.stack

fun ShellType.isValueType() = when (this) {
    ShellType.Float32,
    ShellType.Float64,
    ShellType.Int32,
    ShellType.Int64,
    ShellType.Reference,
    ShellType.Vector128 -> true

    ShellType.Frame,
    ShellType.Label -> false
}
