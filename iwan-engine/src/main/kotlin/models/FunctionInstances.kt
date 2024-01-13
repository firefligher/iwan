package dev.fir3.iwan.engine.models

import dev.fir3.iwan.io.wasm.models.Function
import dev.fir3.iwan.io.wasm.models.FunctionType

interface FunctionInstance {
    val type: FunctionType
}

interface HostFunctionInstance: FunctionInstance
data class WasmFunctionInstance(
    override val type: FunctionType,
    val module: ModuleInstance,
    val code: Function
): FunctionInstance
