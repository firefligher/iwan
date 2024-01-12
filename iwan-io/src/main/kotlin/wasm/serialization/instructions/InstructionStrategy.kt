package dev.fir3.iwan.io.wasm.serialization.instructions

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.DeserializationStrategy
import dev.fir3.iwan.io.serialization.deserialize
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.peekUInt8
import dev.fir3.iwan.io.wasm.models.instructions.*
import java.io.IOException

internal object InstructionStrategy : DeserializationStrategy<Instruction> {
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext
    ): Instruction = when (val typeId = source.peekUInt8()) {
        InstructionIds.BLOCK,
        InstructionIds.IF,
        InstructionIds.LOOP -> context.deserialize<BlockTypeInstruction>(source)

        InstructionIds.BRANCH,
        InstructionIds.BRANCH_IF,
        InstructionIds.BRANCH_TABLE -> context
            .deserialize<BranchInstruction>(source)

        InstructionIds.CALL -> context.deserialize<CallInstruction>(source)
        InstructionIds.CALL_INDIRECT -> context
            .deserialize<CallIndirectInstruction>(source)

        InstructionIds.DROP,
        InstructionIds.FLOAT32_ABS,
        InstructionIds.FLOAT32_ADD,
        InstructionIds.FLOAT32_CEIL,
        InstructionIds.FLOAT32_CONVERT_INT32_S,
        InstructionIds.FLOAT32_CONVERT_INT32_U,
        InstructionIds.FLOAT32_CONVERT_INT64_S,
        InstructionIds.FLOAT32_CONVERT_INT64_U,
        InstructionIds.FLOAT32_COPYSIGN,
        InstructionIds.FLOAT32_DEMOTE_FLOAT64,
        InstructionIds.FLOAT32_DIV,
        InstructionIds.FLOAT32_EQ,
        InstructionIds.FLOAT32_FLOOR,
        InstructionIds.FLOAT32_GE,
        InstructionIds.FLOAT32_GT,
        InstructionIds.FLOAT32_LE,
        InstructionIds.FLOAT32_LT,
        InstructionIds.FLOAT32_MAX,
        InstructionIds.FLOAT32_MIN,
        InstructionIds.FLOAT32_MUL,
        InstructionIds.FLOAT32_NE,
        InstructionIds.FLOAT32_NEAREST,
        InstructionIds.FLOAT32_NEG,
        InstructionIds.FLOAT32_REINTERPRET_INT32,
        InstructionIds.FLOAT32_SQRT,
        InstructionIds.FLOAT32_SUB,
        InstructionIds.FLOAT32_TRUNC,
        InstructionIds.FLOAT64_ABS,
        InstructionIds.FLOAT64_ADD,
        InstructionIds.FLOAT64_CEIL,
        InstructionIds.FLOAT64_CONVERT_INT32_S,
        InstructionIds.FLOAT64_CONVERT_INT32_U,
        InstructionIds.FLOAT64_CONVERT_INT64_S,
        InstructionIds.FLOAT64_CONVERT_INT64_U,
        InstructionIds.FLOAT64_COPYSIGN,
        InstructionIds.FLOAT64_DIV,
        InstructionIds.FLOAT64_EQ,
        InstructionIds.FLOAT64_FLOOR,
        InstructionIds.FLOAT64_GE,
        InstructionIds.FLOAT64_GT,
        InstructionIds.FLOAT64_LE,
        InstructionIds.FLOAT64_LT,
        InstructionIds.FLOAT64_MAX,
        InstructionIds.FLOAT64_MIN,
        InstructionIds.FLOAT64_MUL,
        InstructionIds.FLOAT64_NE,
        InstructionIds.FLOAT64_NEAREST,
        InstructionIds.FLOAT64_NEG,
        InstructionIds.FLOAT64_PROMOTE_FLOAT32,
        InstructionIds.FLOAT64_REINTERPRET_INT64,
        InstructionIds.FLOAT64_SQRT,
        InstructionIds.FLOAT64_SUB,
        InstructionIds.FLOAT64_TRUNC,
        InstructionIds.INT32_ADD,
        InstructionIds.INT32_AND,
        InstructionIds.INT32_CLZ,
        InstructionIds.INT32_CTZ,
        InstructionIds.INT32_DIV_S,
        InstructionIds.INT32_DIV_U,
        InstructionIds.INT32_EQ,
        InstructionIds.INT32_EQZ,
        InstructionIds.INT32_EXTEND8_S,
        InstructionIds.INT32_EXTEND16_S,
        InstructionIds.INT32_GE_S,
        InstructionIds.INT32_GE_U,
        InstructionIds.INT32_GT_S,
        InstructionIds.INT32_GT_U,
        InstructionIds.INT32_LE_S,
        InstructionIds.INT32_LE_U,
        InstructionIds.INT32_LT_S,
        InstructionIds.INT32_LT_U,
        InstructionIds.INT32_MUL,
        InstructionIds.INT32_NE,
        InstructionIds.INT32_OR,
        InstructionIds.INT32_REINTERPRET_FLOAT32,
        InstructionIds.INT32_POPCNT,
        InstructionIds.INT32_REM_S,
        InstructionIds.INT32_REM_U,
        InstructionIds.INT32_ROTL,
        InstructionIds.INT32_ROTR,
        InstructionIds.INT32_SHL,
        InstructionIds.INT32_SHR_S,
        InstructionIds.INT32_SHR_U,
        InstructionIds.INT32_SUB,
        InstructionIds.INT32_TRUNC_FLOAT32_S,
        InstructionIds.INT32_TRUNC_FLOAT32_U,
        InstructionIds.INT32_TRUNC_FLOAT64_S,
        InstructionIds.INT32_TRUNC_FLOAT64_U,
        InstructionIds.INT32_WRAP_INT64,
        InstructionIds.INT32_XOR,
        InstructionIds.INT64_ADD,
        InstructionIds.INT64_AND,
        InstructionIds.INT64_CLZ,
        InstructionIds.INT64_CTZ,
        InstructionIds.INT64_DIV_S,
        InstructionIds.INT64_DIV_U,
        InstructionIds.INT64_EQ,
        InstructionIds.INT64_EQZ,
        InstructionIds.INT64_EXTEND8_S,
        InstructionIds.INT64_EXTEND16_S,
        InstructionIds.INT64_EXTEND32_S,
        InstructionIds.INT64_EXTEND_INT32_S,
        InstructionIds.INT64_EXTEND_INT32_U,
        InstructionIds.INT64_GE_S,
        InstructionIds.INT64_GE_U,
        InstructionIds.INT64_GT_S,
        InstructionIds.INT64_GT_U,
        InstructionIds.INT64_LE_S,
        InstructionIds.INT64_LE_U,
        InstructionIds.INT64_LT_S,
        InstructionIds.INT64_LT_U,
        InstructionIds.INT64_MUL,
        InstructionIds.INT64_NE,
        InstructionIds.INT64_OR,
        InstructionIds.INT64_POPCNT,
        InstructionIds.INT64_REINTERPRET_FLOAT64,
        InstructionIds.INT64_REM_S,
        InstructionIds.INT64_REM_U,
        InstructionIds.INT64_ROTL,
        InstructionIds.INT64_ROTR,
        InstructionIds.INT64_SHL,
        InstructionIds.INT64_SHR_S,
        InstructionIds.INT64_SHR_U,
        InstructionIds.INT64_SUB,
        InstructionIds.INT64_TRUNC_FLOAT32_S,
        InstructionIds.INT64_TRUNC_FLOAT32_U,
        InstructionIds.INT64_TRUNC_FLOAT64_S,
        InstructionIds.INT64_TRUNC_FLOAT64_U,
        InstructionIds.INT64_XOR,
        InstructionIds.NOP,
        InstructionIds.RETURN,
        InstructionIds.SELECT,
        InstructionIds.UNREACHABLE ->
            context.deserialize<FlatInstruction>(source)

        InstructionIds.FLOAT32_CONST,
        InstructionIds.FLOAT64_CONST,
        InstructionIds.INT32_CONST,
        InstructionIds.INT64_CONST -> context
            .deserialize<ConstInstruction<*>>(source)

        InstructionIds.FLOAT32_LOAD,
        InstructionIds.FLOAT32_STORE,
        InstructionIds.FLOAT64_LOAD,
        InstructionIds.FLOAT64_STORE,
        InstructionIds.INT32_LOAD,
        InstructionIds.INT32_LOAD8_S,
        InstructionIds.INT32_LOAD8_U,
        InstructionIds.INT32_LOAD16_S,
        InstructionIds.INT32_LOAD16_U,
        InstructionIds.INT32_STORE,
        InstructionIds.INT32_STORE8,
        InstructionIds.INT32_STORE16,
        InstructionIds.INT64_LOAD,
        InstructionIds.INT64_LOAD8_S,
        InstructionIds.INT64_LOAD8_U,
        InstructionIds.INT64_LOAD16_S,
        InstructionIds.INT64_LOAD16_U,
        InstructionIds.INT64_LOAD32_S,
        InstructionIds.INT64_LOAD32_U,
        InstructionIds.INT64_STORE,
        InstructionIds.INT64_STORE8,
        InstructionIds.INT64_STORE16,
        InstructionIds.INT64_STORE32,
        InstructionIds.MEMORY_GROW,
        InstructionIds.MEMORY_SIZE -> context
            .deserialize<MemoryInstruction>(source)

        InstructionIds.GLOBAL_GET,
        InstructionIds.GLOBAL_SET,
        InstructionIds.LOCAL_GET,
        InstructionIds.LOCAL_SET,
        InstructionIds.LOCAL_TEE -> context
            .deserialize<VariableInstruction>(source)

        else -> throw IOException("Unsupported instruction '$typeId'")
    }
}