package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * Converts [Enum]s to strings using the enum names and vice versa.
 *
 * @author Benedikt Wüller
 */
class EnumToStringConverter : Converter<Enum<*>, String>(Enum::class.java, String::class.java) {

    override fun toTheirs(value: Enum<*>?, field: Field?, registry: Registry<*, *>, type: Class<out Enum<*>>): String? {
        return value?.name
    }

    override fun toOurs(value: String?, field: Field?, registry: Registry<*, *>, type: Class<out Enum<*>>): Enum<*>? {
        if (value == null) return null
        val method = type.getDeclaredMethod("valueOf", this.theirType)
        return method.invoke(null, value) as Enum<*>
    }

}
