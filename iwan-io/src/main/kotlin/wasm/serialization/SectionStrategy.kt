package dev.fir3.iwan.io.wasm.serialization

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.DeserializationStrategy
import dev.fir3.iwan.io.serialization.deserializeVector
import dev.fir3.iwan.io.source.*
import dev.fir3.iwan.io.source.readInt8
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.sections.*
import java.io.IOException

internal object SectionStrategy : DeserializationStrategy<Section> {
    private const val CODE_SECTION: Byte = 0x0A
    private const val CUSTOM_SECTION: Byte = 0x00
    private const val DATA_COUNT_SECTION: Byte = 0x0C
    private const val DATA_SECTION: Byte = 0x0B
    private const val ELEMENT_SECTION: Byte = 0x09
    private const val EXPORT_SECTION: Byte = 0x07
    private const val FUNCTION_SECTION: Byte = 0x03
    private const val GLOBAL_SECTION: Byte = 0x06
    private const val IMPORT_SECTION: Byte = 0x02
    private const val MEMORY_SECTION: Byte = 0x05
    private const val START_SECTION: Byte = 0x08
    private const val TABLE_SECTION: Byte = 0x04
    private const val TYPE_SECTION: Byte = 0x01

    private inline fun <TSection : Section> build(
        source: ByteSource,
        context: DeserializationContext,
        factory: (ByteSource, DeserializationContext, UInt) -> TSection
    ) = factory(source, context, source.readVarUInt32())

    @Throws(IOException::class)
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext
    ): Section {
        val sectionId = source.readInt8()

        return when (sectionId) {
            CODE_SECTION -> build(source, context) { src, ctx, _ ->
                CodeSection(ctx.deserializeVector(src))
            }

            CUSTOM_SECTION -> build(source, context) { src, _, sectionSize ->
                // Read custom section name

                val nameSize = src.readVarUInt32()

                if (nameSize > Int.MAX_VALUE.toUInt()) {
                    throw IOException(
                        "Unsupported length of custom section name"
                    )
                }

                val name = String(src.read(nameSize.toInt()))

                // Read content

                val contentSize = sectionSize - nameSize

                if (contentSize > Int.MAX_VALUE.toUInt()) {
                    throw IOException(
                        "Unsupported length of custom section content"
                    )
                }

                val content = src.read(contentSize.toInt())

                // Instance construction

                CustomSection(name, content)
            }

            DATA_SECTION -> build(source, context) { src, ctx, _ ->
                DataSection(ctx.deserializeVector(src))
            }

            ELEMENT_SECTION -> build(source, context) { src, ctx, _ ->
                ElementSection(ctx.deserializeVector(src))
            }

            EXPORT_SECTION -> build(source, context ) { src, ctx, _ ->
                ExportSection(ctx.deserializeVector(src))
            }

            FUNCTION_SECTION -> build(source, context) { src, ctx, _ ->
                val typeCount = source.readVarUInt32()
                val types = mutableListOf<UInt>()

                for (i in 0u until typeCount) {
                    types.add(source.readVarUInt32())
                }

                FunctionSection(types)
            }

            GLOBAL_SECTION -> build(source, context) { src, ctx, _ ->
                GlobalSection(ctx.deserializeVector(src))
            }

            IMPORT_SECTION -> build(source, context) { src, ctx, _ ->
                ImportSection(ctx.deserializeVector(src))
            }

            MEMORY_SECTION -> build(source, context) { src, ctx, _ ->
                MemorySection(ctx.deserializeVector(src))
            }

            TABLE_SECTION -> build(source, context) { src, ctx, _ ->
                TableSection(ctx.deserializeVector(src))
            }

            TYPE_SECTION -> build(source, context) { src, ctx, _ ->
                TypeSection(ctx.deserializeVector(src))
            }

            else -> throw IOException("Unsupported section id '$sectionId'")
        }
    }
}
