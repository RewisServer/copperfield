package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
data class FieldDefinition(val field: Field, val name: String, val converter: Converter<Any, Any>,
                           val isMap: Boolean, val isIterable: Boolean)
