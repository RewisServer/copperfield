package dev.volix.rewinside.odyssey.common.copperfield.template

import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter

/**
 * @author Benedikt Wüller
 */
abstract class Registry {

    companion object {
        private val CONVERTERS = mutableMapOf<Class<out Converter<*, *>>, Converter<*, *>>()

        fun getConverter(type: Class<out Converter<*, *>>): Converter<*, *> {
            return CONVERTERS.getOrPut(type) { type.newInstance() }
        }
    }

    protected val defaultConverterTypes = mutableMapOf<Class<*>, Class<out Converter<*, *>>>()
    protected val converterTypes = mutableMapOf<Class<*>, MutableMap<Class<*>, Class<out Converter<*, *>>>>()

    @JvmOverloads
    fun with(ourType: Class<*>, converterType: Class<out Converter<*, *>>, targetFormat: Class<*>? = null): Registry {
        return this.with(ourType, Companion.getConverter(converterType), targetFormat)
    }

    @JvmOverloads
    fun with(ourType: Class<*>, converter: Converter<out Any, out Any>, targetFormat: Class<*>? = null): Registry {
        CONVERTERS[converter.javaClass] = converter
        if (targetFormat == null) {
            this.defaultConverterTypes[ourType] = converter.javaClass
        } else {
            this.converterTypes.getOrPut(targetFormat) { mutableMapOf() }[ourType] = converter.javaClass
        }
        return this
    }

    fun with(other: Registry): Registry {
        this.defaultConverterTypes.putAll(other.defaultConverterTypes)
        other.converterTypes.forEach { (ourType, converterTypes) -> this.converterTypes.getOrPut(ourType) { mutableMapOf() }.putAll(converterTypes) }
        return this
    }

    @JvmOverloads
    fun without(ourType: Class<*>, targetFormat: Class<*>? = null): Registry {
        if (targetFormat == null) {
            this.defaultConverterTypes.remove(ourType)
        } else {
            this.converterTypes[targetFormat]?.remove(ourType)
        }
        return this
    }

    fun getConverterType(ourType: Class<*>, targetFormat: Class<*>? = null): Class<out Converter<*, *>>? {
        val converters = mutableMapOf<Class<*>, Class<out Converter<*, *>>>()
        converters.putAll(this.defaultConverterTypes)

        if (targetFormat != null) {
            this.converterTypes.keys
                .filter { it.isAssignableFrom(targetFormat) }
                .forEach {
                    val converterTypes = this.converterTypes[it] ?: return@forEach
                    converters.putAll(converterTypes)
                }
        }

        // Sort by assignability to target format.
        val sorted = converters.entries
            .filter { it.key.isAssignableFrom(ourType) }
            .sortedWith(Comparator { o1, o2 ->
                if (o1.key.isAssignableFrom(o2.key)) {
                    return@Comparator -1
                } else if (o2.key.isAssignableFrom(o1.key)) {
                    return@Comparator 1
                }
                return@Comparator 0
            })

        return sorted.firstOrNull()?.value
    }

    fun getConverter(ourType: Class<*>, targetFormat: Class<*>? = null): Converter<*, *>? {
        val converterType = this.getConverterType(ourType, targetFormat) ?: return null
        return Registry.getConverter(converterType)
    }

}
