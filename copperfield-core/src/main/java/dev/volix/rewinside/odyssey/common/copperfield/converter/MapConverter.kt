package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperMapTypes
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
open class MapConverter : Converter<Map<*, *>, Map<*, *>>(Map::class.java, Map::class.java) {

    override fun toTheirs(value: Map<*, *>?, field: Field?, registry: Registry<*, *>, type: Class<out Map<*, *>>): Map<*, *>? {
        if (value == null) return mapOf<Any, Any>()
        val converters = this.getConverters(field, registry)
        return value
            .mapKeys { converters.first.second.toTheirs(it.key, null, registry, converters.first.first) }
            .mapValues { converters.second.second.toTheirs(it.value, null, registry, converters.second.first) }
    }

    override fun toOurs(value: Map<*, *>?, field: Field?, registry: Registry<*, *>, type: Class<out Map<*, *>>): Map<*, *>? {
        if (value == null) return mapOf<Any, Any>()
        val converters = this.getConverters(field, registry)
        return value
            .mapKeys { converters.first.second.toOurs(it.key, null, registry, converters.first.first) }
            .mapValues { converters.second.second.toOurs(it.value, null, registry, converters.second.first) }
    }

    private fun getConverters(field: Field?, registry: Registry<*, *>): Pair<Pair<Class<*>, Converter<Any, Any>>, Pair<Class<*>, Converter<Any, Any>>> {
        val annotation = when (field) {
            null -> null
            else -> field.getDeclaredAnnotation(CopperMapTypes::class.java)
                ?: throw IllegalStateException("Maps using the default MapConverter must annotate @CopperMapTypes: ${field.name}")
        }

        val keyType = annotation?.keyType?.java ?: Any::class.java
        val keyConverter = if (annotation != null) {
            registry.getConverterByConverterType(annotation.keyConverter.java)
        } else {
            registry.getConverterByValueType(keyType)
        }

        val valueType = annotation?.valueType?.java ?: Any::class.java
        val valueConverter = if (annotation != null) {
            registry.getConverterByConverterType(annotation.valueConverter.java)
        } else {
            registry.getConverterByValueType(valueType)
        }

        return (keyType to keyConverter) to (valueType to valueConverter)
    }

}
