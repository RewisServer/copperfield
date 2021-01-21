package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
class UuidToStringConverter : Converter<UUID, String>(UUID::class.java, String::class.java) {

    override fun toTheirs(value: UUID?, registry: Registry, ourType: Class<out UUID>, targetFormatType: Class<*>, field: Field?): String? {
        return value?.toString()
    }

    override fun toOurs(value: String?, registry: Registry, ourType: Class<out UUID>, targetFormatType: Class<*>, field: Field?): UUID? {
        if (value == null) return null
        return UUID.fromString(value)
    }

}
