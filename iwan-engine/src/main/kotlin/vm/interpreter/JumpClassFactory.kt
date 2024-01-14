package dev.fir3.iwan.engine.vm.interpreter

import dev.fir3.iwan.engine.vm.Stack
import dev.fir3.iwan.engine.vm.Store
import dev.fir3.iwan.engine.vm.instructions.InstructionExecutionContainer
import dev.fir3.iwan.engine.vm.instructions.InstructionExecutor
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
    private const val INSTANCE_FIELD_JVM = "INSTANCE"
    private const val INSTRUCTION_LOCAL_INDEX = 2

    private val JUMP_CLASS_NAME = JumpClassFactory::class.java.`package`.name +
            ".DynamicJumpClass"

    private val STACK_NAME_JVM = Stack::class.java.name.replace('.', '/')
    private val STACK_TYPE_JVM = "L$STACK_NAME_JVM;"
    private val STORE_NAME_JVM = Store::class.java.name.replace('.', '/')
    private val STORE_TYPE_JVM = "L$STORE_NAME_JVM;"
    private val UNSUPPORTED_OPERATION_EXCEPTION_NAME_JVM =
        UnsupportedOperationException::class.java.name.replace('.', '/')

    fun create(): JumpClass {
        val executors = queryExecutors()
        val jumpTargetGenerator = createJumpTargetGenerator(executors)
        val jumpTable = JumpTable(
            executors.mapValues { _ -> jumpTargetGenerator },
            jumpTargetGenerator
        )

        val generatedJumpClass = JumpClassGenerator
            .generate(JUMP_CLASS_NAME, jumpTable)

        return GeneratedClassLoader
            .load(generatedJumpClass)
            .createInstance()
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
            if (parameterType.isCompatible<Stack>()) {
                dst.visitFieldInsn(
                    Opcodes.GETSTATIC,
                    STACK_NAME_JVM,
                    INSTANCE_FIELD_JVM,
                    STACK_TYPE_JVM
                )

                descriptor += STACK_TYPE_JVM
                continue
            }

            if (parameterType.isCompatible<Store>()) {
                dst.visitFieldInsn(
                    Opcodes.GETSTATIC,
                    STORE_NAME_JVM,
                    INSTANCE_FIELD_JVM,
                    STORE_TYPE_JVM
                )

                descriptor += STORE_TYPE_JVM
                continue
            }

            if (parameterType.isCompatible<Instruction>()) {
                val instructionClassNameJvm = parameterType
                    .name
                    .replace('.', '/')

                dst.visitVarInsn(Opcodes.ALOAD, INSTRUCTION_LOCAL_INDEX)
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
