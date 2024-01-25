package dev.fir3.iwan.engine.hacky

import dev.fir3.iwan.engine.models.HostFunctionInstance
import dev.fir3.iwan.engine.vm.DefaultValues
import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.io.wasm.models.FunctionType
import dev.fir3.iwan.io.wasm.models.valueTypes.NumberType

class FakeHostFunctionInstance(
    override val type: FunctionType,
    val module: String,
    val name: String
) : HostFunctionInstance {
    override fun invoke(parameters: List<Any>): List<Any> {
        if (module == "a" && name == "a") {
            val baseAddr = (parameters[1] as Int)
            val memory = Store.memories[0].data

            val bufAddr = memory[baseAddr].toUByte().toUInt() or
                    (memory[baseAddr + 1].toUByte().toUInt() shl 8) or
                    (memory[baseAddr + 2].toUByte().toUInt() shl 16) or
                    (memory[baseAddr + 3].toUByte().toUInt() shl 24)

            val length = memory[baseAddr + 4].toUByte().toUInt() or
                    (memory[baseAddr + 5].toUByte().toUInt() shl 8) or
                    (memory[baseAddr + 6].toUByte().toUInt() shl 16) or
                    (memory[baseAddr + 7].toUByte().toUInt() shl 24)

            println("Program says: " + String(memory, bufAddr.toInt(), length.toInt()))

            return listOf(1)
        }

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
