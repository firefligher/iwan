package dev.fir3.iwan.engine.models

import dev.fir3.iwan.io.wasm.models.TableType

data class TableInstance(
    val type: TableType,
    val elements: MutableList<ReferenceValue>
)
