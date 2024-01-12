package dev.fir3.iwan.io.wasm.serialization.sections

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.deserialize
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.Import
import dev.fir3.iwan.io.wasm.models.sections.ImportSection

internal object ImportSectionStrategy : BaseSectionStrategy<ImportSection>(
    SectionIds.IMPORT_SECTION
) {
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext,
        sectionSize: UInt
    ): ImportSection {
        val importCount = source.readVarUInt32()
        val imports = mutableListOf<Import>()

        for (i in 0u until importCount) {
            imports.add(context.deserialize(source))
        }

        return ImportSection(imports)
    }
}
