package dev.fir3.iwan.io.wasm.serialization.sections

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.deserialize
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.TableType
import dev.fir3.iwan.io.wasm.models.sections.TableSection
import java.io.IOException

internal object TableSectionStrategy : BaseSectionStrategy<TableSection>(
    SectionIds.TABLE_SECTION
) {
    @Throws(IOException::class)
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext,
        sectionSize: UInt
    ): TableSection {
        val typeCount = source.readVarUInt32()
        val types = mutableListOf<TableType>()

        for (i in 0u until typeCount) {
            types.add(context.deserialize(source))
        }

        return TableSection(types)
    }
}
