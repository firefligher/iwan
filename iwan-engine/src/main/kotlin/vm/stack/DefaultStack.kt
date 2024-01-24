package dev.fir3.iwan.engine.vm.stack

import dev.fir3.iwan.engine.extensions.toShellType
import dev.fir3.iwan.engine.models.ModuleInstance
import dev.fir3.iwan.engine.models.ReferenceNull
import dev.fir3.iwan.engine.models.ReferenceValue
import dev.fir3.iwan.engine.models.WasmFunctionInstance
import dev.fir3.iwan.engine.models.stack.LocalType
import dev.fir3.iwan.engine.models.stack.Shell
import dev.fir3.iwan.engine.models.stack.ShellType
import dev.fir3.iwan.engine.models.stack.isValueType
import dev.fir3.iwan.io.wasm.models.instructions.Instruction
import dev.fir3.iwan.io.wasm.models.valueTypes.NumberType
import dev.fir3.iwan.io.wasm.models.valueTypes.ReferenceType
import dev.fir3.iwan.io.wasm.models.valueTypes.ValueType
import dev.fir3.iwan.io.wasm.models.valueTypes.VectorType

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

        LocalPool.release(frame.locals)
        ShellPool.releaseChain(frame)
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
        ShellPool.releaseChain(currentLabel)
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
        ShellPool.release(droppedValue)
    }

    override fun dropValue() {
        val droppedHead = checkCurrentHead()
        droppedHead.type.checkValueType()

        _currentHead = droppedHead.previous
        _currentHead?.next = null
        ShellPool.release(droppedHead)
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

    private inline fun <TValue> peekCheckedValueHead(
        expectedType: ShellType,
        retriever: (Shell) -> TValue
    ): TValue {
        val peekedHead = checkCurrentHead()
        check(peekedHead.type == expectedType) {
            "Head type expectation not met."
        }

        return retriever(peekedHead)
    }

    private inline fun <TValue> popCheckedValueHead(
        expectedType: ShellType,
        retriever: (Shell) -> TValue
    ): TValue {
        val poppedHead = checkCurrentHead()

        check(poppedHead.type == expectedType) {
            "Head type expectation not met."
        }

        _currentHead = poppedHead.previous
        _currentHead?.next = null

        val result = retriever(poppedHead)
        ShellPool.release(poppedHead)

        return result
    }

    override fun popFloat32() =
        popCheckedValueHead(ShellType.Float32, Shell::float32)

    override fun popFloat64() =
        popCheckedValueHead(ShellType.Float64, Shell::float64)

    override fun popInt32() =
        popCheckedValueHead(ShellType.Int32, Shell::int32)

    override fun popInt64() =
        popCheckedValueHead(ShellType.Int64, Shell::int64)

    override fun popIntoLocal(targetIndex: Int) = transferIntoLocal(
        targetIndex
    ) { type, callback -> popCheckedValueHead(type, callback) }

    override fun popReference(): ReferenceValue =
        popCheckedValueHead(ShellType.Reference, Shell::reference)

    override fun popVector128() = popCheckedValueHead(
        ShellType.Vector128
    ) { shell -> Pair(shell.vector128Msb, shell.vector128Lsb) }

    override fun pushFloat32(value: Float) =
        pushShell(ShellType.Float32) { shell -> shell.float32 = value }

    override fun pushFloat64(value: Double) =
        pushShell(ShellType.Float64) { shell -> shell.float64 = value }

    override fun pushFrame(function: WasmFunctionInstance) {
        val parameterTypes = function.type.parameterTypes
        val localTypes = function.code.locals
        val parameterCount = parameterTypes.size
        val localsCount = parameterCount + localTypes.size
        val locals = LocalPool.allocate(localsCount)

        for (index in 0 until localsCount) {
            if (index < parameterCount) {
                val inverseIndex = parameterCount - index - 1
                val local = locals[inverseIndex]

                when (parameterTypes[inverseIndex]) {
                    NumberType.Float32 -> popCheckedValueHead(
                        ShellType.Float32
                    ) { shell ->
                        local.type = LocalType.Float32
                        local.float32 = shell.float32
                    }

                    NumberType.Float64 -> popCheckedValueHead(
                        ShellType.Float64
                    ) { shell ->
                        local.type = LocalType.Float64
                        local.float64 = shell.float64
                    }

                    NumberType.Int32 -> popCheckedValueHead(
                        ShellType.Int32
                    ) { shell ->
                        local.type = LocalType.Int32
                        local.int32 = shell.int32
                    }

                    NumberType.Int64 -> popCheckedValueHead(
                        ShellType.Int64
                    ) { shell ->
                        local.type = LocalType.Int64
                        local.int64 = shell.int64
                    }
                    ReferenceType.ExternalReference,
                    ReferenceType.FunctionReference -> popCheckedValueHead(
                        ShellType.Reference
                    ) { shell ->
                        local.type = LocalType.Reference
                        local.reference = shell.reference
                    }

                    VectorType.Vector128 -> popCheckedValueHead(
                        ShellType.Vector128
                    ) { shell ->
                        local.type = LocalType.Vector128
                        local.vector128Msb = shell.vector128Msb
                        local.vector128Lsb = shell.vector128Lsb
                    }
                }

                continue
            }

            val local = locals[index]

            when (localTypes[index - parameterCount]) {
                NumberType.Float32 -> {
                    local.type = LocalType.Float32
                    local.float32 = 0F
                }

                NumberType.Float64 -> {
                    local.type = LocalType.Float64
                    local.float64 = 0.0
                }

                NumberType.Int32 -> {
                    local.type = LocalType.Int32
                    local.int32 = 0
                }

                NumberType.Int64 -> {
                    local.type = LocalType.Int64
                    local.int64 = 0
                }

                ReferenceType.ExternalReference -> {
                    local.type = LocalType.Reference
                    local.reference = ReferenceNull.EXTERNAL
                }
                ReferenceType.FunctionReference -> {
                    local.type = LocalType.Reference
                    local.reference = ReferenceNull.FUNCTION
                }
                VectorType.Vector128 -> {
                    local.type = LocalType.Vector128
                    local.vector128Msb = 0
                    local.vector128Lsb = 0
                }
            }
        }

        // Create the frame.

        val frame = ShellPool.allocate()

        frame.type = ShellType.Frame
        frame.locals = locals
        frame.module = function.module
        frame.resultTypes = function.type.resultTypes

        frame.previous = _currentHead
        frame.previousBody = _currentInstructions
        frame.previousBodyIndex = _currentInstructionIndex
        frame.previousFrame = _currentFrame
        frame.previousLabel = _currentLabel

        // Push the frame

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
        val frame = ShellPool.allocate()

        // Initialize frame

        frame.type = ShellType.Frame
        frame.module = module
        frame.resultTypes = resultType?.let(::listOf) ?: emptyList()

        frame.previous = _currentHead
        frame.previousBody = _currentInstructions
        frame.previousBodyIndex = _currentInstructionIndex
        frame.previousFrame = _currentFrame
        frame.previousLabel = _currentLabel

        // Push frame

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
        val label = ShellPool.allocate()

        // Initialize label

        label.type = ShellType.Label
        label.isLoop = isLoop
        label.resultTypes = resultTypes

        label.previous = _currentHead
        label.previousBody = _currentInstructions
        label.previousBodyIndex = _currentInstructionIndex
        label.previousLabel = _currentLabel

        // Push label

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
        val shell = ShellPool.allocate()
        shell.previous = _currentHead
        shell.type = type
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

    override fun teeIntoLocal(targetIndex: Int) = transferIntoLocal(
        targetIndex
    ) { type, callback -> peekCheckedValueHead(type, callback) }

    private inline fun transferIntoLocal(
        targetIndex: Int,
        valueReader: (ShellType, (Shell) -> Unit) -> Unit
    ) {
        val local = checkCurrentFrame().locals[targetIndex]

        when (local.type) {
            LocalType.Float32 -> valueReader(ShellType.Float32) { shell ->
                local.float32 = shell.float32
            }

            LocalType.Float64 -> valueReader(ShellType.Float64) { shell ->
                local.float64 = shell.float64
            }

            LocalType.Int32 -> valueReader(ShellType.Int32) { shell ->
                local.int32 = shell.int32
            }

            LocalType.Int64 -> valueReader(ShellType.Int64) { shell ->
                local.int64 = shell.int64
            }

            LocalType.Reference -> valueReader(ShellType.Reference) { shell ->
                local.reference = shell.reference
            }

            LocalType.Vector128 -> valueReader(ShellType.Vector128) { shell ->
                local.vector128Lsb = shell.vector128Lsb
                local.vector128Msb = shell.vector128Msb
            }
        }
    }
}

private fun ShellType.checkValueType() = check(this.isValueType()) {
    "Attempted to drop non-value element from stack"
}
