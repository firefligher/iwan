package dev.fir3.iwan.io.wasm.serialization.sections

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.deserialize
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.MemoryType
import dev.fir3.iwan.io.wasm.models.sections.MemorySection
import java.io.IOException

internal object MemorySectionStrategy : BaseSectionStrategy<MemorySection>(
    SectionIds.MEMORY_SECTION
) {
    @Throws(IOException::class)
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext,
        sectionSize: UInt
    ): MemorySection {
        val memoryCount = source.readVarUInt32()

        if (memoryCount > Int.MAX_VALUE.toUInt()) {
            throw IOException("Unsupported number of memories")
        }

        val memories = mutableListOf<MemoryType>()

        for (i in 0u until memoryCount) {
            memories.add(context.deserialize(source))
        }

        return MemorySection(memories)
    }
}