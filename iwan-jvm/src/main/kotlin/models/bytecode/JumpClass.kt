package dev.fir3.iwan.jvm.models.bytecode

fun interface JumpClass {
    fun evaluate(index: Int, userData: Any): Any
}
