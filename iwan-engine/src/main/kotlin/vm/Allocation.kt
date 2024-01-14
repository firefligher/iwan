package dev.fir3.iwan.engine.vm

import dev.fir3.iwan.engine.models.*
import dev.fir3.iwan.io.wasm.models.*
import dev.fir3.iwan.io.wasm.models.Function
import dev.fir3.iwan.io.wasm.models.valueTypes.ReferenceType

object Allocation {
    private fun allocateData(bytes: List<Byte>): Int {
        val address = Store.data.size
        val instance = DataInstance(bytes.toByteArray())

        Store.data.add(address, instance)
        return address
    }

    private fun allocateElement(
        referenceType: ReferenceType,
        references: List<ReferenceValue>
    ): Int {
        val address = Store.elements.size
        val instance = ElementInstance(referenceType, references)

        Store.elements.add(address, instance)
        return address
    }

    private fun allocateFunction(
        function: Function,
        moduleInstance: ModuleInstance
    ): Int {
        val address = Store.functions.size
        val type = moduleInstance.types[function.typeIndex.toInt()]
        val instance = WasmFunctionInstance(type, moduleInstance, function)

        Store.functions.add(address, instance)
        return address
    }

    private fun allocateGlobal(
        type: GlobalType,
        value: Value
    ): Int {
        val address = Store.globals.size
        val instance = GlobalInstance(type, value)

        Store.globals.add(address, instance)
        return address
    }

    private fun allocateMemory(memoryType: MemoryType): Int {
        val address = Store.memories.size
        val instance = MemoryInstance(
            memoryType,
            ByteArray((memoryType.minimum shl 16).toInt())
        )

        Store.memories.add(address, instance)
        return address
    }

    fun allocateAuxiliaryModule(module: Module): AuxiliaryModuleInstance {
        val functionAddresses = mutableListOf<Int>()
        val globalAddresses = mutableListOf<Int>()
        val instance = AuxiliaryModuleInstance(
            functionAddresses,
            globalAddresses,
            module.types
        )

        for (import in module.imports) {
            // TODO: Implement resolution strategy.

            when (import) {
                is FunctionImport -> functionAddresses += -1
                is GlobalImport -> functionAddresses += -1
                else -> {}
            }
        }

        for (function in module.functions) {
            functionAddresses += allocateFunction(function, instance)
        }

        return instance
    }

    fun allocateModule(
        module: Module,
        globalValues: List<Value>,
        elementValues: List<List<ReferenceValue>>
    ): InstantiatedModuleInstance {
        val dataAddresses = mutableListOf<Int>()
        val elementAddresses = mutableListOf<Int>()
        val exports = mutableListOf<ExportInstance>()
        val functionAddresses = mutableListOf<Int>()
        val globalAddresses = mutableListOf<Int>()
        val memoryAddresses = mutableListOf<Int>()
        val tableAddresses = mutableListOf<Int>()
        val instance = InstantiatedModuleInstance(
            dataAddresses,
            elementAddresses,
            exports,
            functionAddresses,
            globalAddresses,
            memoryAddresses,
            tableAddresses,
            module.types
        )

        for (import in module.imports) {
            // TODO: Implement resolution strategy.

            when (import) {
                is FunctionImport -> functionAddresses += -1
                is GlobalImport -> functionAddresses += -1
                is MemoryImport -> memoryAddresses += -1
                is TableImport -> tableAddresses += -1
                else -> {}
            }
        }

        for (data in module.data) {
            dataAddresses += allocateData(data.initializers)
        }

        for (index in module.elements.indices) {
            val element = module.elements[index]
            val value = elementValues[index]

            elementAddresses += allocateElement(element.type, value)
        }

        for (function in module.functions) {
            functionAddresses += allocateFunction(function, instance)
        }

        for (index in module.globals.indices) {
            val global = module.globals[index]
            val value = globalValues[index]

            globalAddresses += allocateGlobal(global.type, value)
        }

        for (memoryType in module.memories) {
            memoryAddresses += allocateMemory(memoryType)
        }

        for (tableType in module.tables) {
            tableAddresses += allocateTable(tableType, ReferenceNull)
        }

        for (export in module.exports) {
            exports += when (export) {
                is FunctionExport -> FunctionExportInstance(
                    export.name,
                    functionAddresses[export.functionIndex.toInt()]
                )

                is GlobalExport -> GlobalExportInstance(
                    export.name,
                    globalAddresses[export.globalIndex.toInt()]
                )

                is MemoryExport -> MemoryExportInstance(
                    export.name,
                    tableAddresses[export.memoryIndex.toInt()]
                )

                is TableExport -> TableExportInstance(
                    export.name,
                    tableAddresses[export.tableIndex.toInt()]
                )
            }
        }

        return instance
    }

    private fun allocateTable(
        tableType: TableType,
        initializationValue: ReferenceValue
    ): Int {
        val address = Store.tables.size
        val instance = TableInstance(
            tableType,
            tableType
                .minimum
                .downTo(1u)
                .map { initializationValue }
                .toMutableList()
        )

        Store.tables.add(address, instance)
        return address
    }

    fun deallocateAuxiliaryModuleInstance(instance: AuxiliaryModuleInstance) {
        for (address in instance.functionAddresses.reversed()) {
            if (address == -1) continue
            Store.functions.removeAt(address)
        }
    }
}
