package dev.fir3.iwan.engine.vm

import dev.fir3.iwan.engine.models.stack.StackElement
import dev.fir3.iwan.engine.models.stack.StackFrame

object Stack {
    private val _stack: MutableList<StackElement> = mutableListOf()

    val currentFrame: StackFrame get() = _stack
        .filterIsInstance<StackFrame>()
        .last()

    fun pop() = _stack.removeAt(_stack.size - 1)
    fun push(element: StackElement): Unit {
        _stack.add(element)
    }
}
