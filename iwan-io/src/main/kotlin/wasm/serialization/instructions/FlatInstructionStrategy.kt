package dev.fir3.iwan.io.wasm.serialization.instructions

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.DeserializationStrategy
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.readUInt8
import dev.fir3.iwan.io.wasm.models.instructions.FlatInstruction
import java.io.IOException

internal object FlatInstructionStrategy :
    DeserializationStrategy<FlatInstruction> {
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext
    ) = when (val instrId = source.readUInt8()) {
        InstructionIds.DROP -> FlatInstruction.DROP
        InstructionIds.FLOAT32_ABS -> FlatInstruction.FLOAT32_ABS
        InstructionIds.FLOAT32_ADD -> FlatInstruction.FLOAT32_ADD
        InstructionIds.FLOAT32_CEIL -> FlatInstruction.FLOAT32_CEIL
        InstructionIds.FLOAT32_CONVERT_INT32_S ->
            FlatInstruction.FLOAT32_CONVERT_INT32_S

        InstructionIds.FLOAT32_CONVERT_INT32_U ->
            FlatInstruction.FLOAT32_CONVERT_INT32_U

        InstructionIds.FLOAT32_CONVERT_INT64_S ->
            FlatInstruction.FLOAT32_CONVERT_INT64_S

        InstructionIds.FLOAT32_CONVERT_INT64_U ->
            FlatInstruction.FLOAT32_CONVERT_INT64_U

        InstructionIds.FLOAT32_COPYSIGN -> FlatInstruction.FLOAT32_COPYSIGN
        InstructionIds.FLOAT32_DEMOTE_FLOAT64 ->
            FlatInstruction.FLOAT32_DEMOTE_FLOAT64

        InstructionIds.FLOAT32_DIV -> FlatInstruction.FLOAT32_DIV
        InstructionIds.FLOAT32_EQ -> FlatInstruction.FLOAT32_EQ
        InstructionIds.FLOAT32_FLOOR -> FlatInstruction.FLOAT32_FLOOR
        InstructionIds.FLOAT32_GE -> FlatInstruction.FLOAT32_GE
        InstructionIds.FLOAT32_GT -> FlatInstruction.FLOAT32_GT
        InstructionIds.FLOAT32_LE -> FlatInstruction.FLOAT32_LE
        InstructionIds.FLOAT32_LT -> FlatInstruction.FLOAT32_LT
        InstructionIds.FLOAT32_MAX -> FlatInstruction.FLOAT32_MAX
        InstructionIds.FLOAT32_MIN -> FlatInstruction.FLOAT32_MIN
        InstructionIds.FLOAT32_MUL -> FlatInstruction.FLOAT32_MUL
        InstructionIds.FLOAT32_NE -> FlatInstruction.FLOAT32_NE
        InstructionIds.FLOAT32_NEAREST -> FlatInstruction.FLOAT32_NEAREST
        InstructionIds.FLOAT32_NEG -> FlatInstruction.FLOAT32_NEG
        InstructionIds.FLOAT32_REINTERPRET_INT32 ->
            FlatInstruction.FLOAT32_REINTERPRET_INT32

        InstructionIds.FLOAT32_SQRT -> FlatInstruction.FLOAT32_SQRT
        InstructionIds.FLOAT32_SUB -> FlatInstruction.FLOAT32_SUB
        InstructionIds.FLOAT32_TRUNC -> FlatInstruction.FLOAT32_TRUNC
        InstructionIds.FLOAT64_ABS -> FlatInstruction.FLOAT64_ABS
        InstructionIds.FLOAT64_ADD -> FlatInstruction.FLOAT64_ADD
        InstructionIds.FLOAT64_CEIL -> FlatInstruction.FLOAT64_CEIL
        InstructionIds.FLOAT64_CONVERT_INT32_S ->
            FlatInstruction.FLOAT64_CONVERT_INT32_S

        InstructionIds.FLOAT64_CONVERT_INT32_U ->
            FlatInstruction.FLOAT64_CONVERT_INT32_U

        InstructionIds.FLOAT64_CONVERT_INT64_S ->
            FlatInstruction.FLOAT64_CONVERT_INT64_S

        InstructionIds.FLOAT64_CONVERT_INT64_U ->
            FlatInstruction.FLOAT64_CONVERT_INT64_U

        InstructionIds.FLOAT64_COPYSIGN -> FlatInstruction.FLOAT64_COPYSIGN
        InstructionIds.FLOAT64_DIV -> FlatInstruction.FLOAT64_DIV
        InstructionIds.FLOAT64_EQ -> FlatInstruction.FLOAT64_EQ
        InstructionIds.FLOAT64_FLOOR -> FlatInstruction.FLOAT64_FLOOR
        InstructionIds.FLOAT64_GE -> FlatInstruction.FLOAT64_GE
        InstructionIds.FLOAT64_GT -> FlatInstruction.FLOAT64_GT
        InstructionIds.FLOAT64_LE -> FlatInstruction.FLOAT64_LE
        InstructionIds.FLOAT64_LT -> FlatInstruction.FLOAT64_LT
        InstructionIds.FLOAT64_MAX -> FlatInstruction.FLOAT64_MAX
        InstructionIds.FLOAT64_MIN -> FlatInstruction.FLOAT64_MIN
        InstructionIds.FLOAT64_MUL -> FlatInstruction.FLOAT64_MUL
        InstructionIds.FLOAT64_NE -> FlatInstruction.FLOAT64_NE
        InstructionIds.FLOAT64_NEAREST -> FlatInstruction.FLOAT64_NEAREST
        InstructionIds.FLOAT64_NEG -> FlatInstruction.FLOAT64_NEG
        InstructionIds.FLOAT64_PROMOTE_FLOAT32 ->
            FlatInstruction.FLOAT64_PROMOTE_FLOAT32

        InstructionIds.FLOAT64_REINTERPRET_INT64 ->
            FlatInstruction.FLOAT64_REINTERPRET_INT64

        InstructionIds.FLOAT64_SQRT -> FlatInstruction.FLOAT64_SQRT
        InstructionIds.FLOAT64_SUB -> FlatInstruction.FLOAT64_SUB
        InstructionIds.FLOAT64_TRUNC -> FlatInstruction.FLOAT64_TRUNC
        InstructionIds.INT32_ADD -> FlatInstruction.INT32_ADD
        InstructionIds.INT32_AND -> FlatInstruction.INT32_AND
        InstructionIds.INT32_CLZ -> FlatInstruction.INT32_CLZ
        InstructionIds.INT32_CTZ -> FlatInstruction.INT32_CTZ
        InstructionIds.INT32_DIV_S -> FlatInstruction.INT32_DIV_S
        InstructionIds.INT32_DIV_U -> FlatInstruction.INT32_DIV_U
        InstructionIds.INT32_EQ -> FlatInstruction.INT32_EQ
        InstructionIds.INT32_EQZ -> FlatInstruction.INT32_EQZ
        InstructionIds.INT32_EXTEND8_S -> FlatInstruction.INT32_EXTEND8_S
        InstructionIds.INT32_EXTEND16_S -> FlatInstruction.INT32_EXTEND16_S
        InstructionIds.INT32_GE_S -> FlatInstruction.INT32_GE_S
        InstructionIds.INT32_GE_U -> FlatInstruction.INT32_GE_U
        InstructionIds.INT32_GT_S -> FlatInstruction.INT32_GT_S
        InstructionIds.INT32_GT_U -> FlatInstruction.INT32_GT_U
        InstructionIds.INT32_LE_S -> FlatInstruction.INT32_LE_S
        InstructionIds.INT32_LE_U -> FlatInstruction.INT32_LE_U
        InstructionIds.INT32_LT_S -> FlatInstruction.INT32_LT_S
        InstructionIds.INT32_LT_U -> FlatInstruction.INT32_LT_U
        InstructionIds.INT32_MUL -> FlatInstruction.INT32_MUL
        InstructionIds.INT32_NE -> FlatInstruction.INT32_NE
        InstructionIds.INT32_OR -> FlatInstruction.INT32_OR
        InstructionIds.INT32_POPCNT -> FlatInstruction.INT32_POPCNT
        InstructionIds.INT32_REINTERPRET_FLOAT32 ->
            FlatInstruction.INT32_REINTERPRET_FLOAT32

        InstructionIds.INT32_REM_S -> FlatInstruction.INT32_REM_S
        InstructionIds.INT32_REM_U -> FlatInstruction.INT32_REM_U
        InstructionIds.INT32_ROTL -> FlatInstruction.INT32_ROTL
        InstructionIds.INT32_ROTR -> FlatInstruction.INT32_ROTR
        InstructionIds.INT32_SHL -> FlatInstruction.INT32_SHL
        InstructionIds.INT32_SHR_S -> FlatInstruction.INT32_SHR_S
        InstructionIds.INT32_SHR_U -> FlatInstruction.INT32_SHR_U
        InstructionIds.INT32_SUB -> FlatInstruction.INT32_SUB
        InstructionIds.INT32_TRUNC_FLOAT32_S ->
            FlatInstruction.INT32_TRUNC_FLOAT32_S

        InstructionIds.INT32_TRUNC_FLOAT32_U ->
            FlatInstruction.INT32_TRUNC_FLOAT32_U

        InstructionIds.INT32_TRUNC_FLOAT64_S ->
            FlatInstruction.INT32_TRUNC_FLOAT64_S

        InstructionIds.INT32_TRUNC_FLOAT64_U ->
            FlatInstruction.INT32_TRUNC_FLOAT64_U

        InstructionIds.INT32_WRAP_INT64 -> FlatInstruction.INT32_WRAP_INT64
        InstructionIds.INT32_XOR -> FlatInstruction.INT32_XOR
        InstructionIds.INT64_ADD -> FlatInstruction.INT64_ADD
        InstructionIds.INT64_AND -> FlatInstruction.INT64_AND
        InstructionIds.INT64_CLZ -> FlatInstruction.INT64_CLZ
        InstructionIds.INT64_CTZ -> FlatInstruction.INT64_CTZ
        InstructionIds.INT64_DIV_S -> FlatInstruction.INT64_DIV_S
        InstructionIds.INT64_DIV_U -> FlatInstruction.INT64_DIV_U
        InstructionIds.INT64_EQ -> FlatInstruction.INT64_EQ
        InstructionIds.INT64_EQZ -> FlatInstruction.INT64_EQZ
        InstructionIds.INT64_EXTEND8_S -> FlatInstruction.INT64_EXTEND8_S
        InstructionIds.INT64_EXTEND16_S -> FlatInstruction.INT64_EXTEND16_S
        InstructionIds.INT64_EXTEND32_S -> FlatInstruction.INT64_EXTEND32_S
        InstructionIds.INT64_EXTEND_INT32_S ->
            FlatInstruction.INT64_EXTEND_INT32_S

        InstructionIds.INT64_EXTEND_INT32_U ->
            FlatInstruction.INT64_EXTEND_INT32_U

        InstructionIds.INT64_GE_S -> FlatInstruction.INT64_GE_S
        InstructionIds.INT64_GE_U -> FlatInstruction.INT64_GE_U
        InstructionIds.INT64_GT_S -> FlatInstruction.INT64_GT_S
        InstructionIds.INT64_GT_U -> FlatInstruction.INT64_GT_U
        InstructionIds.INT64_LE_S -> FlatInstruction.INT64_LE_S
        InstructionIds.INT64_LE_U -> FlatInstruction.INT64_LE_U
        InstructionIds.INT64_LT_S -> FlatInstruction.INT64_LT_S
        InstructionIds.INT64_LT_U -> FlatInstruction.INT64_LT_U
        InstructionIds.INT64_MUL -> FlatInstruction.INT64_MUL
        InstructionIds.INT64_NE -> FlatInstruction.INT64_NE
        InstructionIds.INT64_OR -> FlatInstruction.INT64_OR
        InstructionIds.INT64_POPCNT -> FlatInstruction.INT64_POPCNT
        InstructionIds.INT64_REINTERPRET_FLOAT64 ->
            FlatInstruction.INT64_REINTERPRET_FLOAT64

        InstructionIds.INT64_REM_S -> FlatInstruction.INT64_REM_S
        InstructionIds.INT64_REM_U -> FlatInstruction.INT64_REM_U
        InstructionIds.INT64_ROTL -> FlatInstruction.INT64_ROTL
        InstructionIds.INT64_ROTR -> FlatInstruction.INT64_ROTR
        InstructionIds.INT64_SHL -> FlatInstruction.INT64_SHL
        InstructionIds.INT64_SHR_S -> FlatInstruction.INT64_SHR_S
        InstructionIds.INT64_SHR_U -> FlatInstruction.INT64_SHR_U
        InstructionIds.INT64_SUB -> FlatInstruction.INT64_SUB
        InstructionIds.INT64_TRUNC_FLOAT32_S ->
            FlatInstruction.INT64_TRUNC_FLOAT32_S

        InstructionIds.INT64_TRUNC_FLOAT32_U ->
            FlatInstruction.INT64_TRUNC_FLOAT32_U

        InstructionIds.INT64_TRUNC_FLOAT64_S ->
            FlatInstruction.INT64_TRUNC_FLOAT64_S

        InstructionIds.INT64_TRUNC_FLOAT64_U ->
            FlatInstruction.INT64_TRUNC_FLOAT64_U

        InstructionIds.INT64_XOR -> FlatInstruction.INT64_XOR
        InstructionIds.NOP -> FlatInstruction.NOP
        InstructionIds.RETURN -> FlatInstruction.RETURN
        InstructionIds.SELECT -> FlatInstruction.SELECT
        InstructionIds.UNREACHABLE -> FlatInstruction.UNREACHABLE
        else -> throw IOException("Invalid flat instruction: '$instrId'")
    }
}
