package dev.volix.rewinside.odyssey.common.copperfield.typeconverter

import dev.volix.rewinside.odyssey.common.copperfield.Convertible
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class ConvertibleTypeConverter<T : Any, C : Convertible, R : Registry<T, C, R>>(ourType: Class<C>, theirType: Class<T>)
    : TypeConverter<R, C, T>(ourType, theirType) {

    override fun convertOursToTheirs(value: C?, field: Field, registry: R) = registry.write(value, this.theirType)

    override fun convertTheirsToOurs(value: T?, field: Field, registry: R) = registry.read(value, this.ourType)

}
