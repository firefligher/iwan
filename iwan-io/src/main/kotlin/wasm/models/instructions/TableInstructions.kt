package dev.fir3.iwan.io.wasm.models.instructions

interface TableInstruction : Instruction

data class ElementDropInstruction(val elementIndex: UInt): TableInstruction {
    override val uniqueId: Int = UniqueIds.ELEMENT_DROP
}

data class TableCopyInstruction(
    val tableIndex1: UInt,
    val tableIndex2: UInt
): TableInstruction {
    override val uniqueId: Int = 0xFC14
}


data class TableFillInstruction(val tableIndex: UInt): TableInstruction {
    override val uniqueId: Int = 0xFC17
}

data class TableGetInstruction(val tableIndex: UInt): TableInstruction {
    override val uniqueId: Int = 0x25
}

data class TableGrowInstruction(val tableIndex: UInt): TableInstruction {
    override val uniqueId: Int = 0xFC15
}

data class TableInitInstruction(
    val tableIndex: UInt,
    val elementIndex: UInt
): TableInstruction {
    override val uniqueId: Int = UniqueIds.TABLE_INIT
}

data class TableSetInstruction(val tableIndex: UInt): TableInstruction {
    override val uniqueId: Int = UniqueIds.TABLE_SET
}

data class TableSizeInstruction(val tableIndex: UInt): TableInstruction {
    override val uniqueId: Int = 0xFC16
}

