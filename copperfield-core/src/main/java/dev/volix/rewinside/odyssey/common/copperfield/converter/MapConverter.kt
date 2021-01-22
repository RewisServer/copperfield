package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent
import dev.volix.rewinside.odyssey.common.copperfield.KeyAware
import dev.volix.rewinside.odyssey.common.copperfield.ValueAware
import java.lang.reflect.Field

/**
 * Converts [Map]s to a new [Map] with transformed keys and values based on the defined types in the optional
 * [dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperKeyType] and
 * [dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperValueType] annotations.
 *
 * TODO: Uses the configured converters or falls back to the default ones.
 *
 * @author Benedikt Wüller
 */
class MapConverter : Converter<Map<*, *>, Map<*, *>>(Map::class.java, Map::class.java), KeyAware, ValueAware {

    override fun toTheirs(
        value: Map<*, *>?, agent: CopperfieldAgent, ourType: Class<out Map<*, *>>, contextType: Class<out Any>,
        field: Field?): Map<*, *> {
        if (value == null) return mapOf<Any, Any>()

        val valueType = this.getValueType(field)
        val keyType = this.getKeyType(field)

        return value
            .mapKeys { agent.toTheirs(it, keyType, contextType, field) }
            .mapValues { agent.toTheirs(it, valueType, contextType, field) }
    }

    override fun toOurs(value: Map<*, *>?, agent: CopperfieldAgent, ourType: Class<out Map<*, *>>, contextType: Class<out Any>, field: Field?): Map<*, *> {
        if (value == null) return mapOf<Any, Any>()

        val valueType = this.getValueType(field)
        val keyType = this.getKeyType(field)

        return value
            .mapKeys { agent.toOurs(it, keyType, contextType, field) }
            .mapValues { agent.toOurs(it, valueType, contextType, field) }
    }

}
