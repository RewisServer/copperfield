package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.CopperConvertable
import dev.volix.rewinside.odyssey.common.copperfield.CopperFieldDefinition
import dev.volix.rewinside.odyssey.common.copperfield.CopperTypeMapper
import dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperFields
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperIgnore
import dev.volix.rewinside.odyssey.common.copperfield.helper.camelToSnakeCase
import dev.volix.rewinside.odyssey.common.copperfield.helper.getAnnotation
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
abstract class CopperConvertableConverter<T : Any>(theirType: Class<T>) : Converter<CopperConvertable, T>(CopperConvertable::class.java, theirType) {

    override fun toTheirs(
        value: CopperConvertable?, agent: CopperfieldAgent, ourType: Class<out CopperConvertable>, targetFormat: Class<out Any>,
        field: Field?): T? {
        val instance = this.createTheirInstance(targetFormat, value?.javaClass)

        if (value != null) {
            value.onBeforeOursToTheirs()
            this.getCopperFields(ourType, this.getTheirMappedType(ourType, targetFormat)).forEach {
                val fieldValue = it.field.get(value)
                val fieldType = fieldValue?.javaClass ?: it.field.type
                val mappedType = if (it.typeMapper == null) fieldType else it.typeMapper.mapType(value, fieldType)
                val converter = agent.findConverter(mappedType, it.converter, targetFormat)
                val convertedValue = if (converter == null) fieldValue else agent.toTheirsWithConverter(fieldValue, mappedType, converter, targetFormat, it.field)

                val writeType = when {
                    converter == null -> convertedValue?.javaClass ?: mappedType
                    Map::class.java.isAssignableFrom(converter.theirType) -> Map::class.java
                    Iterable::class.java.isAssignableFrom(converter.theirType) -> Iterable::class.java
                    else -> convertedValue?.javaClass ?: converter.theirType
                }

                this.setValue(instance, it.name, convertedValue, this.getNonPrimitiveType(writeType))
            }
            value.onAfterOursToTheirs()
        }

        return this.finalizeTheirInstance(instance)
    }

    override fun toOurs(
        value: T?, agent: CopperfieldAgent, ourType: Class<out CopperConvertable>, targetFormat: Class<out Any>,
        field: Field?): CopperConvertable? {
        val instance = this.createOurInstance(ourType)
        instance.onBeforeTheirsToOurs()

        if (value != null) {
            this.getCopperFields(ourType, this.getTheirMappedType(ourType, targetFormat)).forEach {
                val fieldType = it.field.type
                val mappedType = if (it.typeMapper == null) fieldType else it.typeMapper.mapType(instance, fieldType)
                val converter = agent.findConverter(mappedType, it.converter, targetFormat)

                val readType = when {
                    converter == null -> mappedType
                    Map::class.java.isAssignableFrom(converter.theirType) -> Map::class.java
                    Iterable::class.java.isAssignableFrom(converter.theirType) -> Iterable::class.java
                    else -> converter.theirType
                }

                val fieldValue = this.getValue(value, it.name, this.getNonPrimitiveType(readType))
                val convertedValue = (if (converter == null) {
                    fieldValue
                } else {
                    agent.toOursWithConverter(fieldValue, mappedType, converter, targetFormat, it.field)
                }) ?: return@forEach

                it.field.set(instance, convertedValue)
            }
        }

        val finalInstance = this.finalizeOurInstance(instance)
        finalInstance.onAfterTheirsToOurs()
        return finalInstance
    }

    private fun <T : Any> getCopperFields(type: Class<out T>, theirType: Class<out Any>): List<CopperFieldDefinition> {
        return this.getDeclaredFields(type)
            .filterNot { field ->
                val annotation = field.getAnnotation(CopperIgnore::class.java) ?: return@filterNot false
                return@filterNot annotation.types.any { it.java.isAssignableFrom(theirType) }
            }
            .map {
                it.isAccessible = true

                val annotation = it.getDeclaredAnnotation(CopperField::class.java)
                val name = if (annotation?.name?.isEmpty() != false) it.name.camelToSnakeCase() else annotation.name
                val converterType = this.getConverterType(annotation?.converter?.java ?: Converter::class.java, it)

                val typeMapperType = this.getTypeMapper(annotation?.typeMapper?.java ?: CopperTypeMapper::class.java, it)
                val typeMapper = if (typeMapperType == CopperTypeMapper::class.java) null else typeMapperType.newInstance()

                return@map CopperFieldDefinition(it, this.getName(name, it), converterType, typeMapper as CopperTypeMapper<CopperConvertable, CopperConvertable>?)
            }
            .sortedWith(Comparator { o1, o2 ->
                // First handle all fields with no requirements.
                if (o1.typeMapper == null && o2.typeMapper != null) {
                    return@Comparator -1
                } else if (o1.typeMapper != null && o2.typeMapper == null) {
                    return@Comparator 1
                } else if (o1.typeMapper == null && o2.typeMapper == null) {
                    return@Comparator 0
                }

                // Next, sort by requirements.
                if (o1.typeMapper!!.requires(o2.field)) {
                    return@Comparator 1
                } else if (o2.typeMapper!!.requires(o1.field)) {
                    return@Comparator -1
                }

                return@Comparator 0
            })
    }

    private fun getDeclaredFields(type: Class<*>): Collection<Field> {
        val fields = mutableListOf<Field>()
        var currentType: Class<*>? = type
        do {
            val declaresAllFields = getAnnotation(currentType!!, CopperFields::class.java) != null

            currentType.declaredFields
                .filter { declaresAllFields || it.isAnnotationPresent(CopperField::class.java) }
                .forEach { fields.add(it) }

            currentType = currentType.superclass
        } while (currentType != null)
        return fields
    }

    private fun getNonPrimitiveType(type: Class<*>): Class<*> {
        return when (type) {
            Byte::class.javaPrimitiveType -> Byte::class.javaObjectType
            Short::class.javaPrimitiveType -> Short::class.javaObjectType
            Int::class.javaPrimitiveType -> Int::class.javaObjectType
            Long::class.javaPrimitiveType -> Long::class.javaObjectType
            Float::class.javaPrimitiveType -> Float::class.javaObjectType
            Double::class.javaPrimitiveType -> Double::class.javaObjectType
            Number::class.javaPrimitiveType -> Number::class.javaObjectType
            else -> type
        }
    }

    protected open fun <T : CopperConvertable> createOurInstance(type: Class<T>): T = type.newInstance()
    protected open fun finalizeOurInstance(instance: CopperConvertable) = instance

    protected abstract fun createTheirInstance(type: Class<out Any>, ourType: Class<out CopperConvertable>?): T
    protected open fun finalizeTheirInstance(instance: T) = instance

    protected abstract fun getValue(instance: T, name: String, type: Class<*>): Any?
    protected abstract fun setValue(instance: T, name: String, value: Any?, type: Class<*>)

    protected open fun getName(name: String, field: Field): String = name
    protected open fun getConverterType(type: Class<out Converter<out Any, out Any>>, field: Field): Class<out Converter<out Any, out Any>> = type
    protected open fun getTypeMapper(type: Class<out CopperTypeMapper<out CopperConvertable, out CopperConvertable>>, field: Field): Class<out CopperTypeMapper<out CopperConvertable, out CopperConvertable>> = type

    protected open fun getTheirMappedType(type: Class<out CopperConvertable>, targetFormat: Class<out Any>): Class<out Any> = targetFormat

}
