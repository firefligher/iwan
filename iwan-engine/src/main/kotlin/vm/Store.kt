package dev.fir3.iwan.engine.vm

import dev.fir3.iwan.engine.models.*

object Store {
    val data: MutableList<DataInstance> = mutableListOf()
    val elements: MutableList<ElementInstance> = mutableListOf()
    val functions: MutableList<FunctionInstance> = mutableListOf()
    val globals: MutableList<GlobalInstance> = mutableListOf()
    val memories: MutableList<MemoryInstance> = mutableListOf()
    val tables: MutableList<TableInstance> = mutableListOf()
}
