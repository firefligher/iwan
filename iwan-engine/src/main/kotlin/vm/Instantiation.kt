package dev.fir3.iwan.engine.vm

import dev.fir3.iwan.engine.models.AuxiliaryModuleInstance
import dev.fir3.iwan.engine.models.InstantiatedModuleInstance
import dev.fir3.iwan.engine.models.ReferenceValue
import dev.fir3.iwan.engine.models.Value
import dev.fir3.iwan.engine.models.stack.StackFrame
import dev.fir3.iwan.engine.vm.interpreter.Interpreter
import dev.fir3.iwan.io.wasm.models.*
import dev.fir3.iwan.io.wasm.models.instructions.*

object Instantiation {
    fun instantiate(module: Module): InstantiatedModuleInstance {
        val auxiliaryInstance = Allocation.allocateAuxiliaryModule(module)

        Stack.push(StackFrame(arrayOf(), auxiliaryInstance))
        val globalInitializers = mutableListOf<Value>()

        for (global in module.globals) {
            globalInitializers += Interpreter
                .executeAndGetValue(global.initializer)
        }

        val elementInitializers = mutableListOf<List<ReferenceValue>>()

        for (element in module.elements) {
            val values = element
                .initializers
                .map { expression ->
                    val value = Interpreter.executeAndGetValue(expression)

                    if (value !is ReferenceValue) {
                        throw IllegalStateException(
                            "Element initializer evaluated to non-reference " +
                                    "value"
                        )
                    }

                    value
                }

            elementInitializers.add(values)
        }

        Stack.pop()
        Allocation.deallocateAuxiliaryModuleInstance(auxiliaryInstance)

        // Instantiation

        val instance = Allocation.allocateModule(
            module,
            globalInitializers,
            elementInitializers
        )

        Stack.push(StackFrame(arrayOf(), instance))

        for (index in module.elements.indices) {
            val element = module.elements[index]

            if (element is ActiveElement) {
                val n = element.initializers.size

                Interpreter.execute(element.offset)
                Interpreter.execute(Int32ConstInstruction(0))
                Interpreter.execute(Int32ConstInstruction(n))
                Interpreter.execute(
                    TableInitInstruction(element.table, index.toUInt())
                )

                Interpreter.execute(ElementDropInstruction(index.toUInt()))
            }

            if (element is DeclarativeElement) {
                Interpreter.execute(ElementDropInstruction(index.toUInt()))
            }
        }

        for (index in module.data.indices) {
            val data = module.data[index]

            if (data is ActiveData) {
                Interpreter.execute(data.offset)
                Interpreter.execute(Int32ConstInstruction(0))
                Interpreter.execute(
                    Int32ConstInstruction(data.initializers.size)
                )

                Interpreter.execute(MemoryInitInstruction(index.toUInt()))
                Interpreter.execute(DataDropInstruction(index.toUInt()))
            }
        }

        Stack.pop()
        return instance
    }
}
