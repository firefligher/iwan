package dev.fir3.iwan.io.wasm.serialization

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.DeserializationStrategy
import dev.fir3.iwan.io.serialization.deserialize
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readInt8
import dev.fir3.iwan.io.wasm.models.GlobalType
import dev.fir3.iwan.io.wasm.models.valueTypes.ValueType
import java.io.IOException

internal object GlobalTypeStrategy : DeserializationStrategy<GlobalType> {
    private const val IMMUTABLE: Byte = 0
    private const val MUTABLE: Byte = 1

    @Throws(IOException::class)
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext
    ): GlobalType {
        val valueType = context.deserialize<ValueType>(source)
        val isMutable = when (source.readInt8()) {
            IMMUTABLE -> false
            MUTABLE -> true
            else -> throw IOException("Invalid mutability")
        }

        return GlobalType(isMutable, valueType)
    }
}
