package dev.fir3.iwan.io.wasm.serialization.instructions

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.DeserializationStrategy
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readUInt8
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.instructions.*
import java.io.IOException

internal object MemoryInstructionStrategy :
    DeserializationStrategy<MemoryInstruction> {
    private inline fun <TInstruction : MemoryInstruction> build(
        source: ByteSource,
        factory: (align: UInt, offset: UInt) -> TInstruction
    ): TInstruction {
        val align = source.readVarUInt32()
        val offset = source.readVarUInt32()
        return factory(align, offset)
    }

    @Throws(IOException::class)
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext
    ) = when (val instrId = source.readUInt8()) {
        InstructionIds.FLOAT32_LOAD -> build(source, ::Float32Load)
        InstructionIds.FLOAT32_STORE -> build(source, ::Float32Store)
        InstructionIds.FLOAT64_LOAD -> build(source, ::Float64Load)
        InstructionIds.FLOAT64_STORE -> build(source, ::Float64Store)
        InstructionIds.INT32_LOAD -> build(source, ::Int32Load)
        InstructionIds.INT32_LOAD8_S -> build(source, ::Int32Load8S)
        InstructionIds.INT32_LOAD8_U -> build(source, ::Int32Load8U)
        InstructionIds.INT32_LOAD16_S -> build(source, ::Int32Load16S)
        InstructionIds.INT32_LOAD16_U -> build(source, ::Int32Load16U)
        InstructionIds.INT32_STORE -> build(source, ::Int32Store)
        InstructionIds.INT32_STORE8 -> build(source, ::Int32Store8)
        InstructionIds.INT32_STORE16 -> build(source, ::Int32Store16)
        InstructionIds.INT64_LOAD -> build(source, ::Int64Load)
        InstructionIds.INT64_LOAD8_S -> build(source, ::Int64Load8S)
        InstructionIds.INT64_LOAD8_U -> build(source, ::Int64Load8U)
        InstructionIds.INT64_LOAD16_S -> build(source, ::Int64Load16S)
        InstructionIds.INT64_LOAD16_U -> build(source, ::Int64Load16U)
        InstructionIds.INT64_LOAD32_S -> build(source, ::Int64Load32S)
        InstructionIds.INT64_LOAD32_U -> build(source, ::Int64Load32U)
        InstructionIds.INT64_STORE -> build(source, ::Int64Store)
        InstructionIds.INT64_STORE8 -> build(source, ::Int64Store8)
        InstructionIds.INT64_STORE16 -> build(source, ::Int64Store16)
        InstructionIds.INT64_STORE32 -> build(source, ::Int64Store32)
        InstructionIds.MEMORY_GROW -> MemoryGrow
        InstructionIds.MEMORY_SIZE -> MemorySize
        else -> throw IOException("Invalid memory instruction '$instrId'")
    }
}
