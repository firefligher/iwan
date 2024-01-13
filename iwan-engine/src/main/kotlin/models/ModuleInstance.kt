package dev.fir3.iwan.engine.models

import dev.fir3.iwan.io.wasm.models.FunctionType

data class ModuleInstance(
    val data: List<Int>,
    val elementAddresses: List<Int>,
    val exports: List<ExportInstance>,
    val functionAddresses: List<Int>,
    val globalAddresses: List<Int>,
    val memoryAddresses: List<Int>,
    val tableAddresses: List<Int>,
    val types: List<FunctionType>
)
