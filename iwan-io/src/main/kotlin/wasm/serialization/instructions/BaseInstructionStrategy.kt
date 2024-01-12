package dev.fir3.iwan.io.wasm.serialization.instructions

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.DeserializationStrategy
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.expectUInt8
import dev.fir3.iwan.io.wasm.models.instructions.Instruction
import java.io.IOException

internal abstract class BaseInstructionStrategy<TInstruction : Instruction>(
    private val _instructionId: UByte
) : DeserializationStrategy<TInstruction> {
    @Throws(IOException::class)
    final override fun deserialize(
        source: ByteSource,
        context: DeserializationContext
    ): TInstruction {
        source.expectUInt8(_instructionId)
        return deserialize0(source, context)
    }

    protected abstract fun deserialize0(
        source: ByteSource,
        context: DeserializationContext
    ): TInstruction
}
