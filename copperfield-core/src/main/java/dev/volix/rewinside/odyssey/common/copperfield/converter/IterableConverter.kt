package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent
import dev.volix.rewinside.odyssey.common.copperfield.ValueAware
import java.lang.reflect.Field

/**
 * Converts [Iterable]s to a new [Iterable] with transformed values based on the optional
 * [dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperValueType.type] annotation.
 *
 * TODO: Uses the configured converter or falls back to the default one.
 *
 * @author Benedikt Wüller
 */
class IterableConverter : Converter<Iterable<*>, Iterable<*>>(Iterable::class.java, Iterable::class.java), ValueAware {

    override fun toTheirs(value: Iterable<*>?, agent: CopperfieldAgent, ourType: Class<out Iterable<*>>, contextType: Class<out Any>, field: Field?): Iterable<*>? {
        val valueType = this.getValueType(field)
        return value?.map { agent.toTheirs(it, valueType, contextType, field) }
    }

    override fun toOurs(value: Iterable<*>?, agent: CopperfieldAgent, ourType: Class<out Iterable<*>>, contextType: Class<out Any>, field: Field?): Iterable<*>? {
        val valueType = this.getValueType(field)
        return value?.map { agent.toOurs(it, valueType, contextType, field) }
    }

}
