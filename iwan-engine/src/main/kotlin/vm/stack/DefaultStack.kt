package dev.fir3.iwan.engine.vm.stack

import dev.fir3.iwan.engine.extensions.defaultLocal
import dev.fir3.iwan.engine.extensions.toShellType
import dev.fir3.iwan.engine.models.ModuleInstance
import dev.fir3.iwan.engine.models.ReferenceValue
import dev.fir3.iwan.engine.models.WasmFunctionInstance
import dev.fir3.iwan.engine.models.stack.*
import dev.fir3.iwan.io.wasm.models.instructions.Instruction
import dev.fir3.iwan.io.wasm.models.valueTypes.ValueType

class DefaultStack : Stack {
    private var _currentFrame: Shell? = null
    private var _currentHead: Shell? = null
    private var _currentLabel: Shell? = null
    private var _currentInstructions = emptyList<Instruction>()
    private var _currentInstructionIndex = -1

    override val currentModule: ModuleInstance
        get() = checkCurrentFrame().module

    private fun checkCurrentFrame() = checkNotNull(_currentFrame) {
        "No frame available."
    }

    private fun checkCurrentHead() = checkNotNull(_currentHead) {
        "Empty stack."
    }

    private fun checkCurrentLabel() = checkNotNull(_currentLabel) {
        "No label available."
    }

    override fun dropFrame(isReturn: Boolean) {
        val frame = checkCurrentFrame()

        // If we drop the frame due to a return instruction, we need to clean
        // up the stack, because we cannot assume that the stack frame is only
        // followed by the return values.

        if (isReturn) {
            // Find the down-most (stack grows bottom to top) value that
            // represents a parameter.

            val resultTypes = frame.resultTypes
            val resultTypeCount = resultTypes.size
            var stackCursor = checkCurrentHead()

            for (index in resultTypeCount - 1 downTo 0) {
                val expectedType = resultTypes[index].toShellType()
                val resultType = stackCursor.type

                check(resultType == expectedType) {
                    "Encountered unexpected result type"
                }

                stackCursor = checkNotNull(stackCursor.previous) {
                    "Encountered unexpected end of stack"
                }
            }

            val firstResultValue = stackCursor.next

            // Remove everything between the stack frame and the first return
            // value from the stack.
            //
            // WARNING: If there are labels between the stack frame and first
            //          parameter, this operation will invalidate
            //          _currentLabel, _currentInstructions and
            //          _currentInstructionIndex.
            //          Here, this does not matter, because we change those
            //          values when we eventually drop the stack frame anyway.

            frame.next = firstResultValue
            firstResultValue?.previous = frame
        }

        // Fix the linkage.

        val predecessor = frame.previous
        val successor = frame.next

        successor?.previous = predecessor
        predecessor?.next = successor

        // Fix remaining state.

        if (successor == null) {
            _currentHead = predecessor
        }

        _currentFrame = frame.previousFrame
        _currentInstructions = frame.previousBody
        _currentInstructionIndex = frame.previousBodyIndex
        _currentLabel = frame.previousLabel
    }

    override fun dropLabels(count: Int, isBranch: Boolean) {
        require(count > 0) { "Cannot drop zero labels." }
        val currentLabel = checkCurrentLabel()
        var lastLabel = currentLabel

        // Skip over (count - 1) labels that we want to skip.

        for (index in 0 until count - 1) {
            lastLabel = checkNotNull(lastLabel.previousLabel) {
                "Encountered missing label"
            }
        }

        // The last label that we drop determines the number of the topmost
        // stack values that we keep.

        val resultTypes = lastLabel.resultTypes
        val resultTypeCount = resultTypes.size

        // Find first value that we do not drop.

        var stackCursor = checkCurrentHead()

        for (index in resultTypeCount - 1 downTo 0) {
            val expectedType = resultTypes[index].toShellType()
            val resultType = stackCursor.type

            check(resultType == expectedType) {
                "Encountered unexpected result type"
            }

            stackCursor = checkNotNull(stackCursor.previous) {
                "Encountered unexpected end of stack"
            }
        }

        check(isBranch || stackCursor === currentLabel) {
            "Encountered unexpected stack layout"
        }

        val firstParameter = stackCursor.next

        // If the current label dropping operation results from a branch and
        // the last label, that we drop, is a loop, we need to ensure that we
        // re-enter the loop with the next instruction.

        val isLoop = isBranch && lastLabel.isLoop

        // Fix the linkage between the predecessor of the last label, that we
        // drop, and the first parameter that we keep.

        val labelPredecessor = lastLabel.previous
        firstParameter?.previous = labelPredecessor
        labelPredecessor?.next = firstParameter

        // Fix the remaining state.

        if (firstParameter == null) {
            _currentHead = labelPredecessor
        }

        _currentInstructions = lastLabel.previousBody
        _currentInstructionIndex = lastLabel.previousBodyIndex
        _currentLabel = lastLabel.previousLabel

        if (isLoop) _currentInstructionIndex--
    }

    override fun dropPreviousValue() {
        val currentHead = checkCurrentHead()
        val droppedValue = checkNotNull(currentHead.previous) {
            "Attempted to drop non-existent predecessor from stack"
        }

        droppedValue.type.checkValueType()

        // Fix linkage

        currentHead.previous = droppedValue.previous
        droppedValue.previous?.next = currentHead
    }

    override fun dropValue() {
        val droppedHead = checkCurrentHead()
        droppedHead.type.checkValueType()

        _currentHead = droppedHead.previous
        _currentHead?.next = null
    }

    override fun nextInstruction(): Instruction? {
        while (
            _currentInstructionIndex >= _currentInstructions.size &&
            _currentFrame != null
        ) {
            if (_currentLabel != null) {
                dropLabels(count = 1, isBranch = false)
            } else {
                dropFrame(isReturn = false)
            }
        }

        return if (_currentFrame == null) {
            null
        } else {
            _currentInstructions[_currentInstructionIndex++]
        }
    }

    private fun peekCheckedValueHead(expectedType: ShellType): Shell {
        val peekedHead = checkCurrentHead()
        check(peekedHead.type == expectedType) {
            "Head type expectation not met."
        }

        return peekedHead
    }

    private fun popCheckedValueHead(expectedType: ShellType): Shell {
        val poppedHead = checkCurrentHead()

        check(poppedHead.type == expectedType) {
            "Head type expectation not met."
        }

        _currentHead = poppedHead.previous
        _currentHead?.next = null
        return poppedHead
    }

    override fun popFloat32() = popCheckedValueHead(ShellType.Float32).float32
    override fun popFloat64() = popCheckedValueHead(ShellType.Float64).float64
    override fun popInt32() = popCheckedValueHead(ShellType.Int32).int32
    override fun popInt64() = popCheckedValueHead(ShellType.Int64).int64

    override fun popIntoLocal(targetIndex: Int) =
        transferIntoLocal(targetIndex, ::popCheckedValueHead)

    override fun popReference(): ReferenceValue =
        popCheckedValueHead(ShellType.Reference).reference

    override fun popVector128(): Pair<Long, Long> {
        val shell = popCheckedValueHead(ShellType.Vector128)
        return Pair(shell.vector128Msb, shell.vector128Lsb)
    }

    override fun pushFloat32(value: Float) =
        pushShell(ShellType.Float32) { shell -> shell.float32 = value }

    override fun pushFloat64(value: Double) =
        pushShell(ShellType.Float64) { shell -> shell.float64 = value }

    override fun pushFrame(function: WasmFunctionInstance) {
        val parameterTypes = function.type.parameterTypes
        val localTypes = function.code.locals
        val parameterCount = parameterTypes.size

        val locals = Array(parameterCount + localTypes.size) { index ->
            if (index < parameterCount) {
                // On stack, the values are in the inverse order.

                popCheckedValueHead(
                    parameterTypes[parameterCount - index - 1].toShellType()
                ).toLocal()
            } else {
                localTypes[index - parameterCount].defaultLocal
            }
        }

        // Correct the order of the parameters to match the (type) expectation.

        for (index in 0 until (parameterCount shr 1)) {
            val tmpCopy = locals[index]
            val peerIndex = parameterCount - index - 1

            locals[index] = locals[peerIndex]
            locals[peerIndex] = tmpCopy
        }

        // Create the frame.

        val frame = Shell(
            type = ShellType.Frame,
            locals = locals,
            module = function.module,
            resultTypes = function.type.resultTypes,

            previous = _currentHead,
            previousBody = _currentInstructions,
            previousBodyIndex = _currentInstructionIndex,
            previousFrame = _currentFrame,
            previousLabel = _currentLabel
        )

        _currentHead?.next = frame
        _currentHead = frame
        _currentFrame = frame
        _currentInstructions = function.code.body.body
        _currentInstructionIndex = 0
        _currentLabel = null
    }

    override fun pushFromLocal(targetIndex: Int) {
        val local = checkCurrentFrame().locals[targetIndex]

        when (local.type) {
            LocalType.Float32 -> pushShell(ShellType.Float32) { shell ->
                shell.float32 = local.float32
            }

            LocalType.Float64 -> pushShell(ShellType.Float64) { shell ->
                shell.float64 = local.float64
            }

            LocalType.Int32 -> pushShell(ShellType.Int32) { shell ->
                shell.int32 = local.int32
            }

            LocalType.Int64 -> pushShell(ShellType.Int64) { shell ->
                shell.int64 = local.int64
            }

            LocalType.Reference -> pushShell(ShellType.Reference) { shell ->
                shell.reference = local.reference
            }

            LocalType.Vector128 -> pushShell(ShellType.Vector128) { shell ->
                shell.vector128Lsb = local.vector128Lsb
                shell.vector128Msb = local.vector128Msb
            }

        }
    }

    override fun pushInitializerFrame(
        module: ModuleInstance,
        resultType: ValueType?,
        instructions: List<Instruction>
    ) {
        val frame = Shell(
            type = ShellType.Frame,
            module = module,
            resultTypes = resultType?.let(::listOf) ?: emptyList(),

            previous = _currentHead,
            previousBody = _currentInstructions,
            previousBodyIndex = _currentInstructionIndex,
            previousFrame = _currentFrame,
            previousLabel = _currentLabel
        )

        _currentHead?.next = frame
        _currentHead = frame
        _currentFrame = frame
        _currentInstructions = instructions
        _currentInstructionIndex = 0
        _currentLabel = null
    }

    override fun pushInt32(value: Int) =
        pushShell(ShellType.Int32) { shell -> shell.int32 = value }

    override fun pushInt64(value: Long) =
        pushShell(ShellType.Int64) { shell -> shell.int64 = value }

    override fun pushLabel(
        resultTypes: List<ValueType>,
        instructions: List<Instruction>,
        isLoop: Boolean
    ) {
        val label = Shell(
            type = ShellType.Label,
            isLoop = isLoop,
            resultTypes = resultTypes,

            previous = _currentHead,
            previousBody = _currentInstructions,
            previousBodyIndex = _currentInstructionIndex,
            previousLabel = _currentLabel
        )

        _currentHead?.next = label
        _currentHead = label
        _currentInstructions = instructions
        _currentInstructionIndex = 0
        _currentLabel = label
    }

    override fun pushReference(value: ReferenceValue) =
        pushShell(ShellType.Reference) { shell -> shell.reference = value }

    private inline fun pushShell(
        type: ShellType,
        initializer: (Shell) -> Unit
    ) {
        val shell = Shell(previous = _currentHead, type = type)
        initializer(shell)

        _currentHead?.next = shell
        _currentHead = shell
    }

    override fun pushVector128(msb: Long, lsb: Long) = pushShell(
        ShellType.Vector128
    ) { shell ->
        shell.vector128Msb = msb
        shell.vector128Lsb = lsb
    }

    override fun teeIntoLocal(targetIndex: Int) =
        transferIntoLocal(targetIndex, ::peekCheckedValueHead)

    private inline fun transferIntoLocal(
        targetIndex: Int,
        valueReader: (ShellType) -> Shell
    ) {
        val local = checkCurrentFrame().locals[targetIndex]

        when (local.type) {
            LocalType.Float32 -> local.float32 =
                valueReader(ShellType.Float32).float32

            LocalType.Float64 -> local.float64 =
                valueReader(ShellType.Float64).float64

            LocalType.Int32 -> local.int32 = valueReader(ShellType.Int32).int32
            LocalType.Int64 -> local.int64 = valueReader(ShellType.Int64).int64
            LocalType.Reference -> local.reference =
                valueReader(ShellType.Reference).reference

            LocalType.Vector128 -> {
                val value = valueReader(ShellType.Vector128)
                local.vector128Lsb = value.vector128Lsb
                local.vector128Msb = value.vector128Msb
            }
        }
    }
}

private fun ShellType.checkValueType() = check(this.isValueType()) {
    "Attempted to drop non-value element from stack"
}
