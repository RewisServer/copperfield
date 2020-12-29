package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class AutoConverter : Converter<Any, Any>(Any::class.java, Any::class.java) {

    override fun toTheirs(value: Any?, field: Field?, registry: Registry<*, *>, type: Class<out Any>): Any? {
        val converter = registry.getConverterByValueType(type)
        return converter.toTheirs(value, field, registry, type)
    }

    override fun toOurs(value: Any?, field: Field?, registry: Registry<*, *>, type: Class<out Any>): Any? {
        val converter = registry.getConverterByValueType(type)
        return converter.toOurs(value, field, registry, type)
    }

}
