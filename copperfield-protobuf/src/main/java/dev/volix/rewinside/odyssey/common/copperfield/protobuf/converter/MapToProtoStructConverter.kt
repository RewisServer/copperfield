package dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter

import com.google.protobuf.ListValue
import com.google.protobuf.NullValue
import com.google.protobuf.Struct
import com.google.protobuf.Value
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.converter.MapConverter
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class MapToProtoStructConverter : Converter<Map<*, *>, Struct>(Map::class.java, Struct::class.java) {

    private val converter = MapConverter()

    override fun toTheirs(value: Map<*, *>?, field: Field?, registry: Registry<*, *>, type: Class<out Map<*, *>>): Struct? {
        val map = this.converter.toTheirs(value, field, registry, type) ?: return null
        return this.convertMapToStruct(map)
    }

    override fun toOurs(value: Struct?, field: Field?, registry: Registry<*, *>, type: Class<out Map<*, *>>): Map<*, *>? {
        val map = if (value == null) null else this.convertStructToMap(value)
        return this.converter.toOurs(map, field, registry, type)
    }

    private fun convertMapToStruct(map: Map<*, *>): Struct {
        return Struct.newBuilder().putAllFields(
            map.mapKeys { (key, _) ->
                if (key !is String) throw IllegalStateException("Expected key of type string in map. Found ${key?.javaClass}.")
                return@mapKeys key
            }.mapValues { this.convertToValue(it.value) }
        ).build()
    }

    private fun convertStructToMap(struct: Struct): Map<String, Any?> {
        return struct.fieldsMap.mapValues { (_, value) -> this.convertFromValue(value) }
    }

    private fun convertIterableToListValue(iterable: Iterable<*>): ListValue? {
        return ListValue.newBuilder().addAllValues(iterable.map(this::convertToValue)).build()
    }

    private fun convertListValueToIterable(value: ListValue): Iterable<Any?> {
        return value.valuesList.map(this::convertFromValue)
    }

    private fun convertToValue(value: Any?): Value {
        val builder = Value.newBuilder()
        when (value) {
            null -> builder.nullValue = NullValue.NULL_VALUE
            is Number -> builder.numberValue = value.toDouble()
            is String -> builder.stringValue = value
            is Boolean -> builder.boolValue = value
            is Struct -> builder.structValue = value
            is Map<*, *> -> builder.structValue = this.convertMapToStruct(value)
            is Iterable<*> -> builder.listValue = this.convertIterableToListValue(value)
            else -> TODO("throw exception")
        }
        return builder.build()
    }

    private fun convertFromValue(value: Value): Any? {
        return when (value.kindCase) {
            Value.KindCase.NULL_VALUE -> null
            Value.KindCase.NUMBER_VALUE -> value.numberValue
            Value.KindCase.STRING_VALUE -> value.stringValue
            Value.KindCase.BOOL_VALUE -> value.boolValue
            Value.KindCase.STRUCT_VALUE -> this.convertStructToMap(value.structValue)
            Value.KindCase.LIST_VALUE -> this.convertListValueToIterable(value.listValue)
            else -> TODO("throw exception")
        }
    }

}
