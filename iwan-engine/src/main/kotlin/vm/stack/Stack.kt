package dev.fir3.iwan.engine.vm.stack

import dev.fir3.iwan.engine.models.ModuleInstance
import dev.fir3.iwan.engine.models.ReferenceValue
import dev.fir3.iwan.engine.models.WasmFunctionInstance
import dev.fir3.iwan.io.wasm.models.instructions.Instruction
import dev.fir3.iwan.io.wasm.models.instructions.blockTypes.BlockType
import dev.fir3.iwan.io.wasm.models.valueTypes.ValueType

interface Stack {
    val currentModule: ModuleInstance

    fun dropFrame(isReturn: Boolean)
    fun dropLabels(count: Int, isBranch: Boolean)
    fun dropPreviousValue()
    fun dropValue()

    fun nextInstruction(): Instruction?

    fun popFloat32(): Float
    fun popFloat64(): Double
    fun popInt32(): Int
    fun popInt64(): Long
    fun popIntoLocal(targetIndex: Int)
    fun popReference(): ReferenceValue
    fun popVector128(): Pair<Long, Long>

    fun pushFloat32(value: Float)
    fun pushFloat64(value: Double)
    fun pushFrame(function: WasmFunctionInstance)
    fun pushFromLocal(targetIndex: Int)
    fun pushInitializerFrame(
        module: ModuleInstance,
        resultType: ValueType?,
        instructions: List<Instruction>
    )

    fun pushInt32(value: Int)
    fun pushInt64(value: Long)
    fun pushLabel(
        resultTypes: List<ValueType>,
        instructions: List<Instruction>,
        isLoop: Boolean
    )

    fun pushReference(value: ReferenceValue)
    fun pushVector128(msb: Long, lsb: Long)

    fun teeIntoLocal(targetIndex: Int)
}
