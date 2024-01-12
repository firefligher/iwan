package dev.fir3.iwan.io.wasm.serialization

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.DeserializationStrategy
import dev.fir3.iwan.io.serialization.deserialize
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.source.expectInt8
import dev.fir3.iwan.io.source.readVarUInt32
import dev.fir3.iwan.io.wasm.models.*
import dev.fir3.iwan.io.wasm.models.valueTypes.ReferenceType
import java.io.IOException

internal object ElementSegmentStrategy :
    DeserializationStrategy<ElementSegment> {
    @Throws(IOException::class)
    override fun deserialize(
        source: ByteSource,
        context: DeserializationContext
    ) = when (val typeId = source.readVarUInt32()) {
        0u -> {
            val expression = context.deserialize<Expression>(source)
            val functionIndicesCount = source.readVarUInt32()
            val functionIndices = mutableListOf<UInt>()

            for (i in 0u until functionIndicesCount) {
                functionIndices.add(source.readVarUInt32())
            }

            ElementSegmentType0(expression, functionIndices)
        }

        1u -> {
            source.expectInt8(0)

            val functionIndicesCount = source.readVarUInt32()
            val functionIndices = mutableListOf<UInt>()

            for (i in 0u until functionIndicesCount) {
                functionIndices.add(source.readVarUInt32())
            }

            ElementSegmentType1(functionIndices)
        }

        2u -> {
            val tableIndex = source.readVarUInt32()
            val expression = context.deserialize<Expression>(source)
            source.expectInt8(0)
            val functionIndicesCount = source.readVarUInt32()
            val functionIndices = mutableListOf<UInt>()

            for (i in 0u until functionIndicesCount) {
                functionIndices.add(source.readVarUInt32())
            }

            ElementSegmentType2(tableIndex, expression, functionIndices)
        }

        3u -> {
            source.expectInt8(0)

            val functionIndicesCount = source.readVarUInt32()
            val functionIndices = mutableListOf<UInt>()

            for (i in 0u until functionIndicesCount) {
                functionIndices.add(source.readVarUInt32())
            }

            ElementSegmentType3(functionIndices)
        }

        4u -> {
            val expression = context.deserialize<Expression>(source)
            val expressionsCount = source.readVarUInt32()
            val expressions = mutableListOf<Expression>()

            for (i in 0u until expressionsCount) {
                expressions.add(context.deserialize(source))
            }

            ElementSegmentType4(expression, expressions)
        }

        5u -> {
            val referenceType = context.deserialize<ReferenceType>(source)
            val expressionsCount = source.readVarUInt32()
            val expressions = mutableListOf<Expression>()

            for (i in 0u until expressionsCount) {
                expressions.add(context.deserialize(source))
            }

            ElementSegmentType5(referenceType, expressions)
        }

        6u -> {
            val tableIndex = source.readVarUInt32()
            val expression = context.deserialize<Expression>(source)
            val referenceType = context.deserialize<ReferenceType>(source)
            val expressionsCount = source.readVarUInt32()
            val expressions = mutableListOf<Expression>()

            for (i in 0u until expressionsCount) {
                expressions.add(context.deserialize(source))
            }

            ElementSegmentType6(tableIndex, expression, referenceType, expressions)
        }

        7u -> {
            val referenceType = context.deserialize<ReferenceType>(source)
            val expressionsCount = source.readVarUInt32()
            val expressions = mutableListOf<Expression>()

            for (i in 0u until expressionsCount) {
                expressions.add(context.deserialize(source))
            }

            ElementSegmentType7(referenceType, expressions)
        }

        else -> throw IOException("Invalid element segment typeId '$typeId'")
    }
}
