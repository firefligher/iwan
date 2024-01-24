package dev.fir3.iwan.engine.models.stack

fun Shell.toLocal() = when (this.type) {
    ShellType.Float32 -> Local(type = LocalType.Float32, float32 = float32)
    ShellType.Float64 -> Local(type = LocalType.Float64, float64 = float64)
    ShellType.Int32 -> Local(type = LocalType.Int32, int32 = int32)
    ShellType.Int64 -> Local(type = LocalType.Int64, int64 = int64)
    ShellType.Reference ->
        Local(type = LocalType.Reference, reference =  reference)

    ShellType.Vector128 -> Local(
        type = LocalType.Vector128,
        vector128Msb = vector128Msb,
        vector128Lsb = vector128Lsb
    )

    else -> error("Attempted to convert non-value shell to local")
}
