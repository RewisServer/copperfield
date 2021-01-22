package dev.volix.rewinside.odyssey.common.copperfield.registry

import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter

/**
 * @author Benedikt Wüller
 */
abstract class Registry {

    private val defaultConverterTypes = mutableMapOf<Class<*>, Class<out Converter<*, *>>>()
    private val converterTypes = mutableMapOf<Class<*>, MutableMap<Class<*>, Class<out Converter<*, *>>>>()
    private val converters = mutableMapOf<Class<out Converter<*, *>>, Converter<*, *>>()

    @JvmOverloads
    open fun with(ourType: Class<*>, converterType: Class<out Converter<*, *>>, targetFormat: Class<*>? = null): Registry {
        return this.with(ourType, this.getConverterByType(converterType), targetFormat)
    }

    @JvmOverloads
    open fun with(ourType: Class<*>, converter: Converter<out Any, out Any>, targetFormat: Class<*>? = null): Registry {
        converters[converter.javaClass] = converter
        if (targetFormat == null) {
            this.defaultConverterTypes[ourType] = converter.javaClass
        } else {
            this.converterTypes.getOrPut(targetFormat) { mutableMapOf() }[ourType] = converter.javaClass
        }
        return this
    }

    open fun with(other: Registry): Registry {
        this.defaultConverterTypes.putAll(other.defaultConverterTypes)
        other.converterTypes.forEach { (ourType, converterTypes) -> this.converterTypes.getOrPut(ourType) { mutableMapOf() }.putAll(converterTypes) }
        this.converters.putAll(other.converters)
        return this
    }

    @JvmOverloads
    open fun without(ourType: Class<*>, targetFormat: Class<*>? = null): Registry {
        if (targetFormat == null) {
            this.defaultConverterTypes.remove(ourType)
        } else {
            this.converterTypes[targetFormat]?.remove(ourType)
        }
        return this
    }

    protected open fun getConverterType(ourType: Class<*>, targetFormat: Class<*>? = null): Class<out Converter<out Any, out Any>>? {
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

    @Suppress("UNCHECKED_CAST")
    open fun <O : Any, T : Any> getConverterByValueType(ourType: Class<*>, targetFormat: Class<*>? = null): Converter<out O, out T>? {
        val converterType = this.getConverterType(ourType, targetFormat) ?: return null
        return this.getConverterByType(converterType as Class<out Converter<out O, out T>>)
    }

    @Suppress("UNCHECKED_CAST")
    open fun <O : Any, T : Any> getConverterByType(type: Class<out Converter<out O, out T>>): Converter<out O, out T> {
        return converters.getOrPut(type) { type.newInstance() } as Converter<out O, out T>
    }

}
