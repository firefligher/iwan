package dev.fir3.iwan.io.wasm.serialization.sections

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.deserialize
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.Global
import dev.fir3.iwan.io.wasm.models.sections.GlobalSection
import java.io.IOException

internal object GlobalSectionStrategy : BaseSectionStrategy<GlobalSection>(
    SectionIds.GLOBAL_SECTION
) {
    @Throws(IOException::class)
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext,
        sectionSize: UInt
    ): GlobalSection {
        val globalsCount = source.readVarUInt32()
        val globals = mutableListOf<Global>()

        for (i in 0u until globalsCount) {
            globals.add(context.deserialize(source))
        }

        return GlobalSection(globals)
    }
}