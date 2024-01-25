package dev.fir3.iwan.io.wasm.models.instructions

import dev.fir3.iwan.io.wasm.serialization.instructions.FlatInstructionStrategy
import dev.fir3.iwan.io.wasm.serialization.instructions.InstructionInfo

enum class FlatInstruction(override val uniqueId: Int) : Instruction {
    @InstructionInfo(0x1Au, FlatInstructionStrategy::class)
    DROP(UniqueIds.DROP),

    @InstructionInfo(0x8Bu, FlatInstructionStrategy::class)
    FLOAT32_ABS(0xFF),

    @InstructionInfo(0x92u, FlatInstructionStrategy::class)
    FLOAT32_ADD(0xFF),

    @InstructionInfo(0x8Du, FlatInstructionStrategy::class)
    FLOAT32_CEIL(0xFF),

    @InstructionInfo(0xB2u, FlatInstructionStrategy::class)
    FLOAT32_CONVERT_INT32_S(0xFF),

    @InstructionInfo(0xB3u, FlatInstructionStrategy::class)
    FLOAT32_CONVERT_INT32_U(0xFF),

    @InstructionInfo(0xB4u, FlatInstructionStrategy::class)
    FLOAT32_CONVERT_INT64_S(0xFF),

    @InstructionInfo(0xB5u, FlatInstructionStrategy::class)
    FLOAT32_CONVERT_INT64_U(0xFF),

    @InstructionInfo(0x98u, FlatInstructionStrategy::class)
    FLOAT32_COPYSIGN(0xFF),

    @InstructionInfo(0xB6u, FlatInstructionStrategy::class)
    FLOAT32_DEMOTE_FLOAT64(0xFF),

    @InstructionInfo(0x95u, FlatInstructionStrategy::class)
    FLOAT32_DIV(0xFF),

    @InstructionInfo(0x5Bu, FlatInstructionStrategy::class)
    FLOAT32_EQ(0xFF),

    @InstructionInfo(0x8Eu, FlatInstructionStrategy::class)
    FLOAT32_FLOOR(0xFF),

    @InstructionInfo(0x60u, FlatInstructionStrategy::class)
    FLOAT32_GE(0xFF),

    @InstructionInfo(0x5Eu, FlatInstructionStrategy::class)
    FLOAT32_GT(0xFF),

    @InstructionInfo(0x5Fu, FlatInstructionStrategy::class)
    FLOAT32_LE(0xFF),

    @InstructionInfo(0x5Du, FlatInstructionStrategy::class)
    FLOAT32_LT(0xFF),

    @InstructionInfo(0x97u, FlatInstructionStrategy::class)
    FLOAT32_MAX(0xFF),

    @InstructionInfo(0x96u, FlatInstructionStrategy::class)
    FLOAT32_MIN(0xFF),

    @InstructionInfo(0x94u, FlatInstructionStrategy::class)
    FLOAT32_MUL(0xFF),

    @InstructionInfo(0x5Cu, FlatInstructionStrategy::class)
    FLOAT32_NE(0xFF),

    @InstructionInfo(0x90u, FlatInstructionStrategy::class)
    FLOAT32_NEAREST(0xFF),

    @InstructionInfo(0x8Cu, FlatInstructionStrategy::class)
    FLOAT32_NEG(0xFF),

    @InstructionInfo(0xBEu, FlatInstructionStrategy::class)
    FLOAT32_REINTERPRET_INT32(0xFF),

    @InstructionInfo(0x91u, FlatInstructionStrategy::class)
    FLOAT32_SQRT(0xFF),

    @InstructionInfo(0x93u, FlatInstructionStrategy::class)
    FLOAT32_SUB(0xFF),

    @InstructionInfo(0x8Fu, FlatInstructionStrategy::class)
    FLOAT32_TRUNC(0xFF),

    @InstructionInfo(0x99u, FlatInstructionStrategy::class)
    FLOAT64_ABS(0xFF),

    @InstructionInfo(0xA0u, FlatInstructionStrategy::class)
    FLOAT64_ADD(0xFF),

    @InstructionInfo(0x9Bu, FlatInstructionStrategy::class)
    FLOAT64_CEIL(0xFF),

    @InstructionInfo(0xB7u, FlatInstructionStrategy::class)
    FLOAT64_CONVERT_INT32_S(0xFF),

    @InstructionInfo(0xB8u, FlatInstructionStrategy::class)
    FLOAT64_CONVERT_INT32_U(0xFF),

    @InstructionInfo(0xB9u, FlatInstructionStrategy::class)
    FLOAT64_CONVERT_INT64_S(0xFF),

    @InstructionInfo(0xBAu, FlatInstructionStrategy::class)
    FLOAT64_CONVERT_INT64_U(0xFF),

    @InstructionInfo(0xA6u, FlatInstructionStrategy::class)
    FLOAT64_COPYSIGN(0xFF),

    @InstructionInfo(0xA3u, FlatInstructionStrategy::class)
    FLOAT64_DIV(0xFF),

    @InstructionInfo(0x61u, FlatInstructionStrategy::class)
    FLOAT64_EQ(0xFF),

    @InstructionInfo(0x9Cu, FlatInstructionStrategy::class)
    FLOAT64_FLOOR(0xFF),

    @InstructionInfo(0x66u, FlatInstructionStrategy::class)
    FLOAT64_GE(0xFF),

    @InstructionInfo(0x64u, FlatInstructionStrategy::class)
    FLOAT64_GT(0xFF),

    @InstructionInfo(0x65u, FlatInstructionStrategy::class)
    FLOAT64_LE(0xFF),

    @InstructionInfo(0x63u, FlatInstructionStrategy::class)
    FLOAT64_LT(0xFF),

    @InstructionInfo(0xA5u, FlatInstructionStrategy::class)
    FLOAT64_MAX(0xFF),

    @InstructionInfo(0xA4u, FlatInstructionStrategy::class)
    FLOAT64_MIN(0xFF),

    @InstructionInfo(0xA2u, FlatInstructionStrategy::class)
    FLOAT64_MUL(0xFF),

    @InstructionInfo(0x62u, FlatInstructionStrategy::class)
    FLOAT64_NE(0xFF),

    @InstructionInfo(0x9Eu, FlatInstructionStrategy::class)
    FLOAT64_NEAREST(0xFF),

    @InstructionInfo(0x9Au, FlatInstructionStrategy::class)
    FLOAT64_NEG(0xFF),

    @InstructionInfo(0xBBu, FlatInstructionStrategy::class)
    FLOAT64_PROMOTE_FLOAT32(0xFF),

    @InstructionInfo(0xBFu, FlatInstructionStrategy::class)
    FLOAT64_REINTERPRET_INT64(0xFF),

    @InstructionInfo(0x9Fu, FlatInstructionStrategy::class)
    FLOAT64_SQRT(0xFF),

    @InstructionInfo(0xA1u, FlatInstructionStrategy::class)
    FLOAT64_SUB(0xFF),

    @InstructionInfo(0x9Du, FlatInstructionStrategy::class)
    FLOAT64_TRUNC(0xFF),

    @InstructionInfo(0x6Au, FlatInstructionStrategy::class)
    INT32_ADD(UniqueIds.INT32_ADD),

    @InstructionInfo(0x71u, FlatInstructionStrategy::class)
    INT32_AND(UniqueIds.INT32_AND),

    @InstructionInfo(0x67u, FlatInstructionStrategy::class)
    INT32_CLZ(0xFF),

    @InstructionInfo(0x68u, FlatInstructionStrategy::class)
    INT32_CTZ(0xFF),

    @InstructionInfo(0x6Du, FlatInstructionStrategy::class)
    INT32_DIV_S(0xFF),

    @InstructionInfo(0x6Eu, FlatInstructionStrategy::class)
    INT32_DIV_U(UniqueIds.INT32_DIV_U),

    @InstructionInfo(0x46u, FlatInstructionStrategy::class)
    INT32_EQ(UniqueIds.INT32_EQ),

    @InstructionInfo(0x45u, FlatInstructionStrategy::class)
    INT32_EQZ(UniqueIds.INT32_EQZ),

    @InstructionInfo(0xC0u, FlatInstructionStrategy::class)
    INT32_EXTEND8_S(0xFF),

    @InstructionInfo(0xC1u, FlatInstructionStrategy::class)
    INT32_EXTEND16_S(0xFF),

    @InstructionInfo(0x4Eu, FlatInstructionStrategy::class)
    INT32_GE_S(UniqueIds.INT32_GE_S),

    @InstructionInfo(0x4Fu, FlatInstructionStrategy::class)
    INT32_GE_U(UniqueIds.INT32_GE_U),

    @InstructionInfo(0x4Au, FlatInstructionStrategy::class)
    INT32_GT_S(UniqueIds.INT32_GT_S),

    @InstructionInfo(0x4Bu, FlatInstructionStrategy::class)
    INT32_GT_U(UniqueIds.INT32_GT_U),

    @InstructionInfo(0x4Cu, FlatInstructionStrategy::class)
    INT32_LE_S(UniqueIds.INT32_LE_S),

    @InstructionInfo(0x4Du, FlatInstructionStrategy::class)
    INT32_LE_U(UniqueIds.INT32_LE_U),

    @InstructionInfo(0x48u, FlatInstructionStrategy::class)
    INT32_LT_S(UniqueIds.INT32_LT_S),

    @InstructionInfo(0x49u, FlatInstructionStrategy::class)
    INT32_LT_U(UniqueIds.INT32_LT_U),

    @InstructionInfo(0x6Cu, FlatInstructionStrategy::class)
    INT32_MUL(UniqueIds.INT32_MUL),

    @InstructionInfo(0x47u, FlatInstructionStrategy::class)
    INT32_NE(UniqueIds.INT32_NE),

    @InstructionInfo(0x72u, FlatInstructionStrategy::class)
    INT32_OR(UniqueIds.INT32_OR),

    @InstructionInfo(0x69u, FlatInstructionStrategy::class)
    INT32_POPCNT(0xFF),

    @InstructionInfo(0xBCu, FlatInstructionStrategy::class)
    INT32_REINTERPRET_FLOAT32(0xFF),

    @InstructionInfo(0x6Fu, FlatInstructionStrategy::class)
    INT32_REM_S(0xFF),

    @InstructionInfo(0x70u, FlatInstructionStrategy::class)
    INT32_REM_U(0xFF),

    @InstructionInfo(0x77u, FlatInstructionStrategy::class)
    INT32_ROTL(UniqueIds.INT32_ROTL),

    @InstructionInfo(0x78u, FlatInstructionStrategy::class)
    INT32_ROTR(0xFF),

    @InstructionInfo(0x74u, FlatInstructionStrategy::class)
    INT32_SHL(UniqueIds.INT32_SHL),

    @InstructionInfo(0x75u, FlatInstructionStrategy::class)
    INT32_SHR_S(UniqueIds.INT32_SHR_S),

    @InstructionInfo(0x76u, FlatInstructionStrategy::class)
    INT32_SHR_U(UniqueIds.INT32_SHR_U),

    @InstructionInfo(0x6Bu, FlatInstructionStrategy::class)
    INT32_SUB(UniqueIds.INT32_SUB),

    @InstructionInfo(0xA8u, FlatInstructionStrategy::class)
    INT32_TRUNC_FLOAT32_S(0xFF),

    @InstructionInfo(0xA9u, FlatInstructionStrategy::class)
    INT32_TRUNC_FLOAT32_U(0xFF),

    @InstructionInfo(0xAAu, FlatInstructionStrategy::class)
    INT32_TRUNC_FLOAT64_S(0xFF),

    @InstructionInfo(0xABu, FlatInstructionStrategy::class)
    INT32_TRUNC_FLOAT64_U(0xFF),

    @InstructionInfo(0xA7u, FlatInstructionStrategy::class)
    INT32_WRAP_INT64(UniqueIds.INT32_WRAP_INT64),

    @InstructionInfo(0x73u, FlatInstructionStrategy::class)
    INT32_XOR(UniqueIds.INT32_XOR),

    @InstructionInfo(0x7Cu, FlatInstructionStrategy::class)
    INT64_ADD(UniqueIds.INT64_ADD),

    @InstructionInfo(0x83u, FlatInstructionStrategy::class)
    INT64_AND(UniqueIds.INT64_AND),

    @InstructionInfo(0x79u, FlatInstructionStrategy::class)
    INT64_CLZ(0xFF),

    @InstructionInfo(0x7Au, FlatInstructionStrategy::class)
    INT64_CTZ(0xFF),

    @InstructionInfo(0x7Fu, FlatInstructionStrategy::class)
    INT64_DIV_S(0xFF),

    @InstructionInfo(0x80u, FlatInstructionStrategy::class)
    INT64_DIV_U(0xFF),

    @InstructionInfo(0x51u, FlatInstructionStrategy::class)
    INT64_EQ(UniqueIds.INT64_EQ),

    @InstructionInfo(0x50u, FlatInstructionStrategy::class)
    INT64_EQZ(UniqueIds.INT64_EQZ),

    @InstructionInfo(0xC2u, FlatInstructionStrategy::class)
    INT64_EXTEND8_S(0xFF),

    @InstructionInfo(0xC3u, FlatInstructionStrategy::class)
    INT64_EXTEND16_S(0xFF),

    @InstructionInfo(0xC4u, FlatInstructionStrategy::class)
    INT64_EXTEND32_S(0xFF),

    @InstructionInfo(0xACu, FlatInstructionStrategy::class)
    INT64_EXTEND_INT32_S(UniqueIds.INT64_EXTEND_INT32_S),

    @InstructionInfo(0xADu, FlatInstructionStrategy::class)
    INT64_EXTEND_INT32_U(UniqueIds.INT64_EXTEND_INT32_U),

    @InstructionInfo(0x59u, FlatInstructionStrategy::class)
    INT64_GE_S(UniqueIds.INT64_GE_S),

    @InstructionInfo(0x5Au, FlatInstructionStrategy::class)
    INT64_GE_U(0xFF),

    @InstructionInfo(0x55u, FlatInstructionStrategy::class)
    INT64_GT_S(0xFF),

    @InstructionInfo(0x56u, FlatInstructionStrategy::class)
    INT64_GT_U(UniqueIds.INT64_GT_U),

    @InstructionInfo(0x57u, FlatInstructionStrategy::class)
    INT64_LE_S(UniqueIds.INT64_LE_S),

    @InstructionInfo(0x58u, FlatInstructionStrategy::class)
    INT64_LE_U(0xFF),

    @InstructionInfo(0x53u, FlatInstructionStrategy::class)
    INT64_LT_S(0xFF),

    @InstructionInfo(0x54u, FlatInstructionStrategy::class)
    INT64_LT_U(UniqueIds.INT64_LT_U),

    @InstructionInfo(0x7Eu, FlatInstructionStrategy::class)
    INT64_MUL(UniqueIds.INT64_MUL),

    @InstructionInfo(0x52u, FlatInstructionStrategy::class)
    INT64_NE(UniqueIds.INT64_NE),

    @InstructionInfo(0x84u, FlatInstructionStrategy::class)
    INT64_OR(UniqueIds.INT64_OR),

    @InstructionInfo(0x7Bu, FlatInstructionStrategy::class)
    INT64_POPCNT(0xFF),

    @InstructionInfo(0xBDu, FlatInstructionStrategy::class)
    INT64_REINTERPRET_FLOAT64(0xFF),

    @InstructionInfo(0x81u, FlatInstructionStrategy::class)
    INT64_REM_S(0xFF),

    @InstructionInfo(0x82u, FlatInstructionStrategy::class)
    INT64_REM_U(0xFF),

    @InstructionInfo(0x89u, FlatInstructionStrategy::class)
    INT64_ROTL(0xFF),

    @InstructionInfo(0x8Au, FlatInstructionStrategy::class)
    INT64_ROTR(0xFF),

    @InstructionInfo(0x86u, FlatInstructionStrategy::class)
    INT64_SHL(UniqueIds.INT64_SHL),

    @InstructionInfo(0x87u, FlatInstructionStrategy::class)
    INT64_SHR_S(0xFF),

    @InstructionInfo(0x88u, FlatInstructionStrategy::class)
    INT64_SHR_U(UniqueIds.INT64_SHR_U),

    @InstructionInfo(0x7Du, FlatInstructionStrategy::class)
    INT64_SUB(UniqueIds.INT64_SUB),

    @InstructionInfo(0xAEu, FlatInstructionStrategy::class)
    INT64_TRUNC_FLOAT32_S(0xFF),

    @InstructionInfo(0xAFu, FlatInstructionStrategy::class)
    INT64_TRUNC_FLOAT32_U(0xFF),

    @InstructionInfo(0xB0u, FlatInstructionStrategy::class)
    INT64_TRUNC_FLOAT64_S(0xFF),

    @InstructionInfo(0xB1u, FlatInstructionStrategy::class)
    INT64_TRUNC_FLOAT64_U(0xFF),

    @InstructionInfo(0x85u, FlatInstructionStrategy::class)
    INT64_XOR(UniqueIds.INT64_XOR),

    @InstructionInfo(0x01u, FlatInstructionStrategy::class)
    NOP(0xFF),

    @InstructionInfo(0xD1u, FlatInstructionStrategy::class)
    REF_IS_NULL(0xFF),

    @InstructionInfo(0x0Fu, FlatInstructionStrategy::class)
    RETURN(UniqueIds.RETURN),

    @InstructionInfo(0x1Bu, FlatInstructionStrategy::class)
    SELECT(UniqueIds.SELECT),

    @InstructionInfo(0x00u, FlatInstructionStrategy::class)
    UNREACHABLE(0xFF)
}
