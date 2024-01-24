package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.engine.vm.stack.Stack
import dev.fir3.iwan.io.wasm.models.instructions.*
import dev.fir3.iwan.io.wasm.models.valueTypes.NumberType
import dev.fir3.iwan.io.wasm.models.valueTypes.ReferenceType
import dev.fir3.iwan.io.wasm.models.valueTypes.VectorType

object VariableExecutor : InstructionExecutionContainer {
    @InstructionExecutor(UniqueIds.GLOBAL_GET)
    @JvmStatic
    fun execGlobalGet(
        store: Store,
        stack: Stack,
        instruction: GlobalGetInstruction
    ) {
        val globalAddress = stack
            .currentModule
            .globalAddresses[instruction.globalIndex.toInt()]

        val global = store.globals[globalAddress]

        when (global.type) {
            NumberType.Float32 -> stack.pushFloat32(global.float32)
            NumberType.Float64 -> stack.pushFloat64(global.float64)
            NumberType.Int32 -> stack.pushInt32(global.int32)
            NumberType.Int64 -> stack.pushInt64(global.int64)
            ReferenceType.ExternalReference,
            ReferenceType.FunctionReference ->
                stack.pushReference(global.reference)

            VectorType.Vector128 ->
                stack.pushVector128(global.vector128Msb, global.vector128Lsb)
        }
    }

    @InstructionExecutor(UniqueIds.GLOBAL_SET)
    @JvmStatic
    fun execGlobalSet(
        store: Store,
        stack: Stack,
        instruction: GlobalSetInstruction
    ) {
        val globalAddress = stack
            .currentModule
            .globalAddresses[instruction.globalIndex.toInt()]

        val global = store.globals[globalAddress]
        check(global.isMutable) { "Attempted to modify immutable global" }

        when (global.type) {
            NumberType.Float32 -> global.float32 = stack.popFloat32()
            NumberType.Float64 -> global.float64 = stack.popFloat64()
            NumberType.Int32 -> global.int32 = stack.popInt32()
            NumberType.Int64 -> global.int64 = stack.popInt64()
            ReferenceType.ExternalReference,
            ReferenceType.FunctionReference ->
                global.reference = stack.popReference()

            VectorType.Vector128 -> {
                val vector128 = stack.popVector128()
                global.vector128Msb = vector128.first
                global.vector128Lsb = vector128.second
            }
        }
    }

    @InstructionExecutor(UniqueIds.LOCAL_GET)
    @JvmStatic
    fun execLocalGet(stack: Stack, instruction: LocalGetInstruction) =
        stack.pushFromLocal(instruction.localIndex.toInt())

    @InstructionExecutor(UniqueIds.LOCAL_SET)
    @JvmStatic
    fun execLocalSet(stack: Stack, instruction: LocalSetInstruction) =
        stack.popIntoLocal(instruction.localIndex.toInt())

    @InstructionExecutor(UniqueIds.LOCAL_TEE)
    @JvmStatic
    fun execLocalTee(stack: Stack, instruction: LocalTeeInstruction) =
        stack.teeIntoLocal(instruction.localIndex.toInt())
}
