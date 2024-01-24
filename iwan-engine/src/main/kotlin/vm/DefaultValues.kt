package dev.fir3.iwan.engine.vm

import dev.fir3.iwan.engine.models.*
import dev.fir3.iwan.io.wasm.models.valueTypes.NumberType
import dev.fir3.iwan.io.wasm.models.valueTypes.ReferenceType
import dev.fir3.iwan.io.wasm.models.valueTypes.ValueType
import dev.fir3.iwan.io.wasm.models.valueTypes.VectorType

object DefaultValues {
    fun getDefaultValue(valueType: ValueType): Any = when (valueType) {
        NumberType.Float32 -> 0F
        NumberType.Float64 -> 0.0
        NumberType.Int32 -> 0
        NumberType.Int64 -> 0L
        ReferenceType.ExternalReference -> ReferenceNull.EXTERNAL
        ReferenceType.FunctionReference -> ReferenceNull.FUNCTION
        VectorType.Vector128 -> error("Unsupported Operation")
    }
}
