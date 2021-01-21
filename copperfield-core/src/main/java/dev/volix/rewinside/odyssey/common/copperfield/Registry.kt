package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.template.DefaultTemplate
import dev.volix.rewinside.odyssey.common.copperfield.template.Template
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
open class Registry(private val template: Template = DefaultTemplate()) {

    @JvmOverloads
    fun <OurType : Any> toTheirs(value: OurType?, ourType: Class<out OurType>, targetFormatType: Class<*> = Any::class.java,
                                 field: Field? = null): Any? {
        val converter = this.findConverter(ourType, targetFormatType) ?: return value
        return this.toTheirsWithConverter(value, ourType, converter as Converter<OurType, Any>, targetFormatType, field)
    }

    @JvmOverloads
    fun <OurType : Any, TheirType : Any> toTheirsWithConverter(value: OurType?, ourType: Class<out OurType>,
                                                               converterType: Class<out Converter<OurType, TheirType>>,
                                                               targetFormatType: Class<*> = Any::class.java, field: Field? = null): TheirType? {
        return this.toTheirsWithConverter(
            value, ourType,
            Template.getConverter(converterType as Class<Converter<*, *>>) as Converter<OurType, TheirType>,
            targetFormatType, field
        )
    }

    @JvmOverloads
    fun <OurType : Any, TheirType : Any> toTheirsWithConverter(value: OurType?, ourType: Class<out OurType>, converter: Converter<OurType, TheirType>,
                                                               targetFormatType: Class<*> = Any::class.java, field: Field? = null): TheirType? {
        return converter.toTheirs(value, this, ourType, targetFormatType, field)
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    @JvmOverloads
    fun <OurType : Any, TheirType : Any> toOurs(value: TheirType?, ourType: Class<out OurType>, targetFormatType: Class<*> = Any::class.java,
                                                field: Field? = null): Any? {
        val converter = this.findConverter(ourType, targetFormatType) ?: return value
        return this.toOursWithConverter(value, ourType, converter as Converter<OurType, TheirType>, targetFormatType, field)
    }

    @JvmOverloads
    fun <OurType : Any, TheirType : Any> toOursWithConverter(value: TheirType?, ourType: Class<out OurType>,
                                                             converterType: Class<out Converter<OurType, TheirType>>,
                                                             targetFormatType: Class<*> = Any::class.java, field: Field? = null): OurType? {
        return this.toOursWithConverter(
            value, ourType, Template.getConverter(converterType as Class<Converter<*, *>>) as Converter<OurType, TheirType>,
            targetFormatType, field
        )
    }

    @JvmOverloads
    fun <OurType : Any, TheirType : Any> toOursWithConverter(value: TheirType?, ourType: Class<out OurType>, converter: Converter<OurType, TheirType>,
                                                             targetFormatType: Class<*> = Any::class.java, field: Field? = null): OurType? {
        return converter.toOurs(value, this, ourType, targetFormatType, field)
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    fun findConverter(type: Class<*>, converterType: Class<Converter<Any, Any>>, targetFormatType: Class<*>): Converter<Any, Any>? {
        // If the given converter type is the base interface, this is an indicator for using the best converter.
        if (converterType == Converter::class.java) {
            return this.findConverter(type, targetFormatType) as Converter<Any, Any>?
        }

        return Template.getConverter(converterType as Class<Converter<*, *>>) as Converter<Any, Any>
    }

    private fun <OurType : Any> findConverter(type: Class<OurType>, targetFormatType: Class<*>): Converter<out OurType, *>? {
        return this.template.getConverter(type, targetFormatType) as Converter<out OurType, *>?
    }

}
