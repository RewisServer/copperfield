package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class EnumToStringConverter : Converter<Enum<*>, String>(Enum::class.java, String::class.java) {

    override fun toTheirs(value: Enum<*>?, registry: Registry, ourType: Class<out Enum<*>>, targetFormatType: Class<*>, field: Field?): String? {
        return value?.name
    }

    override fun toOurs(value: String?, registry: Registry, ourType: Class<out Enum<*>>, targetFormatType: Class<*>, field: Field?): Enum<*>? {
        if (value == null) return null
        val method = ourType.getDeclaredMethod("valueOf", String::class.java)
        return method.invoke(null, value) as Enum<*>
    }

}
