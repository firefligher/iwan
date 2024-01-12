package dev.fir3.iwan.io.wasm.serialization.sections

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.DeserializationStrategy
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.expectInt8
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.sections.Section
import java.io.IOException

internal abstract class BaseSectionStrategy<TSection : Section>(
    private val _sectionId: Byte
) : DeserializationStrategy<TSection> {
    @Throws(IOException::class)
    final override fun deserialize(
        source: ByteSource,
        context: DeserializationContext
    ): TSection {
        source.expectInt8(_sectionId)
        return deserialize(source, context, source.readVarUInt32())
    }

    @Throws(IOException::class)
    protected abstract fun deserialize(
        source: ByteSource,
        context: DeserializationContext,
        sectionSize: UInt
    ): TSection
}
