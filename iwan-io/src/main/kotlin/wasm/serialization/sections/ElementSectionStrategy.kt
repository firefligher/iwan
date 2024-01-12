package dev.fir3.iwan.io.wasm.serialization.sections

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.deserialize
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.ElementSegment
import dev.fir3.iwan.io.wasm.models.sections.ElementSection

internal object ElementSectionStrategy : BaseSectionStrategy<ElementSection>(
    SectionIds.ELEMENT_SECTION
) {
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext,
        sectionSize: UInt
    ): ElementSection {
        val count = source.readVarUInt32()
        val elements = mutableListOf<ElementSegment>()

        for (i in 0u until count) {
            elements.add(context.deserialize(source))
        }

        return ElementSection(elements)
    }
}
