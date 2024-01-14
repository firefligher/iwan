package dev.fir3.iwan.engine.models

import dev.fir3.iwan.io.wasm.models.FunctionType

interface ModuleInstance {
    val data: List<Int>
    val elementAddresses: List<Int>
    val exports: List<ExportInstance>
    val functionAddresses: List<Int>
    val globalAddresses: List<Int>
    val memoryAddresses: List<Int>
    val tableAddresses: List<Int>
    val types: List<FunctionType>
}

data class AuxiliaryModuleInstance(
    override val functionAddresses: List<Int>,
    override val globalAddresses: List<Int>,
    override val types: List<FunctionType>
): ModuleInstance {
    override val data: List<Int>
        get() = throw UnsupportedOperationException()
    override val elementAddresses: List<Int>
        get() = throw UnsupportedOperationException()
    override val exports: List<ExportInstance>
        get() = throw UnsupportedOperationException()
    override val memoryAddresses: List<Int>
        get() = throw UnsupportedOperationException()
    override val tableAddresses: List<Int>
        get() = throw UnsupportedOperationException()
}

data class InstantiatedModuleInstance(
    override val data: List<Int>,
    override val elementAddresses: List<Int>,
    override val exports: List<ExportInstance>,
    override val functionAddresses: List<Int>,
    override val globalAddresses: List<Int>,
    override val memoryAddresses: List<Int>,
    override val tableAddresses: List<Int>,
    override val types: List<FunctionType>
) : ModuleInstance
