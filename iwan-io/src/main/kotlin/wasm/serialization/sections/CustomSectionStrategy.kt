package dev.fir3.iwan.io.wasm.serialization.sections

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.read
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.sections.CustomSection
import java.io.IOException

internal object CustomSectionStrategy : BaseSectionStrategy<CustomSection>(
    SectionIds.CUSTOM_SECTION
) {
    @Throws(IOException::class)
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext,
        sectionSize: UInt
    ): CustomSection {
        // Read custom section name

        val nameSize = source.readVarUInt32()

        if (nameSize > Int.MAX_VALUE.toUInt()) {
            throw IOException("Unsupported length of custom section name")
        }

        val name = String(source.read(nameSize.toInt()))

        // Read content

        val contentSize = sectionSize - nameSize

        if (contentSize > Int.MAX_VALUE.toUInt()) {
            throw IOException("Unsupported length of custom section content")
        }

        val content = source.read(contentSize.toInt())

        // Instance construction

        return CustomSection(name, content)
    }
}
