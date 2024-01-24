package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.models.*
import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.engine.vm.stack.Stack
import dev.fir3.iwan.io.wasm.models.instructions.CallIndirectInstruction
import dev.fir3.iwan.io.wasm.models.instructions.CallInstruction
import dev.fir3.iwan.io.wasm.models.instructions.UniqueIds
import dev.fir3.iwan.io.wasm.models.valueTypes.NumberType
import dev.fir3.iwan.io.wasm.models.valueTypes.ReferenceType
import dev.fir3.iwan.io.wasm.models.valueTypes.VectorType

object CallExecutor : InstructionExecutionContainer {
    private fun invokeFunction(stack: Stack, function: FunctionInstance) =
        when (function) {
            is HostFunctionInstance -> {
                // TODO: Find better place for this code.

                val parameterTypes = function.type.parameterTypes
                val parameterCount = parameterTypes.size
                val parameters = mutableListOf<Any>()

                for (index in parameterCount - 1 downTo 0) {
                    parameters.add(
                        0,
                        when (parameterTypes[index]) {
                            NumberType.Float32 -> stack.popFloat32()
                            NumberType.Float64 -> stack.popFloat64()
                            NumberType.Int32 -> stack.popInt32()
                            NumberType.Int64 -> stack.popInt64()
                            ReferenceType.ExternalReference,
                            ReferenceType.FunctionReference ->
                                stack.popReference()

                            VectorType.Vector128 -> stack.popVector128()
                        }
                    )
                }

                function.invoke(parameters).forEachIndexed { index, value ->
                    when (function.type.resultTypes[index]) {
                        NumberType.Float32 -> stack.pushFloat32(value as Float)
                        NumberType.Float64 ->
                            stack.pushFloat64(value as Double)

                        NumberType.Int32 -> stack.pushInt32(value as Int)
                        NumberType.Int64 -> stack.pushInt64(value as Long)
                        ReferenceType.ExternalReference -> TODO()
                        ReferenceType.FunctionReference -> TODO()
                        VectorType.Vector128 -> TODO()
                    }
                }
            }
            is WasmFunctionInstance -> {
                stack.pushFrame(function)
            }
        }

    @InstructionExecutor(UniqueIds.CALL)
    @JvmStatic
    fun exec(stack: Stack, instruction: CallInstruction) {
        val functionAddress = stack
            .currentModule
            .functionAddresses[instruction.functionIndex.toInt()]

        invokeFunction(stack, Store.functions[functionAddress])
    }

    @InstructionExecutor(UniqueIds.CALL_INDIRECT)
    @JvmStatic
    fun execIndirect(
        store: Store,
        stack: Stack,
        instruction: CallIndirectInstruction
    ) {
        val tableAddress = stack
            .currentModule
            .tableAddresses[instruction.tableIndex.toInt()]

        val table = store.tables[tableAddress].elements
        val index = stack.popInt32()

        if (index >= table.size) {
            throw IllegalStateException("Invalid indirection")
        }

        val reference = table[index]

        if (reference is ReferenceNull) {
            throw IllegalStateException("Null pointer indirection")
        }

        val functionAddress = (reference as FunctionReference).functionAddress
        invokeFunction(stack, Store.functions[functionAddress])
    }
}