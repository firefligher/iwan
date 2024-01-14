package dev.fir3.iwan.jvm.bytecode

import dev.fir3.iwan.jvm.models.bytecode.GeneratedClass
import kotlin.reflect.KClass

object GeneratedClassLoader : ClassLoader() {
    fun <TInterface : Any> load(
        generatedClass: GeneratedClass<TInterface>
    ): KClass<out TInterface> {
        val definedClass = defineClass(
            generatedClass.name,
            generatedClass.data,
            0,
            generatedClass.data.size
        )

        // NOTE:    There is nothing safe about this and since generate
        //          everything at runtime, there is also nothing that a
        //          compiler can verify.
        //          Thus, there is no way to circumvent an UNCHECKED_CAST here.

        @Suppress("UNCHECKED_CAST")
        return definedClass.kotlin as KClass<out TInterface>
    }
}
