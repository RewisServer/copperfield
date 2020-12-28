package dev.volix.rewinside.odyssey.common.copperfield.typeconverter

import dev.volix.rewinside.odyssey.common.copperfield.Convertible
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class DummyTypeConverter<T : Any, C : Convertible, R : Registry<T, C, R>>
    : TypeConverter<R, Any, Any>(Any::class.java, Any::class.java) {

    override fun convertOursToTheirs(value: Any, field: Field, registry: R) = value

    override fun convertTheirsToOurs(value: Any, field: Field, registry: R) = value

}
