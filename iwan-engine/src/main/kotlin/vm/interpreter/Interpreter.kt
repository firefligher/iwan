package dev.fir3.iwan.engine.vm.interpreter

import dev.fir3.iwan.engine.models.stack.StackLabel
import dev.fir3.iwan.engine.models.stack.StackValue
import dev.fir3.iwan.engine.vm.Stack
import dev.fir3.iwan.io.wasm.models.instructions.CallInstruction
import dev.fir3.iwan.io.wasm.models.instructions.UniqueIds
import java.lang.UnsupportedOperationException

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

            if (instruction.uniqueId == UniqueIds.RETURN) {
                break
            }

            try {
                _jumpClass.evaluate(instruction.uniqueId, instruction)
            } catch (ex: UnsupportedOperationException) {
                println(instruction)
                throw ex
            }
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