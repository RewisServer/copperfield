package dev.volix.rewinside.odyssey.common.copperfield.converter

/**
 * @author Benedikt Wüller
 */
interface Converter<S : Any, T : Any> {

    fun convertFrom(name: String, source: S): T?

    fun convertTo(name: String, value: T?, target: S)

}
