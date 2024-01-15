package dev.fir3.iwan.engine.vm

import dev.fir3.iwan.engine.models.EmptyModuleInstance
import dev.fir3.iwan.engine.models.HostFunctionInstance
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
        return when (val instance = Store.functions[functionAddress]) {
            is HostFunctionInstance -> invokeHostFunction(instance)
            is WasmFunctionInstance -> invokeWasmFunction(instance)
        }
    }

    private fun invokeHostFunction(
        functionInstance: HostFunctionInstance
    ): List<Value> {
        val parameters = mutableListOf<Value>()

        functionInstance
            .type
            .parameterTypes
            .size
            .downTo(1)
            .forEach { _ ->
                parameters.add((Stack.pop() as StackValue).value)
            }

        return functionInstance.invoke(parameters)
    }

    private fun invokeWasmFunction(
        functionInstance: WasmFunctionInstance
    ): List<Value> {
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

        val frame = StackFrame(
            locals.toTypedArray(),
            functionInstance.module,
            functionType.resultTypes.size
        )

        Stack.push(frame)
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

        if (frame === Stack.currentFrame) {
            Stack.pop()
        }

        return results
    }
}
