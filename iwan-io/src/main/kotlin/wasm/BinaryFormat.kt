package dev.fir3.iwan.io.wasm

import dev.fir3.iwan.io.serialization.DeserializationContext
import dev.fir3.iwan.io.serialization.DeserializationContextBuilder
import dev.fir3.iwan.io.serialization.deserialize
import dev.fir3.iwan.io.serialization.register
import dev.fir3.iwan.io.source.ByteSource
import dev.fir3.iwan.io.wasm.models.Module
import dev.fir3.iwan.io.wasm.serialization.*
import dev.fir3.iwan.io.wasm.serialization.instructions.*
import dev.fir3.iwan.io.wasm.serialization.instructions.BlockTypeInstructionStrategy
import dev.fir3.iwan.io.wasm.serialization.instructions.CallInstructionStrategy
import dev.fir3.iwan.io.wasm.serialization.instructions.ConstInstructionStrategy
import dev.fir3.iwan.io.wasm.serialization.instructions.InstructionStrategy
import dev.fir3.iwan.io.wasm.serialization.instructions.blockTypes.BlockTypeStrategy
import dev.fir3.iwan.io.wasm.serialization.sections.*
import dev.fir3.iwan.io.wasm.serialization.valueTypes.NumberTypeStrategy
import dev.fir3.iwan.io.wasm.serialization.valueTypes.ReferenceTypeStrategy
import dev.fir3.iwan.io.wasm.serialization.valueTypes.ValueTypeStrategy
import dev.fir3.iwan.io.wasm.serialization.valueTypes.VectorTypeStrategy
import java.io.IOException

object BinaryFormat {
    private val _context: DeserializationContext

    init {
        val b = DeserializationContextBuilder()

        b.register(BlockTypeInstructionStrategy)
        b.register(BlockTypeStrategy)
        b.register(BranchInstructionStrategy)
        b.register(CallIndirectInstructionStrategy)
        b.register(CallInstructionStrategy)
        b.register(CodeSectionStrategy)
        b.register(CodeStrategy)
        b.register(CustomSectionStrategy)
        b.register(DataSectionStrategy)
        b.register(DataStrategy)
        b.register(ElementSectionStrategy)
        b.register(ElementSegmentStrategy)
        b.register(ExportSectionStrategy)
        b.register(ExportStrategy)
        b.register(ExpressionStrategy)
        b.register(FlatInstructionStrategy)
        b.register(FunctionSectionStrategy)
        b.register(FunctionTypeStrategy)
        b.register(GlobalSectionStrategy)
        b.register(GlobalStrategy)
        b.register(GlobalTypeStrategy)
        b.register(ImportSectionStrategy)
        b.register(ImportStrategy)
        b.register(ConstInstructionStrategy)
        b.register(InstructionStrategy)
        b.register(MemoryInstructionStrategy)
        b.register(MemorySectionStrategy)
        b.register(MemoryTypeStrategy)
        b.register(ModuleStrategy)
        b.register(NumberTypeStrategy)
        b.register(ReferenceTypeStrategy)
        b.register(SectionStrategy)
        b.register(TableSectionStrategy)
        b.register(TableTypeStrategy)
        b.register(TypeSectionStrategy)
        b.register(ValueTypeStrategy)
        b.register(VariableInstructionStrategy)
        b.register(VectorTypeStrategy)

        _context = b.build()
    }

    @Throws(IOException::class)
    fun deserializeModule(source: ByteSource) = _context
        .deserialize<Module>(source)
}
