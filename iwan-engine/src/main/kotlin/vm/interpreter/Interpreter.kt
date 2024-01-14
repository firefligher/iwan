package dev.fir3.iwan.engine.vm.interpreter

import dev.fir3.iwan.engine.models.Value
import dev.fir3.iwan.engine.models.stack.StackValue
import dev.fir3.iwan.engine.vm.Stack
import dev.fir3.iwan.io.wasm.models.Expression
import dev.fir3.iwan.io.wasm.models.instructions.Instruction

object Interpreter {
    private val _jumpClass = JumpClassFactory.create()

    fun execute(instruction: Instruction) {
        _jumpClass.evaluate(instruction.uniqueId, instruction)
    }

    fun execute(expression: Expression) {
        for (instruction in expression.body) {
            execute(instruction)
        }
    }

    fun executeAndGetValue(expression: Expression): Value {
        execute(expression)
        val value = Stack.pop()

        if (value !is StackValue) {
            throw IllegalStateException("No value on top of stack")
        }

        return value.value
    }
}