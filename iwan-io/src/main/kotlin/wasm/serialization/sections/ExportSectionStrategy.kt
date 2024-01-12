package dev.fir3.iwan.io.wasm.serialization.sections

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.deserialize
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.Export
import dev.fir3.iwan.io.wasm.models.sections.ExportSection
import java.io.IOException

internal object ExportSectionStrategy : BaseSectionStrategy<ExportSection>(
    SectionIds.EXPORT_SECTION
) {
    @Throws(IOException::class)
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext,
        sectionSize: UInt
    ): ExportSection {
        val exportsCount = source.readVarUInt32()
        val exports = mutableListOf<Export>()

        for (i in 0u until exportsCount) {
            exports.add(context.deserialize(source))
        }

        return ExportSection(exports)
    }
}
