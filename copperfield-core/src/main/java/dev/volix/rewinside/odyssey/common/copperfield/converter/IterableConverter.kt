package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class IterableConverter : Converter<Iterable<*>, Iterable<*>>(Iterable::class.java, Iterable::class.java), ValueAware {

    override fun toTheirs(value: Iterable<*>?, registry: Registry, ourType: Class<out Iterable<*>>, targetFormatType: Class<*>,
                          field: Field?): Iterable<*>? {
        val valueType = this.getValueType(field)
        return value?.map { registry.toTheirs(it, valueType, targetFormatType, field) }
    }

    override fun toOurs(value: Iterable<*>?, registry: Registry, ourType: Class<out Iterable<*>>, targetFormatType: Class<*>,
                        field: Field?): Iterable<*>? {
        val valueType = this.getValueType(field)
        return value?.map { registry.toOurs(it, valueType, targetFormatType, field) }
    }

}
