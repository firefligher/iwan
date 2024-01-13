package dev.fir3.iwan.engine.models

import dev.fir3.iwan.io.wasm.models.valueTypes.ReferenceType

data class ElementInstance(
    val type: ReferenceType,
    val elements: List<ReferenceValue>
)
