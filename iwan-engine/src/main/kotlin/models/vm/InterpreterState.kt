package dev.fir3.iwan.engine.models.vm

import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.engine.vm.stack.Stack
import dev.fir3.iwan.io.wasm.models.instructions.Instruction

data class InterpreterState(
    val stack: Stack,
    val store: Store,
    var instruction: Instruction
)
