package dev.fir3.iwan.engine.vm

import dev.fir3.iwan.engine.models.HostFunctionInstance
import dev.fir3.iwan.engine.models.WasmFunctionInstance
import dev.fir3.iwan.engine.vm.interpreter.Interpreter
import dev.fir3.iwan.engine.vm.stack.DefaultStack
import dev.fir3.iwan.io.wasm.models.valueTypes.NumberType

object Invocation {
    fun invoke(functionAddress: Int, values: List<Any>): List<Any> {
        println("Entering function $functionAddress")

        val stack = DefaultStack()
        val function = Store.functions[functionAddress]

        // Prepare the stack with the parameters.

        function.type.parameterTypes.forEachIndexed { index, type ->
            val raw = values[index]

            when (type) {
                NumberType.Float32 -> stack.pushFloat32(raw as Float)
                NumberType.Float64 -> stack.pushFloat64(raw as Double)
                NumberType.Int32 -> stack.pushInt32(raw as Int)
                NumberType.Int64 -> stack.pushInt64(raw as Long)
                else -> error("Unsupported parameter type for invocation.")
            }
        }

        // Invocation.

        when (function) {
            is HostFunctionInstance -> error("Not supported.")
            is WasmFunctionInstance -> {
                stack.pushFrame(function)
                Interpreter(stack).execute()
            }
        }

        // Retrieve the result values.

        return function.type.resultTypes.reversed().map { type ->
            when (type) {
                NumberType.Float32 -> stack.popFloat32()
                NumberType.Float64 -> stack.popFloat64()
                NumberType.Int32 -> stack.popInt32()
                NumberType.Int64 -> stack.popInt64()
                else -> error("Unsupported result type for invocation.")
            }
        }
    }
}
