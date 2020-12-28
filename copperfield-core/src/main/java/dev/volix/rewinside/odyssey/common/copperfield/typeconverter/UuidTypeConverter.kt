package dev.volix.rewinside.odyssey.common.copperfield.typeconverter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
class UuidTypeConverter<R : Registry<*, *, R>>
    : TypeConverter<R, UUID, String>(UUID::class.java, String::class.java) {

    override fun convertOursToTheirs(value: UUID, field: Field, registry: R) = value.toString()

    override fun convertTheirsToOurs(value: String, field: Field, registry: R): UUID = UUID.fromString(value)

}
