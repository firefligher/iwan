package dev.fir3.iwan.io.wasm.serialization.instructions

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.DeserializationStrategy
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readUInt8
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.instructions.*
import java.io.IOException

internal object VariableInstructionStrategy :
    DeserializationStrategy<VariableInstruction> {
    @Throws(IOException::class)
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext
    ) = when (val instrId = source.readUInt8()) {
        InstructionIds.GLOBAL_GET ->
            GlobalGetInstruction(source.readVarUInt32())

        InstructionIds.GLOBAL_SET ->
            GlobalSetInstruction(source.readVarUInt32())

        InstructionIds.LOCAL_GET ->
            LocalGetInstruction(source.readVarUInt32())

        InstructionIds.LOCAL_SET ->
            LocalSetInstruction(source.readVarUInt32())

        InstructionIds.LOCAL_TEE ->
            LocalTeeInstruction(source.readVarUInt32())

        else -> throw IOException(
            "Invalid variable instruction with instrId '$instrId'"
        )
    }
}
