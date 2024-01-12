package dev.fir3.iwan.io.wasm.models.instructions

interface MemoryInstruction : Instruction
interface AlignedMemoryInstruction : MemoryInstruction {
    val align: UInt
    val offset: UInt
}

data class Float32Load(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Float32Store(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Float64Load(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Float64Store(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int32Load(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int32Load8S(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int32Load8U(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int32Load16S(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int32Load16U(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int32Store(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int32Store8(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int32Store16(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int64Load(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int64Load8S(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int64Load8U(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int64Load16S(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int64Load16U(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int64Load32S(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int64Load32U(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int64Store(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int64Store8(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int64Store16(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

data class Int64Store32(
    override val align: UInt,
    override val offset: UInt
) : AlignedMemoryInstruction

object MemoryGrow : MemoryInstruction
object MemorySize : MemoryInstruction
