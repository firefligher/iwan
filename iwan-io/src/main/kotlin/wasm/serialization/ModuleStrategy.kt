package dev.fir3.iwan.io.wasm.serialization

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.DeserializationStrategy
import dev.fir3.iwan.io.serialization.deserialize
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.expect
import dev.fir3.iwan.io.wasm.models.Module
import dev.fir3.iwan.io.wasm.models.sections.Section
import java.io.IOException

internal object ModuleStrategy : DeserializationStrategy<Module> {
    @Throws(IOException::class)
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext
    ): Module {
        source.expect(
            0x00, 0x61, 0x73, 0x6D, // File Magic
            0x01, 0x00, 0x00, 0x00  // Version
        )

        val sections = mutableListOf<Section>()

        while (!source.isEof()) {
            sections.add(context.deserialize(source))
        }

        return Module(sections)
    }
}
