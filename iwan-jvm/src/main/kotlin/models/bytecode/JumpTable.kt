package dev.fir3.iwan.jvm.models.bytecode

data class JumpTable(
    val indexedEntries: Map<Int, JumpTargetGenerator>,
    val defaultEntry: JumpTargetGenerator
)
