package dev.fir3.iwan.io.wasm.serialization.sections

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.sections.FunctionSection
import java.io.IOException

internal object FunctionSectionStrategy : BaseSectionStrategy<FunctionSection>(
    SectionIds.FUNCTION_SECTION
) {
    @Throws(IOException::class)
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext,
        sectionSize: UInt
    ): FunctionSection {
        val typeCount = source.readVarUInt32()
        val types = mutableListOf<UInt>()

        for (i in 0u until typeCount) {
            types.add(source.readVarUInt32())
        }

        return FunctionSection(types)
    }
}