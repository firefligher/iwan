package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.engine.vm.stack.Stack
import dev.fir3.iwan.io.wasm.models.instructions.TableInitInstruction
import dev.fir3.iwan.io.wasm.models.instructions.TableSetInstruction
import dev.fir3.iwan.io.wasm.models.instructions.UniqueIds

object TableExecutor : InstructionExecutionContainer {
    @InstructionExecutor(UniqueIds.TABLE_INIT)
    @JvmStatic
    fun execInit(
        store: Store,
        stack: Stack,
        instruction: TableInitInstruction
    ) {
        val tableAddress = stack
            .currentModule
            .tableAddresses[instruction.tableIndex.toInt()]

        val elementAddress = stack
            .currentModule
            .elementAddresses[instruction.elementIndex.toInt()]

        val table = store.tables[tableAddress].elements
        val element = store.elements[elementAddress].elements

        val count = stack.popInt32()
        val srcIndex = stack.popInt32()
        val dstIndex = stack.popInt32()

        val trap = (srcIndex + count > element.size) or
                (dstIndex + count > table.size)

        if (trap) {
            throw IllegalStateException(
                "Table initialization exceeds boundaries"
            )
        }

        for (index in 0 until count) {
            table[dstIndex + index] = element[srcIndex + index]
        }
    }

    @InstructionExecutor(UniqueIds.TABLE_SET)
    @JvmStatic
    fun execSet(store: Store, stack: Stack, instruction: TableSetInstruction) {
        val tableAddress = stack
            .currentModule
            .tableAddresses[instruction.tableIndex.toInt()]

        val table = store.tables[tableAddress].elements
        val value = stack.popReference()
        val index = stack.popInt32()

        if (index >= table.size) {
            throw IllegalStateException("Table set exceeds table boundaries")
        }

        table[index] = value
    }
}