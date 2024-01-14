package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.models.Int32Value
import dev.fir3.iwan.engine.models.ReferenceValue
import dev.fir3.iwan.engine.models.stack.StackValue
import dev.fir3.iwan.engine.vm.Stack
import dev.fir3.iwan.engine.vm.Store
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
            .currentFrame
            .module
            .tableAddresses[instruction.tableIndex.toInt()]

        val elementAddress = stack
            .currentFrame
            .module
            .elementAddresses[instruction.elementIndex.toInt()]

        val table = store.tables[tableAddress].elements
        val element = store.elements[elementAddress].elements

        val count = ((stack.pop() as StackValue).value as Int32Value).value
        val srcIndex = ((stack.pop() as StackValue).value as Int32Value).value
        val dstIndex = ((stack.pop() as StackValue).value as Int32Value).value

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
            .currentFrame
            .module
            .tableAddresses[instruction.tableIndex.toInt()]

        val table = store.tables[tableAddress].elements
        val value = (stack.pop() as StackValue).value as ReferenceValue
        val index = ((stack.pop() as StackValue).value as Int32Value).value

        if (index >= table.size) {
            throw IllegalStateException("Table set exceeds table boundaries")
        }

        table[index] = value
    }
}