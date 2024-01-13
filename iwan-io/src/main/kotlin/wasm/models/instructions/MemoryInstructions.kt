package dev.fir3.iwan.io.wasm.models.instructions

import dev.fir3.iwan.io.wasm.serialization.instructions.InstructionInfo
import dev.fir3.iwan.io.wasm.serialization.instructions.MemoryInstructionStrategy

interface MemoryInstruction : Instruction
interface AlignedMemoryInstruction : MemoryInstruction {
    val align: UInt
    val offset: UInt
}

@InstructionInfo(0x2Au, MemoryInstructionStrategy::class)
data class Float32Load(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x38u, MemoryInstructionStrategy::class)
data class Float32Store(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x2Bu, MemoryInstructionStrategy::class)
data class Float64Load(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x39u, MemoryInstructionStrategy::class)
data class Float64Store(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x28u, MemoryInstructionStrategy::class)
data class Int32Load(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x2Cu, MemoryInstructionStrategy::class)
data class Int32Load8S(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x2Du, MemoryInstructionStrategy::class)
data class Int32Load8U(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x2Eu, MemoryInstructionStrategy::class)
data class Int32Load16S(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x2Fu, MemoryInstructionStrategy::class)
data class Int32Load16U(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x36u, MemoryInstructionStrategy::class)
data class Int32Store(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x3Au, MemoryInstructionStrategy::class)
data class Int32Store8(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x3Bu, MemoryInstructionStrategy::class)
data class Int32Store16(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x29u, MemoryInstructionStrategy::class)
data class Int64Load(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x30u, MemoryInstructionStrategy::class)
data class Int64Load8S(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x31u, MemoryInstructionStrategy::class)
data class Int64Load8U(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x32u, MemoryInstructionStrategy::class)
data class Int64Load16S(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x33u, MemoryInstructionStrategy::class)
data class Int64Load16U(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x34u, MemoryInstructionStrategy::class)
data class Int64Load32S(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x35u, MemoryInstructionStrategy::class)
data class Int64Load32U(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x37u, MemoryInstructionStrategy::class)
data class Int64Store(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x3Cu, MemoryInstructionStrategy::class)
data class Int64Store8(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x3Du, MemoryInstructionStrategy::class)
data class Int64Store16(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x3Eu, MemoryInstructionStrategy::class)
data class Int64Store32(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

@InstructionInfo(0x40u, MemoryInstructionStrategy::class)
object MemoryGrow : MemoryInstruction

@InstructionInfo(0x3Fu, MemoryInstructionStrategy::class)
object MemorySize : MemoryInstruction
