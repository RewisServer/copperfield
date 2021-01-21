package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class IterableConverter : Converter<Iterable<*>, Iterable<*>>(Iterable::class.java, Iterable::class.java), ValueAware {

    override fun toTheirs(
        value: Iterable<*>?, agent: CopperfieldAgent, ourType: Class<out Iterable<*>>, targetFormat: Class<Any>,
        field: Field?): Iterable<*>? {
        val valueType = this.getValueType(field)
        return value?.map { agent.toTheirs(it, valueType, targetFormat, field) }
    }

    override fun toOurs(
        value: Iterable<*>?, agent: CopperfieldAgent, ourType: Class<out Iterable<*>>, targetFormat: Class<Any>,
        field: Field?): Iterable<*>? {
        val valueType = this.getValueType(field)
        return value?.map { agent.toOurs(it, valueType, targetFormat, field) }
    }

}
