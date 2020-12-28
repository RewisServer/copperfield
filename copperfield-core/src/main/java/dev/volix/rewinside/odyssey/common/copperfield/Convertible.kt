package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.exception.NoMatchingConverterException
import dev.volix.rewinside.odyssey.common.copperfield.exception.PrimitiveTypeException

/**
 * @author Benedikt Wüller
 */
interface Convertible<T : Any> {

    @JvmDefault
    fun findFields(registry: ConverterRegistry<T>): List<CopperFieldDefinition<T>> {
        return this.javaClass.declaredFields
            .filter { it.isAnnotationPresent(CopperField::class.java) }
            .map { field ->
                val annotation = field.getDeclaredAnnotation(CopperField::class.java)

                val name = annotation.name
                val type = field.type

                if (type.isPrimitive) throw PrimitiveTypeException(name, type)
                val converter = registry.findConverter(type) ?: throw NoMatchingConverterException(name, this.javaClass)

                return@map CopperFieldDefinition(field, name, converter as Converter<T, Any>)
            }.toList()
    }

}

