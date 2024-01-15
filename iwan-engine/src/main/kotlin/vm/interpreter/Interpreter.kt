package dev.fir3.iwan.engine.vm.interpreter

import dev.fir3.iwan.engine.models.stack.StackLabel
import dev.fir3.iwan.engine.models.stack.StackValue
import dev.fir3.iwan.engine.vm.Stack
import dev.fir3.iwan.io.wasm.models.instructions.CallInstruction

object Interpreter {
    private val _jumpClass = JumpClassFactory.create()

    fun execute() {
        while (true) {
            val label = Stack.currentLabel ?: break
            val instructions = label.instructions
            val nextInstruction = label.instructionIndex

            if (instructions.size <= nextInstruction) {
                popLabel()
                continue
            }

            val instruction = instructions[nextInstruction]
            label.instructionIndex++
            println(instruction)
            _jumpClass.evaluate(instruction.uniqueId, instruction)
        }
    }

    private fun popLabel() {
        val values = mutableListOf<StackValue>()

        while (true) {
            val value = Stack.pop()

            if (value is StackLabel) break
            if (value is StackValue) {
                values += value
                continue
            }

            throw IllegalStateException(
                "Encountered unexpected object on stack"
            )
        }

        values.reversed().forEach(Stack::push)
    }
}