package dev.fir3.iwan.io.wasm.models.sections

import dev.fir3.iwan.io.wasm.models.ElementSegment

data class ElementSection(val elements: List<ElementSegment>): Section
