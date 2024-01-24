package dev.fir3.iwan.engine.vm.stack

import dev.fir3.iwan.engine.models.stack.Local
import dev.fir3.iwan.engine.models.stack.LocalContainer

object LocalPool {
    private val _containers = mutableMapOf<Int, LocalContainer>()

    fun allocate(size: Int): LocalContainer {
        if (size == 0) return LocalContainer.EMPTY_CONTAINER

        val container = _containers[size]
            ?: return LocalContainer(Array(size) { Local() })

        val predecessor = container.previous

        if (predecessor == null) {
            _containers.remove(size)
        } else {
            _containers[size] = predecessor
        }

        container.previous = null
        return container
    }

    fun release(container: LocalContainer) {
        container.previous = _containers[container.size]
        _containers[container.size] = container
    }
}
