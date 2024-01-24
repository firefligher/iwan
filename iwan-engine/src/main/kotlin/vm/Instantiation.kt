package dev.fir3.iwan.engine.vm

import dev.fir3.iwan.engine.models.*
import dev.fir3.iwan.engine.vm.interpreter.Interpreter
import dev.fir3.iwan.engine.vm.stack.DefaultStack
import dev.fir3.iwan.engine.vm.stack.Stack
import dev.fir3.iwan.io.wasm.models.*
import dev.fir3.iwan.io.wasm.models.instructions.*
import dev.fir3.iwan.io.wasm.models.valueTypes.NumberType
import dev.fir3.iwan.io.wasm.models.valueTypes.ReferenceType
import dev.fir3.iwan.io.wasm.models.valueTypes.ValueType
import dev.fir3.iwan.io.wasm.models.valueTypes.VectorType

object Instantiation {
    fun instantiate(module: Module): InstantiatedModuleInstance {
        val auxiliaryInstance = Allocation.allocateAuxiliaryModule(module)
        val stack = DefaultStack()
        val interpreter = Interpreter(stack)

        val globalInitializers = module.globals.map { global ->
            execute(
                stack,
                interpreter,
                auxiliaryInstance,
                global.type.valueType,
                global.initializer.body
            )
        }

        val elementInitializers = module.elements.map { element ->
            element.initializers.map { expression ->
                execute(
                    stack,
                    interpreter,
                    auxiliaryInstance,
                    element.type,
                    expression.body
                ) as ReferenceValue
            }
        }

        Allocation.deallocateAuxiliaryModuleInstance(auxiliaryInstance)

        // Instantiation

        val instance = Allocation.allocateModule(
            module,
            globalInitializers,
            elementInitializers
        )

        for (index in module.elements.indices) {
            val element = module.elements[index]

            if (element is ActiveElement) {
                val n = element.initializers.size

                execute(
                    stack,
                    interpreter,
                    instance,
                    resultType = null,
                    element.offset.body + listOf(
                        Int32ConstInstruction(0),
                        Int32ConstInstruction(n),
                        TableInitInstruction(element.table, index.toUInt()),
                        ElementDropInstruction(index.toUInt())
                    )
                )
            }

            if (element is DeclarativeElement) {
                execute(
                    stack,
                    interpreter,
                    instance,
                    resultType = null,
                    listOf(ElementDropInstruction(index.toUInt()))
                )
            }
        }

        for (index in module.data.indices) {
            val data = module.data[index]

            if (data is ActiveData) {
                execute(
                    stack,
                    interpreter,
                    instance,
                    resultType = null,
                    data.offset.body + listOf(
                        Int32ConstInstruction(0),
                        Int32ConstInstruction(data.initializers.size),
                        MemoryInitInstruction(index.toUInt()),
                        DataDropInstruction(index.toUInt())
                    )
                )
            }
        }

        return instance
    }

    private fun execute(
        stack: Stack,
        interpreter: Interpreter,
        module: ModuleInstance,
        resultType: ValueType?,
        instructions: List<Instruction>
    ): Any {
        stack.pushInitializerFrame(
            module,
            resultType,
            instructions
        )

        interpreter.execute()

        return when (resultType) {
            NumberType.Float32 -> stack.popFloat32()
            NumberType.Float64 -> stack.popFloat64()
            NumberType.Int32 -> stack.popInt32()
            NumberType.Int64 -> stack.popInt64()
            ReferenceType.ExternalReference,
            ReferenceType.FunctionReference -> stack.popReference()
            VectorType.Vector128 -> stack.popVector128()
            null -> Unit
        }
    }
}
