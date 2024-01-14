package dev.fir3.iwan.engine.vm

import dev.fir3.iwan.engine.models.AuxiliaryModuleInstance
import dev.fir3.iwan.engine.models.InstantiatedModuleInstance
import dev.fir3.iwan.engine.models.ReferenceValue
import dev.fir3.iwan.engine.models.Value
import dev.fir3.iwan.engine.models.stack.StackFrame
import dev.fir3.iwan.engine.models.stack.StackLabel
import dev.fir3.iwan.engine.models.stack.StackValue
import dev.fir3.iwan.engine.vm.interpreter.Interpreter
import dev.fir3.iwan.io.wasm.models.*
import dev.fir3.iwan.io.wasm.models.instructions.*

object Instantiation {
    fun instantiate(module: Module): InstantiatedModuleInstance {
        val auxiliaryInstance = Allocation.allocateAuxiliaryModule(module)

        Stack.push(StackFrame(arrayOf(), auxiliaryInstance, 0))
        val globalInitializers = mutableListOf<Value>()

        for (global in module.globals) {
            globalInitializers += executeAndGetValue(
                global.initializer.body
            ).value
        }

        val elementInitializers = mutableListOf<List<ReferenceValue>>()

        for (element in module.elements) {
            val values = element
                .initializers
                .map { expression ->
                    val value = executeAndGetValue(expression.body).value

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

        Stack.push(StackFrame(arrayOf(), instance, 0))

        for (index in module.elements.indices) {
            val element = module.elements[index]

            if (element is ActiveElement) {
                val n = element.initializers.size

                execute(
                    element.offset.body + listOf(
                        Int32ConstInstruction(0),
                        Int32ConstInstruction(n),
                        TableInitInstruction(element.table, index.toUInt()),
                        ElementDropInstruction(index.toUInt())
                    )
                )
            }

            if (element is DeclarativeElement) {
                execute(listOf(ElementDropInstruction(index.toUInt())))
            }
        }

        for (index in module.data.indices) {
            val data = module.data[index]

            if (data is ActiveData) {
                execute(
                    data.offset.body + listOf(
                        Int32ConstInstruction(0),
                        Int32ConstInstruction(data.initializers.size),
                        MemoryInitInstruction(index.toUInt()),
                        DataDropInstruction(index.toUInt())
                    )
                )
            }
        }

        Stack.pop()
        return instance
    }

    private fun execute(instructions: List<Instruction>) {
        Stack.push(StackLabel(1, instructions, 0, false))
        Interpreter.execute()
    }

    private fun executeAndGetValue(
        instructions: List<Instruction>
    ): StackValue {
        execute(instructions)
        return Stack.pop() as StackValue
    }
}
