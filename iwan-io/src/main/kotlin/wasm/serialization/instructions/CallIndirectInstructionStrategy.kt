package dev.fir3.iwan.io.wasm.serialization.instructions

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.instructions.CallIndirectInstruction
import java.io.IOException

internal object CallIndirectInstructionStrategy :
    BaseInstructionStrategy<CallIndirectInstruction>(
        InstructionIds.CALL_INDIRECT
    ) {
    @Throws(IOException::class)
    override fun deserialize0(
        source: ByteSource,
        context: DeserializationContext
    ): CallIndirectInstruction {
        val typeIndex = source.readVarUInt32()
        val tableIndex = source.readVarUInt32()

        return CallIndirectInstruction(typeIndex, tableIndex)
    }
}