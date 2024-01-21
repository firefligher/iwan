package dev.fir3.iwan.engine.vm

import dev.fir3.iwan.engine.models.stack.StackElement
import dev.fir3.iwan.engine.models.stack.StackFrame
import dev.fir3.iwan.engine.models.stack.StackLabel

object Stack {
    val _stack: MutableList<StackElement> = mutableListOf()

    val currentFrame: StackFrame get() = _stack
        .reversed()
        .filterIsInstance<StackFrame>()
        .first()

    val currentLabel: StackLabel? get() {
        val label = labels.firstOrNull() ?: return null
        val labelIndex = _stack.lastIndexOf(label)
        val frameIndex = _stack.lastIndexOf(currentFrame)
        return if (labelIndex > frameIndex) label else null
    }

    val labels get() = _stack
        .reversed()
        .filterIsInstance<StackLabel>()

    fun pop() = _stack.removeAt(_stack.size - 1)

    fun push(element: StackElement): Unit {
        _stack.add(element)
    }
}
