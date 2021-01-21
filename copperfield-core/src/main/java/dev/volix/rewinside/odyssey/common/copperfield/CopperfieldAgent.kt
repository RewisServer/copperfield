package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.template.BaseRegistry
import dev.volix.rewinside.odyssey.common.copperfield.template.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
open class CopperfieldAgent(vararg registries: Registry) {

    val registry: Registry = BaseRegistry()

    init {
        registries.forEach(this.registry::with)
    }

    @JvmOverloads
    fun <OurType : Any, TheirType : Any> toTheirs(value: OurType, targetFormat: Class<TheirType>, field: Field? = null): TheirType? {
        return this.toTheirs(value, value.javaClass, targetFormat, field)
    }

    @JvmOverloads
    fun <OurType : Any, TheirType : Any> toTheirs(value: OurType?, ourType: Class<out OurType>, targetFormat: Class<TheirType>, field: Field? = null): TheirType? {
        val converter = this.findConverter(ourType, targetFormat) ?: return value as TheirType?
        return this.toTheirsWithConverter(value, ourType, converter as Converter<OurType, TheirType>, targetFormat, field)
    }

    @JvmOverloads
    fun <OurType : Any, TheirType : Any> toTheirsWithConverter(value: OurType?, ourType: Class<out OurType>, converterType: Class<out Converter<OurType, TheirType>>,
                                                               targetFormat: Class<*>, field: Field? = null): TheirType? {
        return this.toTheirsWithConverter(
            value, ourType,
            this.registry.getConverterByType(converterType as Class<Converter<*, *>>) as Converter<OurType, TheirType>,
            targetFormat, field
        )
    }

    @JvmOverloads
    fun <OurType : Any, TheirType : Any> toTheirsWithConverter(value: OurType?, ourType: Class<out OurType>, converter: Converter<OurType, TheirType>,
                                                               targetFormat: Class<*>, field: Field? = null): TheirType? {
        return converter.toTheirs(value, this, ourType, targetFormat as Class<Any>, field)
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    @JvmOverloads
    fun <OurType : Any, TheirType : Any> toOurs(value: TheirType?, ourType: Class<out OurType>, targetFormat: Class<TheirType>, field: Field? = null): OurType? {
        val converter = this.findConverter(ourType, targetFormat) ?: return value as OurType?
        return this.toOursWithConverter(value, ourType, converter as Converter<OurType, TheirType>, targetFormat, field)
    }

    @JvmOverloads
    fun <OurType : Any, TheirType : Any> toOursWithConverter(value: TheirType?, ourType: Class<out OurType>, converterType: Class<out Converter<OurType, TheirType>>,
                                                             targetFormat: Class<*>, field: Field? = null): OurType? {
        return this.toOursWithConverter(
            value, ourType, this.registry.getConverterByType(converterType as Class<Converter<*, *>>) as Converter<OurType, TheirType>,
            targetFormat, field
        )
    }

    @JvmOverloads
    fun <OurType : Any, TheirType : Any> toOursWithConverter(value: TheirType?, ourType: Class<out OurType>, converter: Converter<OurType, TheirType>,
                                                             targetFormat: Class<*>, field: Field? = null): OurType? {
        return converter.toOurs(value, this, ourType, targetFormat as Class<Any>, field)
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    fun findConverter(type: Class<*>, converterType: Class<Converter<Any, Any>>, targetFormat: Class<*>): Converter<Any, Any>? {
        // If the given converter type is the base interface, this is an indicator for using the best converter.
        if (converterType == Converter::class.java) {
            return this.findConverter(type, targetFormat) as Converter<Any, Any>?
        }

        return this.registry.getConverterByType(converterType as Class<Converter<*, *>>) as Converter<Any, Any>
    }

    private fun <OurType : Any> findConverter(type: Class<OurType>, targetFormat: Class<*>): Converter<out OurType, *>? {
        return this.registry.getConverterByValueType(type, targetFormat) as Converter<out OurType, *>?
    }

}
