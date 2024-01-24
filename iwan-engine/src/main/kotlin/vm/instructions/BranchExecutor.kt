package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.vm.stack.Stack
import dev.fir3.iwan.io.wasm.models.instructions.ConditionalBranchInstruction
import dev.fir3.iwan.io.wasm.models.instructions.TableBranchInstruction
import dev.fir3.iwan.io.wasm.models.instructions.UnconditionalBranchInstruction
import dev.fir3.iwan.io.wasm.models.instructions.UniqueIds

object BranchExecutor : InstructionExecutionContainer {
    @InstructionExecutor(UniqueIds.CONDITIONAL_BRANCH)
    @JvmStatic
    fun execConditional(
        stack: Stack,
        instruction: ConditionalBranchInstruction
    ) {
        val value = stack.popInt32()

        if (value != 0) {
            stack.dropLabels(
                instruction.labelIndex.toInt() + 1,
                isBranch = true
            )
        }
    }

    @InstructionExecutor(UniqueIds.TABLE_BRANCH)
    @JvmStatic
    fun execTable(stack: Stack, instruction: TableBranchInstruction) {
        val index = stack.popInt32()

        stack.dropLabels(
            if (index < instruction.labelIndices.size) {
                instruction.labelIndices[index].toInt()
            } else {
                instruction
                    .labelIndices[instruction.tableIndex.toInt()]
                    .toInt()
            } + 1,
            isBranch = true
        )
    }

    @InstructionExecutor(UniqueIds.UNCONDITIONAL_BRANCH)
    @JvmStatic
    fun execUnconditional(
        stack: Stack,
        instruction: UnconditionalBranchInstruction
    ) = stack.dropLabels(instruction.labelIndex.toInt() + 1, isBranch = true)
}
