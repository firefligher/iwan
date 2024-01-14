package dev.fir3.iwan.jvm.bytecode

import dev.fir3.iwan.jvm.models.bytecode.CommonIdentifiers
import dev.fir3.iwan.jvm.models.bytecode.GeneratedClass
import dev.fir3.iwan.jvm.models.bytecode.JumpClass
import dev.fir3.iwan.jvm.models.bytecode.JumpTable
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes

object JumpClassGenerator {
    private const val EVALUATE_DESCRIPTOR_JVM =
        "(ILjava/lang/Object;)Ljava/lang/Object;"

    private val JUMP_CLASS_NAME_JVM = JumpClass::class
        .java
        .name
        .replace('.', '/')

    private val OBJECT_NAME_JVM = Object::class.java.name.replace('.', '/')

    fun generate(name: String, table: JumpTable): GeneratedClass<JumpClass> {
        val c = ClassWriter(
            ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS
        )

        val nameJVM = name.replace('.', '/')

        c.visit(
            Opcodes.V1_7,
            Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL,
            nameJVM,
            null,
            OBJECT_NAME_JVM,
            arrayOf(JUMP_CLASS_NAME_JVM)
        )

        generateConstructor(c)
        generateEvaluation(c, table)
        c.visitEnd()

        return GeneratedClass(name, c.toByteArray(), JumpClass::class)
    }

    private fun generateConstructor(dst: ClassWriter) {
        val m = dst.visitMethod(
            Opcodes.ACC_PUBLIC,
            CommonIdentifiers.CONSTRUCTOR_NAME_JVM,
            CommonIdentifiers.NO_ARGS_RETURN_VOID_DESCRIPTOR_JVM,
            null,
            null
        )

        m.visitCode()
        m.visitVarInsn(Opcodes.ALOAD, 0)
        m.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            OBJECT_NAME_JVM,
            CommonIdentifiers.CONSTRUCTOR_NAME_JVM,
            CommonIdentifiers.NO_ARGS_RETURN_VOID_DESCRIPTOR_JVM,
            false
        )

        m.visitInsn(Opcodes.RETURN)

        // Stack size and local number will be computed by the ASM library
        // internally.

        m.visitMaxs(0, 0)
        m.visitEnd()
    }

    private fun generateEvaluation(dst: ClassWriter, table: JumpTable) {
        val m = dst.visitMethod(
            Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL,
            JumpClass::evaluate.name,
            EVALUATE_DESCRIPTOR_JVM,
            null,
            null
        )

        m.visitCode()
        m.visitVarInsn(Opcodes.ILOAD, 1)

        val defaultLabel = Label()
        val returnLabel = Label()
        val sortedIndices = table.indexedEntries.keys.sorted()
        val sortedLabels = sortedIndices.map { Label() }

        m.visitTableSwitchInsn(
            sortedIndices.first(),
            sortedIndices.last(),
            defaultLabel,
            *sortedLabels.toTypedArray()
        )

        for (sortedIndex in sortedIndices.indices) {
            val targetIndex = sortedIndices[sortedIndex]
            val targetLabel = sortedLabels[sortedIndex]

            m.visitLabel(targetLabel)
            table.indexedEntries[targetIndex]!!.generate(
                m,
                returnLabel,
                targetIndex
            )
        }

        m.visitLabel(defaultLabel)
        table.defaultEntry.generate(m, returnLabel, null)

        m.visitLabel(returnLabel)
        m.visitInsn(Opcodes.ARETURN)

        // Stack size and local number will be computed by the ASM library
        // internally.

        m.visitMaxs(0, 0)
        m.visitEnd()
    }
}
