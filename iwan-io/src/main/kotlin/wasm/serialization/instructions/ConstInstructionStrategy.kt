package dev.fir3.iwan.io.wasm.serialization.instructions

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.DeserializationStrategy
import dev.fir3.iwan.io.source.*
import dev.fir3.iwan.io.source.readUInt8
import dev.fir3.iwan.io.source.readVarInt32
import dev.fir3.iwan.io.source.readVarInt64
import dev.fir3.iwan.io.wasm.models.instructions.*
import java.io.IOException

internal object ConstInstructionStrategy :
    DeserializationStrategy<ConstInstruction<*>> {
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext
    ) = when (val typeId = source.readUInt8()) {
        InstructionIds.FLOAT32_CONST ->
            Float32ConstInstruction(source.readFloat32())

        InstructionIds.FLOAT64_CONST ->
            Float64ConstInstruction(source.readFloat64())

        InstructionIds.INT32_CONST ->
            Int32ConstInstruction(source.readVarInt32())

        InstructionIds.INT64_CONST ->
            Int64ConstInstruction(source.readVarInt64())

        else -> throw IOException(
            "Unsupported numeric constant type '$typeId'"
        )
    }
}