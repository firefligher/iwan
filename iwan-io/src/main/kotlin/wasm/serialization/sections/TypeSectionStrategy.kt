package dev.fir3.iwan.io.wasm.serialization.sections

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.deserialize
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.FunctionType
import dev.fir3.iwan.io.wasm.models.sections.TypeSection
import java.io.IOException

internal object TypeSectionStrategy : BaseSectionStrategy<TypeSection>(
    SectionIds.TYPE_SECTION
) {
    @Throws(IOException::class)
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext,
        sectionSize: UInt
    ): TypeSection {
        val typeCount = source.readVarUInt32()
        val types = mutableListOf<FunctionType>()

        for (i in 0u until typeCount) {
            types.add(context.deserialize(source))
        }

        return TypeSection(types)
    }
}
