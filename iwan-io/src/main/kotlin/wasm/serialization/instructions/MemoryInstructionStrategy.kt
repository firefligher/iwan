package dev.fir3.iwan.io.wasm.serialization.instructions

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.DeserializationStrategy
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readUInt8
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.instructions.*
import java.io.IOException
import kotlin.reflect.KClass

internal object MemoryInstructionStrategy :
    InstructionDeserializationStrategy {
    private inline fun <TInstruction : MemoryInstruction> build(
        source: ByteSource,
        factory: (align: UInt, offset: UInt) -> TInstruction
    ): TInstruction {
        val align = source.readVarUInt32()
        val offset = source.readVarUInt32()
        return factory(align, offset)
    }

    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext,
        model: KClass<out Instruction>,
        instance: Instruction?
    ) = when (model) {
        Float32Load::class -> build(source, ::Float32Load)
        Float32Store::class -> build(source, ::Float32Store)
        Float64Load::class -> build(source, ::Float64Load)
        Float64Store::class -> build(source, ::Float64Store)
        Int32Load::class -> build(source, ::Int32Load)
        Int32Load8S::class -> build(source, ::Int32Load8S)
        Int32Load8U::class -> build(source, ::Int32Load8U)
        Int32Load16S::class -> build(source, ::Int32Load16S)
        Int32Load16U::class -> build(source, ::Int32Load16U)
        Int32Store::class -> build(source, ::Int32Store)
        Int32Store8::class -> build(source, ::Int32Store8)
        Int32Store16::class -> build(source, ::Int32Store16)
        Int64Load::class -> build(source, ::Int64Load)
        Int64Load8S::class -> build(source, ::Int64Load8S)
        Int64Load8U::class -> build(source, ::Int64Load8U)
        Int64Load16S::class -> build(source, ::Int64Load16S)
        Int64Load16U::class -> build(source, ::Int64Load16U)
        Int64Load32S::class -> build(source, ::Int64Load32S)
        Int64Load32U::class -> build(source, ::Int64Load32U)
        Int64Store::class -> build(source, ::Int64Store)
        Int64Store8::class -> build(source, ::Int64Store8)
        Int64Store16::class -> build(source, ::Int64Store16)
        Int64Store32::class -> build(source, ::Int64Store32)
        MemoryGrow::class -> MemoryGrow
        MemorySize::class -> MemorySize
        else -> throw IOException("Invalid memory instruction type: $model")
    }
}
