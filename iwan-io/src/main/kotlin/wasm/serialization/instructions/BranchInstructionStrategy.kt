package dev.fir3.iwan.io.wasm.serialization.instructions

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.DeserializationStrategy
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readUInt8
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.instructions.BranchInstruction
import dev.fir3.iwan.io.wasm.models.instructions.ConditionalBranch
import dev.fir3.iwan.io.wasm.models.instructions.TableBranch
import dev.fir3.iwan.io.wasm.models.instructions.UnconditionalBranch
import java.io.IOException

internal object BranchInstructionStrategy :
    DeserializationStrategy<BranchInstruction> {
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext
    ) = when (val instrId = source.readUInt8()) {
        InstructionIds.BRANCH -> UnconditionalBranch(source.readVarUInt32())
        InstructionIds.BRANCH_IF -> ConditionalBranch(source.readVarUInt32())
        InstructionIds.BRANCH_TABLE -> {
            val indicesCount = source.readVarUInt32()
            val indices = mutableListOf<UInt>()

            for (i in 0u until indicesCount) {
                indices.add(source.readVarUInt32())
            }

            val tableIndex = source.readVarUInt32()
            TableBranch(indices, tableIndex)
        }
        else -> throw IOException("Invalid branch instruction '$instrId'")
    }
}
