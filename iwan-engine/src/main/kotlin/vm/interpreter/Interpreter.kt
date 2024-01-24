package dev.fir3.iwan.engine.vm.interpreter

import dev.fir3.iwan.engine.models.vm.InterpreterState
import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.engine.vm.stack.Stack
import dev.fir3.iwan.io.wasm.models.instructions.BlockTypeInstruction
import dev.fir3.iwan.io.wasm.models.instructions.FlatInstruction

class Interpreter(stack: Stack) {
    private val _jumpClass = JumpClassFactory.create()
    private val _stack = stack

    fun execute() {
        val state = InterpreterState(
            _stack,
            Store,
            FlatInstruction.UNREACHABLE
        )

        while (true) {
            val instruction = _stack.nextInstruction() ?: break
            state.instruction = instruction

            try {
                _jumpClass.evaluate(instruction.uniqueId, state)
            } catch (ex: UnsupportedOperationException) {
                println(instruction)
                throw ex
            }
        }
    }
}
