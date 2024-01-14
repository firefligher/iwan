package dev.fir3.iwan.jvm.models.bytecode

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor

fun interface JumpTargetGenerator {
    fun generate(dst: MethodVisitor, returnLabel: Label, target: Int?)
}
