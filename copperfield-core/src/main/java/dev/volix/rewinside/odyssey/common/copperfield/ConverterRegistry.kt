package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter

/**
 * @author Benedikt Wüller
 */
open class ConverterRegistry<S : Any> {

    private val converters = mutableMapOf<Class<*>, Converter<S, *>>()

    fun <T : Any> registerConverter(type: Class<T>, converter: Converter<S, T>) {
        this.converters[type] = converter
    }

    fun <T : Any> findConverter(type: Class<T>) = this.converters.entries.firstOrNull { (supportedType, _) ->
        if (!supportedType.isAssignableFrom(type)) return@firstOrNull false
        return@firstOrNull true
    }?.value as Converter<S, T>?

}
