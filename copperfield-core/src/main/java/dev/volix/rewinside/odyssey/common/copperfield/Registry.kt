package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperIterableType
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperMapTypes
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.converter.ConvertibleConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.IterableConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.MapConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.NoOpConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.NumberConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.UuidToStringConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.ZonedDateTimeToStringConverter
import java.lang.reflect.Field
import java.time.ZonedDateTime
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
abstract class Registry<OurType : Convertable, TheirType : Any>(private val ourType: Class<OurType>, private val theirType: Class<TheirType>) {

    companion object {
        private val FALLBACK_CONVERTER = NoOpConverter()
    }

    private val defaultConverters = mutableMapOf<Class<*>, Converter<*, *>>()
    private val converters = mutableMapOf<Class<out Converter<*, *>>, Converter<*, *>>()

    init {
        // Register default converters.
        this.setConverter(Number::class.java, NumberConverter())
        this.setConverter(Map::class.java, MapConverter())
        this.setConverter(Iterable::class.java, IterableConverter())
        this.setConverter(UUID::class.java, UuidToStringConverter())
        this.setConverter(ZonedDateTime::class.java, ZonedDateTimeToStringConverter())
    }

    fun setConverter(type: Class<*>, converter: Class<Converter<*, *>>) {
        this.setConverter(type, converter.newInstance())
    }

    fun setConverter(type: Class<*>, converter: Converter<*, *>) {
        this.defaultConverters[type] = converter
        this.converters[converter.javaClass] = converter
    }

    fun <T : Any> getConverterByValueType(type: Class<T>): Converter<Any, Any> {
        if (this.ourType.isAssignableFrom(type) && !this.defaultConverters.containsKey(type)) {
            this.setConverter(type, ConvertibleConverter(type as Class<out OurType>, this.theirType))
        }

        return (this.defaultConverters.entries.firstOrNull { (supportedType, _) ->
            return@firstOrNull supportedType.isAssignableFrom(type)
        }?.value ?: FALLBACK_CONVERTER) as Converter<Any, Any>
    }

    fun getConverterByConverterType(type: Class<out Converter<*, *>>) = this.converters.getOrPut(type, type::newInstance) as Converter<Any, Any>

    fun toTheirs(convertible: OurType?): TheirType? {
        if (convertible == null) return null
        val instance = this.createTheirs(convertible)

        this.findFields(convertible.javaClass).forEach {
            val annotation = it.getDeclaredAnnotation(CopperField::class.java)
            val name = this.getName(annotation.name, it)

            val converterType = annotation.converter
            val converter = this.getConverterByConverterType(converterType.java)

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

        this.findFields(type).forEach {
            val annotation = it.getDeclaredAnnotation(CopperField::class.java)
            val name = this.getName(annotation.name, it)

            val converterType = annotation.converter
            val converter = this.getConverterByConverterType(converterType.java)

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
            .filter { it.isAnnotationPresent(CopperField::class.java) }
            .filterNot { this.isIgnored(it) }
            .map { it.isAccessible = true; it }
    }

    protected open fun <T : OurType> createOurs(type: Class<out T>) = type.newInstance()

    protected open fun <T : TheirType> finalizeTheirs(instance: T): TheirType = instance

    protected abstract fun createTheirs(convertible: OurType): TheirType

    protected abstract fun readValue(name: String, entity: TheirType, type: Class<out Any>): Any?

    protected abstract fun writeValue(name: String, value: Any?, entity: TheirType, type: Class<out Any>)

    protected open fun getName(name: String, field: Field) = name

    protected open fun isIgnored(field: Field) = false

}
