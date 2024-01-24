package dev.fir3.iwan.engine.hacky

import dev.fir3.iwan.engine.models.HostFunctionInstance
import dev.fir3.iwan.engine.vm.DefaultValues
import dev.fir3.iwan.io.wasm.models.FunctionType
import dev.fir3.iwan.io.wasm.models.valueTypes.NumberType

class FakeHostFunctionInstance(
    override val type: FunctionType,
    val module: String,
    val name: String
) : HostFunctionInstance {
    override fun invoke(parameters: List<Any>): List<Any> {
        println("Acting as ['$module':'$name'] - Received: $parameters")
        return type.resultTypes.map { type ->
            if (type == NumberType.Int32) {
                1
            } else {
                DefaultValues.getDefaultValue(type)
            }
        }
    }
}
