package dev.fir3.iwan.io.wasm.models

import dev.fir3.iwan.io.wasm.models.valueTypes.ReferenceType

sealed interface ElementSegment

data class ElementSegmentType0(
    val e: Expression,
    val y: List<UInt>
) : ElementSegment

data class ElementSegmentType1(val y: List<UInt>) : ElementSegment
data class ElementSegmentType2(
    val x: UInt,
    val e: Expression,
    val y: List<UInt>
) : ElementSegment

data class ElementSegmentType3(val y: List<UInt>) : ElementSegment
data class ElementSegmentType4(
    val e: Expression,
    val el: List<Expression>
) : ElementSegment

data class ElementSegmentType5(
    val et: ReferenceType,
    val el: List<Expression>
) : ElementSegment

data class ElementSegmentType6(
    val x: UInt,
    val e: Expression,
    val et: ReferenceType,
    val el: List<Expression>
) : ElementSegment

data class ElementSegmentType7(
    val et: ReferenceType,
    val el: List<Expression>
) : ElementSegment
