package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.exception.NoMatchingConverterException

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

            //if (type.isPrimitive) throw PrimitiveTypeException(name, type)
            val converter = registry.findConverter(type) ?: throw NoMatchingConverterException(name, obj.javaClass)

            field.isAccessible = true
            return@map CopperFieldDefinition(field, name, converter as Converter<T, Any>)
        }.toList()
}

fun <T : Any> convertTo(instance: Any, target: T, registry: ConverterRegistry<T>, nameMapper: FieldNameMapper<*>, filter: FieldFilter<*>) {
    val fields = findFields(instance, registry)
    fields.filter { filter.filterSerialize(it.field) }.forEach {
        val converter = it.converter
        val name = nameMapper.map(it.field, it.name)
        val value = it.field.get(instance)
        converter.convertTo(name, value, target, it.field, registry)
    }
}

fun <T : Any> convertFrom(instance: Any, source: T, registry: ConverterRegistry<T>, nameMapper: FieldNameMapper<*>, filter: FieldFilter<*>) {
    val fields = findFields(instance, registry)
    fields.filter { filter.filterDeserialize(it.field) }.forEach {
        val converter = it.converter
        val name = nameMapper.map(it.field, it.name)
        val value = converter.convertFrom(name, source, it.field, registry)
        it.field.set(instance, value)
    }
}
