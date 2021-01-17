package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperIterableType
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperMapTypes
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import java.lang.reflect.Field

/**
 * Internal helper class to keep several information about a CopperField.
 *
 * @author Benedikt Wüller
 */
data class FieldDefinition(val field: Field, val name: String, val converter: Converter<Any, Any>) {

    val isMap = field.isAnnotationPresent(CopperMapTypes::class.java) && Map::class.java.isAssignableFrom(field.type)
    val isIterable = field.isAnnotationPresent(CopperIterableType::class.java) && Iterable::class.java.isAssignableFrom(field.type)

}
