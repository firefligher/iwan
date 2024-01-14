package dev.fir3.iwan.engine.vm

import dev.fir3.iwan.engine.models.EmptyModuleInstance
import dev.fir3.iwan.engine.models.Value
import dev.fir3.iwan.engine.models.WasmFunctionInstance
import dev.fir3.iwan.engine.models.stack.StackFrame
import dev.fir3.iwan.engine.models.stack.StackLabel
import dev.fir3.iwan.engine.models.stack.StackValue
import dev.fir3.iwan.engine.vm.interpreter.Interpreter

object Invocation {
    fun invoke(functionAddress: Int, values: List<Value>): List<Value> {
        Stack.push(StackFrame(emptyArray(), EmptyModuleInstance, 0))

        for (value in values) {
            Stack.push(StackValue(value))
        }

        val result = invokeFunction(functionAddress)
        Stack.pop()

        return result
    }

    internal fun invokeFunction(functionAddress: Int): List<Value> {
        val functionInstance = Store.functions[functionAddress]
        require(functionInstance is WasmFunctionInstance)

        val functionType = functionInstance.type
        val functionCode = functionInstance.code
        val locals = mutableListOf<Value>()

        for (index in 0 until functionType.parameterTypes.size) {
            locals.add(0, (Stack.pop() as StackValue).value)
        }

        for (index in 0 until functionCode.locals.size) {
            locals.add(
                DefaultValues.getDefaultValue(functionCode.locals[index])
            )
        }

        Stack.push(
            StackFrame(
                locals.toTypedArray(),
                functionInstance.module,
                functionType.resultTypes.size
            )
        )

        Stack.push(
            StackLabel(
                functionType.resultTypes.size,
                functionCode.body.body,
                0,
                false
            )
        )

        Interpreter.execute()

        val results = functionInstance.type.resultTypes.map {
            (Stack.pop() as StackValue).value
        }

        Stack.pop()
        return results
    }
}
