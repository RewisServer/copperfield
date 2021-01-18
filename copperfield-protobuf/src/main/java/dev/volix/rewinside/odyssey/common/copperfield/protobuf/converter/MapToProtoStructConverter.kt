package dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter

import com.google.protobuf.Struct
import com.google.protobuf.Value
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.converter.MapConverter
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.convertMapToStruct
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.convertStructToMap
import java.lang.reflect.Field

/**
 * Converts maps to/from [Struct]. The keys _must_ be a string. The following value types are supported:
 *   - [Number]
 *   - [String]
 *   - [Boolean]
 *   - [Map] following the same restrictions.
 *   - [Iterable] following the same restrictions (value types).
 *
 * Values of type [Value] and [Struct] are allowed as well but will be (recursively) converted to one
 * of the types above when converting [toOurs].
 *
 * If you need to support rich or custom key/value types, define a custom [Converter] using one of the
 * supported [Registry] annotations.
 *
 * @author Benedikt Wüller
 */
class MapToProtoStructConverter : Converter<Map<*, *>, Struct>(Map::class.java, Struct::class.java) {

    private val converter = MapConverter()

    override fun toTheirs(value: Map<*, *>?, field: Field?, registry: Registry<*, *>, type: Class<out Map<*, *>>): Struct? {
        val map = this.converter.toTheirs(value, field, registry, type) ?: return null
        return convertMapToStruct(map)
    }

    override fun toOurs(value: Struct?, field: Field?, registry: Registry<*, *>, type: Class<out Map<*, *>>): Map<*, *>? {
        val map = if (value == null) null else convertStructToMap(value)
        return this.converter.toOurs(map, field, registry, type)
    }

}
