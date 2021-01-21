package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class MapConverter : Converter<Map<*, *>, Map<*, *>>(Map::class.java, Map::class.java), KeyAware, ValueAware {

    override fun toTheirs(value: Map<*, *>?, registry: Registry, ourType: Class<out Map<*, *>>, targetFormatType: Class<*>,
                          field: Field?): Map<*, *> {
        if (value == null) return mapOf<Any, Any>()

        val valueType = this.getValueType(field)
        val keyType = this.getKeyType(field)

        return value
            .mapKeys { registry.toTheirs(it, keyType, targetFormatType, field) }
            .mapValues { registry.toTheirs(it, valueType, targetFormatType, field) }
    }

    override fun toOurs(value: Map<*, *>?, registry: Registry, ourType: Class<out Map<*, *>>, targetFormatType: Class<*>, field: Field?): Map<*, *>? {
        if (value == null) return mapOf<Any, Any>()

        val valueType = this.getValueType(field)
        val keyType = this.getKeyType(field)

        return value
            .mapKeys { registry.toOurs(it, keyType, targetFormatType, field) }
            .mapValues { registry.toOurs(it, valueType, targetFormatType, field) }
    }

}
