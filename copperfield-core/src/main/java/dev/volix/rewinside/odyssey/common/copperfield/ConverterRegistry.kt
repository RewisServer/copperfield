package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter

/**
 * @author Benedikt Wüller
 */
abstract class ConverterRegistry<S : Any> {

    private val converters = mutableMapOf<Class<*>, Converter<S, *>>()
    protected var defaultConverter: Converter<S, Any>? = null

    fun <T : Any> registerConverter(type: Class<T>, converter: Converter<S, T>) {
        this.converters[type] = converter
    }

    fun <T : Any> findConverter(type: Class<T>) = this.converters.entries.firstOrNull { (supportedType, _) ->
        println("$type - $supportedType - ${supportedType.isAssignableFrom(type)}")
        return@firstOrNull supportedType.isAssignableFrom(type)
    }?.value as Converter<S, T>? ?: this.defaultConverter

}
