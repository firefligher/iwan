package dev.fir3.iwan.io.wasm.serialization.sections

internal object SectionIds {
    const val CODE_SECTION: Byte = 0x0A
    const val CUSTOM_SECTION: Byte = 0x00
    const val DATA_COUNT_SECTION: Byte = 0x0C
    const val DATA_SECTION: Byte = 0x0B
    const val ELEMENT_SECTION: Byte = 0x09
    const val EXPORT_SECTION: Byte = 0x07
    const val FUNCTION_SECTION: Byte = 0x03
    const val GLOBAL_SECTION: Byte = 0x06
    const val IMPORT_SECTION: Byte = 0x02
    const val MEMORY_SECTION: Byte = 0x05
    const val START_SECTION: Byte = 0x08
    const val TABLE_SECTION: Byte = 0x04
    const val TYPE_SECTION: Byte = 0x01
}
