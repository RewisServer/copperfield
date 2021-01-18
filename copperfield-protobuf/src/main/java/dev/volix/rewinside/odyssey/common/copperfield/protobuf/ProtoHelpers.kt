package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.protobuf.ListValue
import com.google.protobuf.NullValue
import com.google.protobuf.Struct
import com.google.protobuf.Value
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.MapToProtoStructConverter

/**
 * @author Benedikt Wüller
 */

/**
 * Recursively converts [map] values to [Value]s.
 * Returns a new map.
 */
fun convertMapToStruct(map: Map<*, *>): Struct {
    return Struct.newBuilder().putAllFields(
        map.mapKeys { (key, _) ->
            if (key !is String) throw IllegalStateException("Expected key of type string in map. Found ${key?.javaClass}.")
            return@mapKeys key
        }.mapValues { convertToValue(it.value) }
    ).build()
}

/**
 * Recursively converts the [struct] to a map.
 * Returns a new map.
 */
fun convertStructToMap(struct: Struct): Map<String, Any?> {
    return struct.fieldsMap.mapValues { (_, value) -> convertFromValue(value) }
}

/**
 * Recursively converts the [iterable] to a [ListValue].
 */
fun convertIterableToListValue(iterable: Iterable<*>): ListValue? {
    return ListValue.newBuilder().addAllValues(iterable.map(::convertToValue)).build()
}

/**
 * Recursively converts the [value] to an iterable.
 */
fun convertListValueToIterable(value: ListValue): Iterable<Any?> {
    return value.valuesList.map(::convertFromValue)
}

/**
 * Converts the [value] to a single [Value] or [Struct] (potentially recursively).
 */
fun convertToValue(value: Any?): Value {
    val builder = Value.newBuilder()
    when (value) {
        null -> builder.nullValue = NullValue.NULL_VALUE
        is Number -> builder.numberValue = value.toDouble()
        is String -> builder.stringValue = value
        is Boolean -> builder.boolValue = value
        is Struct -> builder.structValue = value
        is Map<*, *> -> builder.structValue = convertMapToStruct(value)
        is Iterable<*> -> builder.listValue = convertIterableToListValue(value)
        is Value -> return value
        else -> throw IllegalArgumentException("Invalid value ${value.javaClass}. See documentation of ${MapToProtoStructConverter::class.java}.")
    }
    return builder.build()
}

/**
 * Converts the [value] to an object of the corresponding type (potentially recursively).
 */
fun convertFromValue(value: Value): Any? {
    return when (value.kindCase) {
        Value.KindCase.NULL_VALUE -> null
        Value.KindCase.NUMBER_VALUE -> value.numberValue
        Value.KindCase.STRING_VALUE -> value.stringValue
        Value.KindCase.BOOL_VALUE -> value.boolValue
        Value.KindCase.STRUCT_VALUE -> convertStructToMap(value.structValue)
        Value.KindCase.LIST_VALUE -> convertListValueToIterable(value.listValue)
        else -> throw IllegalArgumentException("KindCase ${value.kindCase} is not supported.")
    }
}
