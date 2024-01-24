package dev.fir3.iwan.engine.vm.stack

import dev.fir3.iwan.engine.models.EmptyModuleInstance
import dev.fir3.iwan.engine.models.ReferenceNull
import dev.fir3.iwan.engine.models.stack.LocalContainer
import dev.fir3.iwan.engine.models.stack.Shell
import dev.fir3.iwan.io.wasm.models.instructions.Instruction
import dev.fir3.iwan.io.wasm.models.valueTypes.ValueType

object ShellPool {
    private val EMPTY_BODY = emptyList<Instruction>()
    private val EMPTY_MODULE = EmptyModuleInstance
    private val EMPTY_REFERENCE = ReferenceNull.EXTERNAL

    private val EMPTY_RESULT_TYPES = emptyList<ValueType>()

    private var _nextShell: Shell? = null

    fun allocate(): Shell {
        val shell = _nextShell ?: return Shell(
            locals = LocalContainer.EMPTY_CONTAINER,
            module = EMPTY_MODULE,
            previousBody = EMPTY_BODY,
            resultTypes = EMPTY_RESULT_TYPES
        )

        _nextShell = shell.previous
        shell.next = null
        shell.previous = null
        return shell
    }

    private fun resetValueReferences(shell: Shell) {
        shell.locals = LocalContainer.EMPTY_CONTAINER
        shell.module = EMPTY_MODULE
        shell.resultTypes = EMPTY_RESULT_TYPES
        shell.previousBody = EMPTY_BODY
        shell.reference = EMPTY_REFERENCE
    }

    fun release(shell: Shell) {
        // Clear all references to avoid to GC issues.

        resetValueReferences(shell)

        // Add to internal list.

        shell.previous = _nextShell
        shell.next = null
        _nextShell?.next = shell
        _nextShell = shell
    }

    fun releaseChain(shell: Shell) {
        val shellPredecessor = shell.previous

        if (shellPredecessor !== null && shellPredecessor.next === shell) {
            var current: Shell = shellPredecessor
            var predecessor = current.previous

            // Remove the tail from the shell. We handle it independently now.

            current.next = null
            shell.previous = null

            while (predecessor !== null && predecessor.next === current) {
                resetValueReferences(current)
                current = predecessor
                predecessor = current.previous
            }

            // Insert cleaned tail into the pool

            current.previous = _nextShell
            _nextShell?.next = current
            _nextShell = current
        }

        // Do the chain clearing upwards the chain.

        var current = shell
        var successor = current.next

        while (successor !== null && successor.previous === current) {
            resetValueReferences(current)
            current = successor
            successor = current.next
        }

        // Cut off any invalid successors.

        current.next = null

        // Insert the cleaned chain into the pool.

        shell.previous = _nextShell
        _nextShell?.next = shell
        _nextShell = shell
    }
}
