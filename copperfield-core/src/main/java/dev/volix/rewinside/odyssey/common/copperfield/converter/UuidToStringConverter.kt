package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
open class UuidToStringConverter : Converter<UUID, String>(UUID::class.java, String::class.java) {

    override fun toTheirs(value: UUID?, field: Field?, registry: Registry<*, *>, type: Class<out UUID>): String? {
        if (value == null) return null
        return value.toString()
    }

    override fun toOurs(value: String?, field: Field?, registry: Registry<*, *>, type: Class<out UUID>): UUID? {
        if (value == null) return null
        return UUID.fromString(value)
    }

}
