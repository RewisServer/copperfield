package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
interface Converter<S : Any, T : Any> {

    fun convertFrom(name: String, source: S, field: Field, registry: ConverterRegistry<S>): T?

    fun convertTo(name: String, value: T?, target: S, field: Field, registry: ConverterRegistry<S>)

}
