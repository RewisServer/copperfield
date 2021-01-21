package dev.volix.rewinside.odyssey.common.copperfield.proto.converter

import com.google.protobuf.Struct
import dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent
import dev.volix.rewinside.odyssey.common.copperfield.convertMapToStruct
import dev.volix.rewinside.odyssey.common.copperfield.convertStructToMap
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.converter.MapConverter
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class MapToProtoStructConverter : Converter<Map<*, *>, Struct>(Map::class.java, Struct::class.java) {

    override fun toTheirs(value: Map<*, *>?, agent: CopperfieldAgent, ourType: Class<out Map<*, *>>, targetFormat: Class<Any>, field: Field?): Struct? {
        val map = agent.toTheirsWithConverter(value, ourType, MapConverter::class.java, targetFormat, field) ?: return null
        return convertMapToStruct(map)
    }

    override fun toOurs(value: Struct?, agent: CopperfieldAgent, ourType: Class<out Map<*, *>>, targetFormat: Class<Any>, field: Field?): Map<*, *>? {
        val map = if (value == null) null else convertStructToMap(value)
        return agent.toOursWithConverter(map, ourType, MapConverter::class.java, targetFormat, field)
    }

}
