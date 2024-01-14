package dev.fir3.iwan.engine.vm.instructions

@Target(AnnotationTarget.FUNCTION)
annotation class InstructionExecutor(val uniqueInstructionId: Int)
