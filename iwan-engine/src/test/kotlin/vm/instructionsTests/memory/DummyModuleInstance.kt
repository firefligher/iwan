package dev.fir3.iwan.engine.vm.instructionsTests.memory

import dev.fir3.iwan.engine.models.ExportInstance
import dev.fir3.iwan.engine.models.ModuleInstance
import dev.fir3.iwan.io.wasm.models.FunctionType

data class DummyModuleInstance(
    override val memoryAddresses: List<Int>
) : ModuleInstance {
    override val data: List<Int> get() = error("Not implemented.")
    override val elementAddresses: List<Int> get() = error("Not implemented.")
    override val exports: List<ExportInstance>
        get() = error("Not implemented.")

    override val functionAddresses: List<Int> get() = error("Not implemented.")
    override val globalAddresses: List<Int> get() = error("Not implemented.")
    override val tableAddresses: List<Int> get() = error("Not implemented.")
    override val types: List<FunctionType> get() = error("Not implemented.")
}
