package dev.fir3.iwan.jvm.models.bytecode

import kotlin.reflect.KClass

data class GeneratedClass<TInterface : Any>(
    val name: String,
    val data: ByteArray,
    val classInterface: KClass<TInterface>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GeneratedClass<*>

        if (name != other.name) return false
        if (!data.contentEquals(other.data)) return false
        if (classInterface != other.classInterface) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + classInterface.hashCode()
        return result
    }
}
