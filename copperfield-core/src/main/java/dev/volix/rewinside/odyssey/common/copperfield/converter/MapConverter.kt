package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class MapConverter : Converter<Map<*, *>, Map<*, *>>(Map::class.java, Map::class.java), KeyAware, ValueAware {

    override fun toTheirs(value: Map<*, *>?, agent: CopperfieldAgent, ourType: Class<out Map<*, *>>, targetFormat: Class<Any>,
                          field: Field?): Map<*, *> {
        if (value == null) return mapOf<Any, Any>()

        val valueType = this.getValueType(field)
        val keyType = this.getKeyType(field)

        return value
            .mapKeys { agent.toTheirs(it, keyType, targetFormat, field) }
            .mapValues { agent.toTheirs(it, valueType, targetFormat, field) }
    }

    override fun toOurs(value: Map<*, *>?, agent: CopperfieldAgent, ourType: Class<out Map<*, *>>, targetFormat: Class<Any>, field: Field?): Map<*, *>? {
        if (value == null) return mapOf<Any, Any>()

        val valueType = this.getValueType(field)
        val keyType = this.getKeyType(field)

        return value
            .mapKeys { agent.toOurs(it, keyType, targetFormat, field) }
            .mapValues { agent.toOurs(it, valueType, targetFormat, field) }
    }

}
