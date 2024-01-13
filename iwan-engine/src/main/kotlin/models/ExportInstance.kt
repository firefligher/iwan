package dev.fir3.iwan.engine.models

interface ExportInstance {
    val name: String
}

data class FunctionExportInstance(
    override val name: String,
    val functionAddress: Int
): ExportInstance

data class GlobalExportInstance(
    override val name: String,
    val globalAddress: Int
): ExportInstance

data class MemoryExportInstance(
    override val name: String,
    val memoryAddress: Int
): ExportInstance

data class TableExportInstance(
    override val name: String,
    val tableAddress: Int
): ExportInstance
