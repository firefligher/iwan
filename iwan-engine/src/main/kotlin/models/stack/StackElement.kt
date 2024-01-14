package dev.fir3.iwan.engine.models.stack

import dev.fir3.iwan.engine.models.ModuleInstance
import dev.fir3.iwan.engine.models.Value
import dev.fir3.iwan.io.wasm.models.instructions.Instruction

sealed interface StackElement

data class StackFrame(
    val locals: Array<Value>,
    val module: ModuleInstance,
    val arity: Int
): StackElement {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StackFrame

        if (!locals.contentEquals(other.locals)) return false
        if (module != other.module) return false

        return true
    }

    override fun hashCode(): Int {
        var result = locals.contentHashCode()
        result = 31 * result + module.hashCode()
        return result
    }
}

data class StackLabel(
    val n: Int,
    val instructions: List<Instruction>,
    var instructionIndex: Int,
    val isLoop: Boolean
): StackElement

data class StackValue(val value: Value): StackElement
