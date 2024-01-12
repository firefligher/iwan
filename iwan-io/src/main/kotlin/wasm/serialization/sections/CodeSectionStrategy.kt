package dev.fir3.iwan.io.wasm.serialization.sections

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.deserialize
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.Code
import dev.fir3.iwan.io.wasm.models.sections.CodeSection
import java.io.IOException

internal object CodeSectionStrategy : BaseSectionStrategy<CodeSection>(
    SectionIds.CODE_SECTION
) {
    @Throws(IOException::class)
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext,
        sectionSize: UInt
    ): CodeSection {
        val codeCount = source.readVarUInt32()
        val codes = mutableListOf<Code>()

        for (i in 0u until codeCount) {
            codes.add(context.deserialize(source))
        }

        return CodeSection(codes)
    }
}