package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.exception.NoMatchingConverterException
import dev.volix.rewinside.odyssey.common.copperfield.exception.PrimitiveTypeException

/**
 * @author Benedikt Wüller
 */

// TODO: maybe add caching of converters (and allow it to be disabled)

fun <T : Any> findFields(obj: Any, registry: ConverterRegistry<T>): List<CopperFieldDefinition<T>> {
    return obj.javaClass.declaredFields
        .filter { it.isAnnotationPresent(CopperField::class.java) }
        .map { field ->
            val annotation = field.getDeclaredAnnotation(CopperField::class.java)

            val name = annotation.name
            val type = field.type

            if (type.isPrimitive) throw PrimitiveTypeException(name, type)
            val converter = registry.findConverter(type) ?: throw NoMatchingConverterException(name, obj.javaClass)

            field.isAccessible = true
            return@map CopperFieldDefinition(field, name, converter as Converter<T, Any>)
        }.toList()
}

fun <T : Any> convertTo(instance: Any, target: T, registry: ConverterRegistry<T>) {
    val fields = findFields(instance, registry)
    fields.forEach {
        val converter = it.converter
        val name = it.name
        val value = it.field.get(instance)
        converter.convertTo(name, value, target)
    }
}

fun <T : Any> convertFrom(instance: Any, source: T, registry: ConverterRegistry<T>) {
    val fields = findFields(instance, registry)
    fields.forEach {
        val converter = it.converter
        val name = it.name
        val value = converter.convertFrom(name, source)
        it.field.set(instance, value)
    }
}
