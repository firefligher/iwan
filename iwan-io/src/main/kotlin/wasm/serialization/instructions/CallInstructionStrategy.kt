package dev.fir3.iwan.io.wasm.serialization.instructions

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.instructions.CallInstruction

internal object CallInstructionStrategy :
    BaseInstructionStrategy<CallInstruction>(InstructionIds.CALL) {
    override fun deserialize0(
        source: ByteSource,
        context: DeserializationContext
    ) = CallInstruction(source.readVarUInt32())
}
