package dev.fir3.iwan.io.wasm.models.instructions

object UniqueIds {
    const val BLOCK = 0
    const val CALL = 1
    const val CONDITIONAL_BRANCH = 2
    const val DATA_DROP = 2 + 1
    const val DROP = 3 + 1
    const val ELEMENT_DROP = 4 + 1
    const val FLOAT32_CONST = 5 + 1
    const val FLOAT64_CONST = 6 + 1
    const val GLOBAL_GET = 7 + 1
    const val GLOBAL_SET = 9
    const val INT32_AND = 10
    const val INT32_CONST = 11
    const val INT32_EQZ = 12
    const val INT32_GE_S = 13
    const val INT32_LOAD = 14
    const val INT32_SUB = 15
    const val INT64_CONST = 16
    const val LOCAL_GET = 17
    const val LOCAL_SET = 18
    const val LOCAL_TEE = 19
    const val LOOP = 20
    const val MEMORY_INIT = 21
    const val REFERENCE_FUNCTION = 22
    const val RETURN = 23
    const val STORE_FLOAT32 = 24
    const val STORE_FLOAT64 = 25
    const val STORE_INT32 = 26
    const val STORE_INT32_8 = 27
    const val STORE_INT32_16 = 28
    const val STORE_INT64 = 29
    const val STORE_INT64_8 = 30
    const val STORE_INT64_16 = 31
    const val STORE_INT64_32 = 32
    const val TABLE_INIT = 33
    const val TABLE_SET = 34
    const val UNCONDITIONAL_BRANCH = 35

    // Unsorted.

    const val INT32_ADD = 36
    const val INT32_XOR = 37
    const val INT32_LOAD_8U = 38
    const val INT32_MUL = 39
    const val INT32_GT_S = 40
    const val SELECT = 41
    const val INT32_DIV_U = 42
    const val INT32_GE_U = 43
    const val INT32_LT_S = 44
    const val INT32_EQ = 45
    const val INT32_LT_U = 46
    const val INT32_NE = 47
    const val INT32_OR = 48
    const val CALL_INDIRECT = 49
    const val INT32_GT_U = 50
    const val INT32_SHL = 51
    const val INT32_SHR_S = 52
    const val CONDITIONAL_BLOCK = 53
    const val INT64_EXTEND_INT32_U = 54
    const val INT64_MUL = 55
    const val TABLE_BRANCH = 56
    const val INT64_EXTEND_INT32_S = 57
    const val INT64_EQZ = 58
    const val INT64_LOAD = 59
    const val INT64_NE = 60
    const val INT64_ADD = 61
    const val INT64_LE_S = 62
    const val INT32_LE_U = 63
    const val INT64_GE_S = 64
    const val INT32_SHR_U = 65
    const val INT32_LOAD_8S = 66
    const val INT64_SHR_U = 67
    const val INT64_LT_U = 68
    const val INT64_AND = 69
    const val INT64_SHL = 70
    const val INT64_OR = 71
    const val INT64_XOR = 72
    const val INT64_SUB = 73
    const val INT64_EQ = 74
    const val INT32_WRAP_INT64 = 75
    const val INT64_LOAD_32U = 76
    const val INT64_GT_U = 77
    const val INT32_LE_S = 78
    const val INT32_ROTL = 79
}
