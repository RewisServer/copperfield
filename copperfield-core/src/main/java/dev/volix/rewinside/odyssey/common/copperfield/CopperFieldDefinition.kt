package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
data class CopperFieldDefinition<T : Any>(val field: Field, val name: String, val converter: Converter<T, Any>)
