package dev.fir3.iwan.io.wasm.models

import dev.fir3.iwan.io.wasm.models.sections.Section

data class Module(
    val sections: List<Section>
)
