package dev.fir3.iwan.io.wasm.models.instructions

import dev.fir3.iwan.io.wasm.serialization.instructions.InstructionInfo
import dev.fir3.iwan.io.wasm.serialization.instructions.MemoryInstructionStrategy

interface MemoryInstruction : Instruction
interface AlignedMemoryInstruction : MemoryInstruction {
    val align: UInt
    val offset: UInt
}

data class DataDropInstruction(val dataIndex: UInt): MemoryInstruction {
    override val uniqueId: Int = UniqueIds.DATA_DROP
}

@InstructionInfo(0x2Au, MemoryInstructionStrategy::class)
data class Float32LoadInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = 0xFF
}

@InstructionInfo(0x38u, MemoryInstructionStrategy::class)
data class Float32StoreInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = UniqueIds.STORE_FLOAT32
}

@InstructionInfo(0x2Bu, MemoryInstructionStrategy::class)
data class Float64LoadInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = 0xFF
}

@InstructionInfo(0x39u, MemoryInstructionStrategy::class)
data class Float64StoreInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = UniqueIds.STORE_FLOAT64
}

@InstructionInfo(0x28u, MemoryInstructionStrategy::class)
data class Int32LoadInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = UniqueIds.INT32_LOAD
}

@InstructionInfo(0x2Cu, MemoryInstructionStrategy::class)
data class Int32Load8SInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = UniqueIds.INT32_LOAD_8S
}

@InstructionInfo(0x2Du, MemoryInstructionStrategy::class)
data class Int32Load8UInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = UniqueIds.INT32_LOAD_8U
}

@InstructionInfo(0x2Eu, MemoryInstructionStrategy::class)
data class Int32Load16SInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = 0xFF
}

@InstructionInfo(0x2Fu, MemoryInstructionStrategy::class)
data class Int32Load16UInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = 0xFF
}

@InstructionInfo(0x36u, MemoryInstructionStrategy::class)
data class Int32StoreInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = UniqueIds.STORE_INT32
}

@InstructionInfo(0x3Au, MemoryInstructionStrategy::class)
data class Int32Store8Instruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = UniqueIds.STORE_INT32_8
}

@InstructionInfo(0x3Bu, MemoryInstructionStrategy::class)
data class Int32Store16Instruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = UniqueIds.STORE_INT32_16
}

@InstructionInfo(0x29u, MemoryInstructionStrategy::class)
data class Int64LoadInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = UniqueIds.INT64_LOAD
}

@InstructionInfo(0x30u, MemoryInstructionStrategy::class)
data class Int64Load8SInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = 0xFF
}

@InstructionInfo(0x31u, MemoryInstructionStrategy::class)
data class Int64Load8UInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = 0xFF
}

@InstructionInfo(0x32u, MemoryInstructionStrategy::class)
data class Int64Load16SInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = 0xFF
}

@InstructionInfo(0x33u, MemoryInstructionStrategy::class)
data class Int64Load16UInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = 0xFF
}

@InstructionInfo(0x34u, MemoryInstructionStrategy::class)
data class Int64Load32SInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = 0xFF
}

@InstructionInfo(0x35u, MemoryInstructionStrategy::class)
data class Int64Load32UInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = UniqueIds.INT64_LOAD_32U
}

@InstructionInfo(0x37u, MemoryInstructionStrategy::class)
data class Int64StoreInstruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = UniqueIds.STORE_INT64
}

@InstructionInfo(0x3Cu, MemoryInstructionStrategy::class)
data class Int64Store8Instruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = UniqueIds.STORE_INT64_8
}

@InstructionInfo(0x3Du, MemoryInstructionStrategy::class)
data class Int64Store16Instruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = UniqueIds.STORE_INT64_16
}

@InstructionInfo(0x3Eu, MemoryInstructionStrategy::class)
data class Int64Store32Instruction(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction {
    override val uniqueId: Int = UniqueIds.STORE_INT64_32
}

@InstructionInfo(0x40u, MemoryInstructionStrategy::class)
object MemoryGrowInstruction : MemoryInstruction {
    override val uniqueId: Int = 0xFF
}

data class MemoryInitInstruction(
    val dataIndex: UInt
) : MemoryInstruction {
    override val uniqueId: Int = UniqueIds.MEMORY_INIT
}

@InstructionInfo(0x3Fu, MemoryInstructionStrategy::class)
object MemorySizeInstruction : MemoryInstruction {
    override val uniqueId: Int = 0xFF
}
