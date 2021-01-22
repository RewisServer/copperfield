package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.template.BaseRegistry
import dev.volix.rewinside.odyssey.common.copperfield.template.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
open class CopperfieldAgent(vararg registries: Registry) {

    @Suppress("MemberVisibilityCanBePrivate")
    val registry: Registry = BaseRegistry()

    init {
        registries.forEach(this.registry::with)
    }

    @JvmOverloads
    fun <O : Any, T : Any> toTheirs(value: O, targetFormat: Class<out T>, field: Field? = null): T? {
        return this.toTheirs(value, value.javaClass, targetFormat, field)
    }

    @Suppress("UNCHECKED_CAST")
    @JvmOverloads
    fun <O : Any, T : Any> toTheirs(value: O?, ourType: Class<out O>, targetFormat: Class<out T>, field: Field? = null): T? {
        val converter = this.findConverter<O, T>(ourType, targetFormat)

        if (converter == null) {
            if (value == null) return null
            if (targetFormat.isAssignableFrom(value.javaClass)) return value as T
            throw IllegalArgumentException("" +
                    "No converter could be found for the given value with ourType ${ourType.name} " +
                    "and the value does not match the targetFormat ${targetFormat.name}."
            )
        }

        return this.toTheirsWithConverter(value, ourType, converter, targetFormat, field)
    }

    @JvmOverloads
    fun <O : Any, T : Any> toTheirsWithConverter(value: O?, ourType: Class<out O>, converterType: Class<out Converter<out O, out T>>, targetFormat: Class<out Any>, field: Field? = null): T? {
        return this.toTheirsWithConverter(
            value, ourType,
            this.registry.getConverterByType(converterType),
            targetFormat, field
        )
    }

    @Suppress("UNCHECKED_CAST")
    @JvmOverloads
    fun <O : Any, T : Any> toTheirsWithConverter(value: O?, ourType: Class<out O>, converter: Converter<out O, out T>, targetFormat: Class<out Any>, field: Field? = null): T? {
        return (converter as Converter<O, T>).toTheirs(value, this, ourType, targetFormat, field)
    }

    @Suppress("UNCHECKED_CAST")
    @JvmOverloads
    fun <O : Any, T : Any> toOurs(value: T?, ourType: Class<out O>, targetFormat: Class<out T>, field: Field? = null): O? {
        val converter = this.findConverter<O, T>(ourType, targetFormat)

        if (converter == null) {
            if (value == null) return null
            if (ourType.isAssignableFrom(value.javaClass)) return value as O
            throw IllegalArgumentException("" +
                    "No converter could be found for the given value with ourType ${ourType.name} " +
                    "and the value does not match ourType ${ourType.name}."
            )
        }

        return this.toOursWithConverter(value, ourType, converter, targetFormat, field)
    }

    @JvmOverloads
    fun <O : Any, T : Any> toOursWithConverter(value: T?, ourType: Class<out O>, converterType: Class<out Converter<out O, out T>>, targetFormat: Class<out Any>, field: Field? = null): O? {
        return this.toOursWithConverter(
            value, ourType, this.registry.getConverterByType(converterType),
            targetFormat, field
        )
    }

    @Suppress("UNCHECKED_CAST")
    @JvmOverloads
    fun <O : Any, T : Any> toOursWithConverter(value: T?, ourType: Class<out O>, converter: Converter<out O, out T>, targetFormat: Class<out Any>, field: Field? = null): O? {
        return (converter as Converter<O, T>).toOurs(value, this, ourType, targetFormat, field)
    }

    fun findConverter(type: Class<*>, converterType: Class<out Converter<out Any, out Any>>, targetFormat: Class<*>): Converter<out Any, out Any>? {
        // If the given converter type is the base interface, this is an indicator for using the best converter.
        if (converterType == Converter::class.java) {
            return this.findConverter<Any, Any>(type, targetFormat)
        }

        return this.registry.getConverterByType(converterType)
    }

    private fun <O : Any, T : Any> findConverter(type: Class<out O>, targetFormat: Class<out Any>): Converter<out O, out T>? {
        return this.registry.getConverterByValueType(type, targetFormat)
    }

}
