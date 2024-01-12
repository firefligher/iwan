package dev.fir3.iwan.io.wasm.models

import dev.fir3.iwan.io.wasm.models.valueTypes.ValueType

data class Code(
    val locals: Map<ValueType, UInt>,
    val body: Expression
)
