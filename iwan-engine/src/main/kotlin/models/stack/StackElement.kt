package dev.fir3.iwan.engine.models.stack

import dev.fir3.iwan.engine.models.ModuleInstance
import dev.fir3.iwan.engine.models.Value

sealed interface StackElement

data class StackFrame(
    val locals: Array<Value>,
    val module: ModuleInstance
): StackElement

data class StackValue(val value: Value): StackElement
