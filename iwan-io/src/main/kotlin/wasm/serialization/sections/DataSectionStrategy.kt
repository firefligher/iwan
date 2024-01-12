package dev.fir3.iwan.io.wasm.serialization.sections

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.deserializeVector
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.wasm.models.sections.DataSection

internal object DataSectionStrategy : BaseSectionStrategy<DataSection>(
    SectionIds.DATA_SECTION
) {
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext,
        sectionSize: UInt
    ) = DataSection(context.deserializeVector(source))
}
