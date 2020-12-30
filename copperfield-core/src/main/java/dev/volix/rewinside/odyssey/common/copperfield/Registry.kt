package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperIgnore
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperIterableType
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperMapTypes
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.converter.ConvertibleConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.CopperFieldOrAutoConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.IterableConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.MapConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.NoOpConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.NumberConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.UuidToStringConverter
import java.lang.reflect.Field
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
abstract class Registry<OurType : Convertable, TheirType : Any>(private val ourType: Class<OurType>, private val theirType: Class<TheirType>) {

    companion object {
        private val FALLBACK_CONVERTER = NoOpConverter()
    }

    private val annotations = LinkedHashMap<Class<out Annotation>, CopperFieldAnnotationHelper>()

    private val defaultConverters = LinkedHashMap<Class<*>, Converter<*, *>>()
    private val converters = mutableMapOf<Class<out Converter<*, *>>, Converter<*, *>>()

    init {
        // Register annotation.
        this.registerAnnotation(CopperField::class.java)

        // Register default converters.
        this.setConverter(Number::class.java, NumberConverter::class.java)
        this.setConverter(UUID::class.java, UuidToStringConverter::class.java)
        this.setConverter(Iterable::class.java, IterableConverter::class.java)
        this.setConverter(Map::class.java, MapConverter::class.java)
    }

    protected fun registerAnnotation(type: Class<out Annotation>) {
        this.annotations[type] = CopperFieldAnnotationHelper(type)
    }

    fun setConverter(type: Class<*>, converter: Class<out Converter<*, *>>) {
        this.setConverter(type, converter.newInstance())
    }

    fun setConverter(type: Class<*>, converter: Converter<*, *>) {
        this.defaultConverters[type] = converter
        this.converters[converter.javaClass] = converter
    }

    fun unsetConverter(type: Class<*>) {
        val converter = this.defaultConverters.remove(type) ?: return
        this.converters.remove(converter.javaClass)
    }

    fun <T : Any> getConverterByValueType(type: Class<T>): Converter<Any, Any> {
        if (this.ourType.isAssignableFrom(type) && !this.defaultConverters.containsKey(type)) {
            this.setConverter(type, ConvertibleConverter(type as Class<out OurType>, this.theirType))
        }

        return (this.defaultConverters.entries.lastOrNull { (supportedType, _) ->
            return@lastOrNull supportedType.isAssignableFrom(type)
        }?.value ?: FALLBACK_CONVERTER) as Converter<Any, Any>
    }

    fun getConverterByConverterType(type: Class<out Converter<*, *>>) = this.converters.getOrPut(type, type::newInstance) as Converter<Any, Any>

    fun toTheirs(convertible: OurType?): TheirType? {
        if (convertible == null) return null
        val instance = this.createTheirs(convertible)

        // TODO: cache

        this.findFields(convertible.javaClass).forEach {
            val name = this.getName(it)

            val converterType = this.getConverterType(it)
            val converter = this.getConverterByConverterType(converterType)

            val value = it.get(convertible)
            val convertedValue = converter.toTheirs(value, it, this, it.type)

            val isMap = it.isAnnotationPresent(CopperMapTypes::class.java) && Map::class.java.isAssignableFrom(it.type)
            val isIterable = it.isAnnotationPresent(CopperIterableType::class.java) && Iterable::class.java.isAssignableFrom(it.type)

            val writeType = when {
                isMap -> Map::class.java
                isIterable -> Iterable::class.java
                else -> convertedValue?.javaClass ?: converter.theirType
            }

            this.writeValue(name, convertedValue, instance, writeType)
        }

        return this.finalizeTheirs(instance)
    }

    fun <T : OurType> toOurs(entity: TheirType?, type: Class<T>): T? {
        if (entity == null) return null
        val instance = this.createOurs(type)

        // TODO: cache

        this.findFields(type).forEach {
            val name = this.getName(it)

            val converterType = this.getConverterType(it)
            val converter = this.getConverterByConverterType(converterType)

            val isMap = it.isAnnotationPresent(CopperMapTypes::class.java) && Map::class.java.isAssignableFrom(it.type)
            val isIterable = it.isAnnotationPresent(CopperIterableType::class.java) && Iterable::class.java.isAssignableFrom(it.type)

            val readType = when {
                isMap -> Map::class.java
                isIterable -> Iterable::class.java
                else -> converter.theirType
            }

            val value = this.readValue(name, entity, readType)
            val convertedValue = converter.toOurs(value, it, this, it.type)
            it.set(instance, convertedValue)
        }

        return instance
    }

    private fun findFields(type: Class<out OurType>): List<Field> {
        return type.declaredFields
            .filter(this::isAnnotated)
            .filterNot(this::isIgnored)
            .map { it.isAccessible = true; it }
    }

    protected open fun isAnnotated(field: Field) = this.annotations.keys.any(field::isAnnotationPresent)

    protected open fun isIgnored(field: Field): Boolean {
        val annotation = field.getDeclaredAnnotation(CopperIgnore::class.java) ?: return false
        return annotation.convertables.any { it.java.isAssignableFrom(this.ourType) }
    }

    protected open fun getName(field: Field): String {
        var name = ""
        this.annotations.entries.forEach { (type, helper) ->
            val annotation = field.getDeclaredAnnotation(type) ?: return@forEach
            val annotationName = helper.getName(annotation)
            if (annotationName.isEmpty()) return@forEach
            name = annotationName
        }
        if (name.isEmpty()) throw IllegalStateException("No valid name has been set for field: ${field.name}")
        return name
    }

    protected open fun getConverterType(field: Field): Class<Converter<Any, Any>> {
        var converterType: Class<Converter<Any, Any>>? = null
        this.annotations.entries.forEach { (type, helper) ->
            val annotation = field.getDeclaredAnnotation(type) ?: return@forEach
            val annotationConverterType = helper.getConverter(annotation)
            if (annotationConverterType == CopperFieldOrAutoConverter::class.java && converterType != null) return@forEach
            converterType = annotationConverterType
        }
        return converterType ?: throw IllegalStateException("Unable to find valid converter for field: ${field.name}")
    }

    protected open fun <T : OurType> createOurs(type: Class<out T>) = type.newInstance()

    protected open fun <T : TheirType> finalizeTheirs(instance: T): TheirType = instance

    protected abstract fun createTheirs(convertible: OurType): TheirType

    protected abstract fun readValue(name: String, entity: TheirType, type: Class<out Any>): Any?

    protected abstract fun writeValue(name: String, value: Any?, entity: TheirType, type: Class<out Any>)

}
