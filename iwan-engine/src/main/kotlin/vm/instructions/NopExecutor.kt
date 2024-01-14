package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.io.wasm.models.instructions.DataDropInstruction
import dev.fir3.iwan.io.wasm.models.instructions.ElementDropInstruction
import dev.fir3.iwan.io.wasm.models.instructions.UniqueIds

object NopExecutor : InstructionExecutionContainer {
    @InstructionExecutor(UniqueIds.DATA_DROP)
    @JvmStatic
    fun execDataDrop() { }

    @InstructionExecutor(UniqueIds.ELEMENT_DROP)
    @JvmStatic
    fun execElementDrop() { }
}
