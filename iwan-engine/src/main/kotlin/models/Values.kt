package dev.fir3.iwan.engine.models

import dev.fir3.iwan.io.wasm.models.valueTypes.ReferenceType

sealed interface ReferenceValue

data class ExternalReference(val externalAddress: Int): ReferenceValue

data class FunctionReference(val functionAddress: Int): ReferenceValue

class ReferenceNull(val type: ReferenceType) : ReferenceValue
