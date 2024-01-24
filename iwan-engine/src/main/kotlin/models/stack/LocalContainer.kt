package dev.fir3.iwan.engine.models.stack

data class LocalContainer(
    val locals: Array<Local>,

    // Pool structures

    var previous: LocalContainer? = null
) {
    companion object {
        val EMPTY_CONTAINER = LocalContainer(emptyArray())
    }

    val size get() = locals.size

    operator fun get(index: Int) = locals[index]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocalContainer

        return locals.contentEquals(other.locals)
    }

    override fun hashCode(): Int {
        return locals.contentHashCode()
    }
}
