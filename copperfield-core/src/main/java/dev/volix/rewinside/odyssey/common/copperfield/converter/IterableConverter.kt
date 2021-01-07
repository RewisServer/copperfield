package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperIterableType
import java.lang.reflect.Field

/**
 * Converts [Iterable]s to a new [Iterable] with transformed values based on the defined [CopperIterableType.valueConverter].
 *
 * @author Benedikt Wüller
 */
open class IterableConverter : Converter<Iterable<*>, Iterable<*>>(Iterable::class.java, Iterable::class.java) {

    override fun toTheirs(value: Iterable<*>?, field: Field?, registry: Registry<*, *>, type: Class<out Iterable<*>>): Iterable<*> {
        val result = this.getConverter(field, registry)
        return value?.map { result.second.toTheirs(it, null, registry, result.first) } ?: listOf<Any>()
    }

    override fun toOurs(value: Iterable<*>?, field: Field?, registry: Registry<*, *>, type: Class<out Iterable<*>>): Iterable<*> {
        val result = this.getConverter(field, registry)
        return value?.map { result.second.toOurs(it, null, registry, result.first) } ?: listOf<Any>()
    }

    private fun getConverter(field: Field?, registry: Registry<*, *>) : Pair<Class<*>, Converter<Any, Any>> {
        val annotation = when (field) {
            null -> null
            else -> field.getDeclaredAnnotation(CopperIterableType::class.java)
                ?: throw IllegalStateException("Iterables using the default IterableConverter must annotate @CopperIterableType: ${field.name}")
        }

        val type = annotation?.valueType?.java ?: Any::class.java
        val converter = if (annotation != null) {
            registry.getConverterByConverterType(annotation.valueConverter.java)
        } else {
            registry.getConverterByValueType(type)
        }

        return type to converter
    }

}
