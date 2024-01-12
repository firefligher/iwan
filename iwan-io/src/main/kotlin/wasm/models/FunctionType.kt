package dev.fir3.iwan.io.wasm.models

import dev.fir3.iwan.io.wasm.models.valueTypes.ValueType

data class FunctionType(
    val parameterTypes: List<ValueType>,
    val resultTypes: List<ValueType>
)
