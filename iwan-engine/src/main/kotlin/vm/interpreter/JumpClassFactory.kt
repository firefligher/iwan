package dev.fir3.iwan.engine.vm.interpreter

import dev.fir3.iwan.engine.models.vm.InterpreterState
import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.engine.vm.instructions.InstructionExecutionContainer
import dev.fir3.iwan.engine.vm.instructions.InstructionExecutor
import dev.fir3.iwan.engine.vm.stack.Stack
import dev.fir3.iwan.io.common.ClassLoaderUtilities
import dev.fir3.iwan.io.wasm.models.instructions.Instruction
import dev.fir3.iwan.jvm.bytecode.GeneratedClassLoader
import dev.fir3.iwan.jvm.bytecode.JumpClassGenerator
import dev.fir3.iwan.jvm.models.bytecode.CommonIdentifiers
import dev.fir3.iwan.jvm.models.bytecode.JumpClass
import dev.fir3.iwan.jvm.models.bytecode.JumpTable
import dev.fir3.iwan.jvm.models.bytecode.JumpTargetGenerator
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import java.lang.reflect.Method
import kotlin.reflect.full.createInstance

object JumpClassFactory {
    private const val GET_STACK_METHOD_NAME_JVM = "getStack"
    private const val GET_STORE_METHOD_NAME_JVM = "getStore"
    private const val GET_INSTRUCTION_METHOD_NAME_JVM = "getInstruction"
    private const val INTERPRETER_STATE_LOCAL_INDEX = 2

    private val INSTRUCTION_NAME_JVM =
        Instruction::class.java.name.replace('.', '/')

    private val INTERPRETER_STATE_NAME_JVM =
        InterpreterState::class.java.name.replace('.', '/')

    private val JUMP_CLASS_NAME = JumpClassFactory::class.java.`package`.name +
            ".DynamicJumpClass"

    private val STACK_NAME_JVM = Stack::class.java.name.replace('.', '/')
    private val STACK_TYPE_JVM = "L$STACK_NAME_JVM;"
    private val STORE_NAME_JVM = Store::class.java.name.replace('.', '/')
    private val STORE_TYPE_JVM = "L$STORE_NAME_JVM;"
    private val UNSUPPORTED_OPERATION_EXCEPTION_NAME_JVM =
        UnsupportedOperationException::class.java.name.replace('.', '/')

    private val GET_STACK_METHOD_TYPE_JVM = "()$STACK_TYPE_JVM"
    private val GET_STORE_METHOD_TYPE_JVM = "()$STORE_TYPE_JVM"
    private val GET_INSTRUCTION_METHOD_TYPE_JVM = "()L$INSTRUCTION_NAME_JVM;"

    // Cache

    private var _cachedClass: JumpClass? = null

    fun create(): JumpClass {
        var cachedClass = _cachedClass

        if (cachedClass != null) {
            return cachedClass
        }

        val executors = queryExecutors()
        val jumpTargetGenerator = createJumpTargetGenerator(executors)
        val jumpTable = JumpTable(
            executors.mapValues { _ -> jumpTargetGenerator },
            jumpTargetGenerator
        )

        val generatedJumpClass = JumpClassGenerator
            .generate(JUMP_CLASS_NAME, jumpTable)

        cachedClass = GeneratedClassLoader
            .load(generatedJumpClass)
            .createInstance()

        _cachedClass = cachedClass
        return cachedClass
    }

    private fun createJumpTargetGenerator(executors: Map<Int, Method>) =
        JumpTargetGenerator { dst, returnLabel, target ->
            if (target == null) {
                generateDefaultTarget(dst)
            } else {
                // Executor must be not-null, because we assume that the
                // underlying JumpTable originates from the same source as the
                // executor map.

                generateIndexedTarget(dst, returnLabel, executors[target]!!)
            }
        }

    private fun generateDefaultTarget(dst: MethodVisitor) {
        dst.visitTypeInsn(
            Opcodes.NEW,
            UNSUPPORTED_OPERATION_EXCEPTION_NAME_JVM
        )

        dst.visitInsn(Opcodes.DUP)
        dst.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            UNSUPPORTED_OPERATION_EXCEPTION_NAME_JVM,
            CommonIdentifiers.CONSTRUCTOR_NAME_JVM,
            CommonIdentifiers.NO_ARGS_RETURN_VOID_DESCRIPTOR_JVM,
            false
        )

        dst.visitInsn(Opcodes.ATHROW)
    }

    private fun generateIndexedTarget(
        dst: MethodVisitor,
        returnLabel: Label,
        executor: Method
    ) {
        val executorParameterTypes = executor.parameterTypes
        var descriptor = "("

        for (parameterType in executorParameterTypes) {
            dst.visitVarInsn(Opcodes.ALOAD, INTERPRETER_STATE_LOCAL_INDEX)
            dst.visitTypeInsn(Opcodes.CHECKCAST, INTERPRETER_STATE_NAME_JVM)

            if (parameterType.isCompatible<Stack>()) {
                dst.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    INTERPRETER_STATE_NAME_JVM,
                    GET_STACK_METHOD_NAME_JVM,
                    GET_STACK_METHOD_TYPE_JVM,
                    false
                )

                descriptor += STACK_TYPE_JVM
                continue
            }

            if (parameterType.isCompatible<Store>()) {
                dst.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    INTERPRETER_STATE_NAME_JVM,
                    GET_STORE_METHOD_NAME_JVM,
                    GET_STORE_METHOD_TYPE_JVM,
                    false
                )

                descriptor += STORE_TYPE_JVM
                continue
            }

            if (parameterType.isCompatible<Instruction>()) {
                val instructionClassNameJvm = parameterType
                    .name
                    .replace('.', '/')

                dst.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    INTERPRETER_STATE_NAME_JVM,
                    GET_INSTRUCTION_METHOD_NAME_JVM,
                    GET_INSTRUCTION_METHOD_TYPE_JVM,
                    false
                )

                dst.visitTypeInsn(
                    Opcodes.CHECKCAST,
                    instructionClassNameJvm
                )

                descriptor += "L$instructionClassNameJvm;"
                continue
            }

            throw IllegalStateException(
                "Instruction executor specifies parameter with " +
                        "unsupported type: " + parameterType.name
            )
        }

        descriptor += ")V"

        dst.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            executor.declaringClass.name.replace('.', '/'),
            executor.name,
            descriptor,
            false
        )

        dst.visitInsn(Opcodes.ACONST_NULL)
        dst.visitJumpInsn(Opcodes.GOTO, returnLabel)
    }

    private fun queryExecutors() = ClassLoaderUtilities.queryClasses(
        InstructionExecutionContainer::class.java.classLoader,
        InstructionExecutionContainer::class.java.`package`.name
    ).filter(
        InstructionExecutionContainer::class.java::isAssignableFrom
    ).flatMap { containerClass ->
        containerClass.declaredMethods.mapNotNull { method ->
            val annotation = method.getAnnotation(
                InstructionExecutor::class.java
            ) ?: return@mapNotNull null

            Pair(annotation.uniqueInstructionId, method)
        }
    }.toMap()

    private inline fun <reified TType> Class<*>.isCompatible() =
        TType::class.java.isAssignableFrom(this)
}
