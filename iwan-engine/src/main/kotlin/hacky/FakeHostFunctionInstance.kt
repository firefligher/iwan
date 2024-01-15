package dev.fir3.iwan.engine.hacky

import dev.fir3.iwan.engine.models.HostFunctionInstance
import dev.fir3.iwan.engine.models.Int32Value
import dev.fir3.iwan.engine.models.Value
import dev.fir3.iwan.engine.vm.DefaultValues
import dev.fir3.iwan.io.wasm.models.FunctionType
import dev.fir3.iwan.io.wasm.models.valueTypes.NumberType
import dev.fir3.iwan.io.wasm.models.valueTypes.ValueType

class FakeHostFunctionInstance(
    override val type: FunctionType,
    val module: String,
    val name: String
) : HostFunctionInstance {
    override fun invoke(parameters: List<Value>): List<Value> {
        println("Acting as ['$module':'$name'] - Received: $parameters")
        return type.resultTypes.map { type ->
            if (type == NumberType.Int32) {
                Int32Value(1)
            } else {
                DefaultValues.getDefaultValue(type)
            }
        }
    }
}
