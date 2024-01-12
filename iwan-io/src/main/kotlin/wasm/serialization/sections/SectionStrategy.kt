package dev.fir3.iwan.io.wasm.serialization.sections

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.DeserializationStrategy
import dev.fir3.iwan.io.serialization.deserialize
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.peekInt8
import dev.fir3.iwan.io.wasm.models.sections.*
import java.io.IOException

internal object SectionStrategy : DeserializationStrategy<Section> {
    @Throws(IOException::class)
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext
    ): Section {
        val sectionId = source.peekInt8()

        return when (sectionId) {
            SectionIds.CODE_SECTION -> context.deserialize<CodeSection>(source)
            SectionIds.CUSTOM_SECTION -> context
                .deserialize<CustomSection>(source)

            SectionIds.DATA_SECTION -> context.deserialize<DataSection>(source)
            SectionIds.ELEMENT_SECTION -> context
                .deserialize<ElementSection>(source)

            SectionIds.EXPORT_SECTION -> context
                .deserialize<ExportSection>(source)

            SectionIds.FUNCTION_SECTION -> context
                .deserialize<FunctionSection>(source)

            SectionIds.GLOBAL_SECTION -> context
                .deserialize<GlobalSection>(source)

            SectionIds.IMPORT_SECTION -> context
                .deserialize<ImportSection>(source)

            SectionIds.MEMORY_SECTION -> context
                .deserialize<MemorySection>(source)

            SectionIds.TABLE_SECTION -> context
                .deserialize<TableSection>(source)

            SectionIds.TYPE_SECTION -> context.deserialize<TypeSection>(source)
            else -> throw IOException("Unsupported section id '$sectionId'")
        }
    }
}
