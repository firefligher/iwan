package dev.fir3.iwan.engine.vm.instructions

import dev.fir3.iwan.engine.models.Int32Value
import dev.fir3.iwan.engine.models.stack.StackLabel
import dev.fir3.iwan.engine.models.stack.StackValue
import dev.fir3.iwan.engine.vm.Stack
import dev.fir3.iwan.io.wasm.models.instructions.ConditionalBranchInstruction
import dev.fir3.iwan.io.wasm.models.instructions.UnconditionalBranchInstruction
import dev.fir3.iwan.io.wasm.models.instructions.UniqueIds

object BranchExecutor : InstructionExecutionContainer {
    private fun execJump(stack: Stack, labelIndex: Int) {
        val label = stack.labels[labelIndex]
        val parameterValues = label
            .n
            .downTo(1)
            .map { Stack.pop() as StackValue }

        labelIndex.downTo(0).forEach { _ ->
            while (true) {
                val element = Stack.pop()

                if (element is StackValue) continue
                if (element is StackLabel) break

                throw IllegalStateException(
                    "Encountered unexpected value on stack"
                )
            }
        }

        if (label.isLoop) {
            // If we branch to a loop instruction, we want to decrement the
            // instruction pointer. Otherwise, there is no loop.

            stack.currentLabel!!.instructionIndex--
        }

        parameterValues.reversed().forEach(Stack::push)
    }

    @InstructionExecutor(UniqueIds.CONDITIONAL_BRANCH)
    @JvmStatic
    fun execConditional(
        stack: Stack,
        instruction: ConditionalBranchInstruction
    ) {
        val value = ((stack.pop() as StackValue).value as Int32Value).value
        if (value != 0) execJump(stack, instruction.labelIndex.toInt())
    }

    @InstructionExecutor(UniqueIds.UNCONDITIONAL_BRANCH)
    @JvmStatic
    fun execUnconditional(
        stack: Stack,
        instruction: UnconditionalBranchInstruction
    ) = execJump(stack, instruction.labelIndex.toInt())
}
