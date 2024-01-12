package dev.fir3.iwan.io.wasm.models.instructions

enum class FlatInstruction : Instruction {
    DROP,
    FLOAT32_ABS,
    FLOAT32_ADD,
    FLOAT32_CEIL,
    FLOAT32_CONVERT_INT32_S,
    FLOAT32_CONVERT_INT32_U,
    FLOAT32_CONVERT_INT64_S,
    FLOAT32_CONVERT_INT64_U,
    FLOAT32_COPYSIGN,
    FLOAT32_DEMOTE_FLOAT64,
    FLOAT32_DIV,
    FLOAT32_EQ,
    FLOAT32_FLOOR,
    FLOAT32_GE,
    FLOAT32_GT,
    FLOAT32_LE,
    FLOAT32_LT,
    FLOAT32_MAX,
    FLOAT32_MIN,
    FLOAT32_MUL,
    FLOAT32_NE,
    FLOAT32_NEAREST,
    FLOAT32_NEG,
    FLOAT32_REINTERPRET_INT32,
    FLOAT32_SQRT,
    FLOAT32_SUB,
    FLOAT32_TRUNC,
    FLOAT64_ABS,
    FLOAT64_ADD,
    FLOAT64_CEIL,
    FLOAT64_CONVERT_INT32_S,
    FLOAT64_CONVERT_INT32_U,
    FLOAT64_CONVERT_INT64_S,
    FLOAT64_CONVERT_INT64_U,
    FLOAT64_COPYSIGN,
    FLOAT64_DIV,
    FLOAT64_EQ,
    FLOAT64_FLOOR,
    FLOAT64_GE,
    FLOAT64_GT,
    FLOAT64_LE,
    FLOAT64_LT,
    FLOAT64_MAX,
    FLOAT64_MIN,
    FLOAT64_MUL,
    FLOAT64_NE,
    FLOAT64_NEAREST,
    FLOAT64_NEG,
    FLOAT64_PROMOTE_FLOAT32,
    FLOAT64_REINTERPRET_INT64,
    FLOAT64_SQRT,
    FLOAT64_SUB,
    FLOAT64_TRUNC,
    INT32_ADD,
    INT32_AND,
    INT32_CLZ,
    INT32_CTZ,
    INT32_DIV_S,
    INT32_DIV_U,
    INT32_EQ,
    INT32_EQZ,
    INT32_EXTEND8_S,
    INT32_EXTEND16_S,
    INT32_GE_S,
    INT32_GE_U,
    INT32_GT_S,
    INT32_GT_U,
    INT32_LE_S,
    INT32_LE_U,
    INT32_LT_S,
    INT32_LT_U,
    INT32_MUL,
    INT32_NE,
    INT32_OR,
    INT32_POPCNT,
    INT32_REINTERPRET_FLOAT32,
    INT32_REM_S,
    INT32_REM_U,
    INT32_ROTL,
    INT32_ROTR,
    INT32_SHL,
    INT32_SHR_S,
    INT32_SHR_U,
    INT32_SUB,
    INT32_TRUNC_FLOAT32_S,
    INT32_TRUNC_FLOAT32_U,
    INT32_TRUNC_FLOAT64_S,
    INT32_TRUNC_FLOAT64_U,
    INT32_WRAP_INT64,
    INT32_XOR,
    INT64_ADD,
    INT64_AND,
    INT64_CLZ,
    INT64_CTZ,
    INT64_DIV_S,
    INT64_DIV_U,
    INT64_EQ,
    INT64_EQZ,
    INT64_EXTEND8_S,
    INT64_EXTEND16_S,
    INT64_EXTEND32_S,
    INT64_EXTEND_INT32_S,
    INT64_EXTEND_INT32_U,
    INT64_GE_S,
    INT64_GE_U,
    INT64_GT_S,
    INT64_GT_U,
    INT64_LE_S,
    INT64_LE_U,
    INT64_LT_S,
    INT64_LT_U,
    INT64_MUL,
    INT64_NE,
    INT64_OR,
    INT64_POPCNT,
    INT64_REINTERPRET_FLOAT64,
    INT64_REM_S,
    INT64_REM_U,
    INT64_ROTL,
    INT64_ROTR,
    INT64_SHL,
    INT64_SHR_S,
    INT64_SHR_U,
    INT64_SUB,
    INT64_TRUNC_FLOAT32_S,
    INT64_TRUNC_FLOAT32_U,
    INT64_TRUNC_FLOAT64_S,
    INT64_TRUNC_FLOAT64_U,
    INT64_XOR,
    NOP,
    RETURN,
    SELECT,
    UNREACHABLE
}
