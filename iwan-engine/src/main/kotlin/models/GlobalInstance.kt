package dev.fir3.iwan.engine.models

import dev.fir3.iwan.io.wasm.models.GlobalType

data class GlobalInstance(
    val type: GlobalType,
    val value: Value
)
