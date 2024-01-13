package dev.fir3.iwan.engine.models

import dev.fir3.iwan.io.wasm.models.MemoryType

data class MemoryInstance(
    val type: MemoryType,
    val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MemoryInstance

        if (type != other.type) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}
