package dev.volix.rewinside.odyssey.common.copperfield.proto.converter

import com.google.protobuf.Struct
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.convertMapToStruct
import dev.volix.rewinside.odyssey.common.copperfield.convertStructToMap
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.converter.MapConverter
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class MapToProtoStructConverter : Converter<Map<*, *>, Struct>(Map::class.java, Struct::class.java) {

    override fun toTheirs(value: Map<*, *>?, registry: Registry, ourType: Class<out Map<*, *>>, targetFormatType: Class<*>, field: Field?): Struct? {
        val map = registry.toTheirsWithConverter(value, ourType, MapConverter::class.java, targetFormatType, field) ?: return null
        return convertMapToStruct(map)
    }

    override fun toOurs(value: Struct?, registry: Registry, ourType: Class<out Map<*, *>>, targetFormatType: Class<*>, field: Field?): Map<*, *>? {
        val map = if (value == null) null else convertStructToMap(value)
        return registry.toOursWithConverter(map, ourType, MapConverter::class.java, targetFormatType, field)
    }

}
