package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.CopperConvertable
import dev.volix.rewinside.odyssey.common.copperfield.CopperFieldDefinition
import dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent
import dev.volix.rewinside.odyssey.common.copperfield.TypeMapper
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperFields
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperIgnore
import dev.volix.rewinside.odyssey.common.copperfield.camelToSnakeCase
import dev.volix.rewinside.odyssey.common.copperfield.getAnnotation
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
abstract class CopperConvertableConverter<TheirType : Any>(theirType: Class<TheirType>) : Converter<CopperConvertable, TheirType>(
    CopperConvertable::class.java, theirType) {

    override fun toTheirs(
        value: CopperConvertable?, agent: CopperfieldAgent, ourType: Class<out CopperConvertable>, targetFormat: Class<Any>,
        field: Field?): TheirType? {
        val instance = this.createTheirInstance(targetFormat as Class<out TheirType>, value?.javaClass)

        if (value != null) {
            value.onBeforeOursToTheirs()
            this.getCopperFields(ourType, this.getTheirMappedType(ourType, targetFormat)).forEach {
                val fieldValue = it.field.get(value)
                val fieldType = fieldValue?.javaClass ?: it.field.type
                val mappedType = if (it.typeMapper == null) fieldType else it.typeMapper.map(value, fieldType)
                val converter = agent.findConverter(mappedType, it.converter, targetFormat)
                val convertedValue = if (converter == null) fieldValue else agent.toTheirsWithConverter(fieldValue, mappedType, converter, targetFormat as Class<Any>, it.field)

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

    override fun toOurs(value: TheirType?, agent: CopperfieldAgent, ourType: Class<out CopperConvertable>, targetFormat: Class<Any>,
                        field: Field?): CopperConvertable? {
        val instance = this.createOurInstance(ourType)
        instance.onBeforeTheirsToOurs()

        if (value != null) {
            this.getCopperFields(ourType, this.getTheirMappedType(ourType, targetFormat)).forEach {
                val fieldType = it.field.type
                val mappedType = if (it.typeMapper == null) fieldType else it.typeMapper.map(instance, fieldType)
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
                val converterType = this.getConverterType((annotation?.converter?.java ?: Converter::class.java) as Class<Converter<Any, Any>>, it)

                val typeMapperType = this.getTypeMapper((annotation?.typeMapper?.java ?: TypeMapper::class.java) as Class<TypeMapper<out CopperConvertable, CopperConvertable>>, it)
                val typeMapper = if (typeMapperType == TypeMapper::class.java) null else typeMapperType.newInstance()

                return@map CopperFieldDefinition(it, this.getName(name, it), converterType, typeMapper as TypeMapper<CopperConvertable, CopperConvertable>?)
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

    protected open fun <T : CopperConvertable> createOurInstance(type: Class<T>) = type.newInstance()
    protected open fun finalizeOurInstance(instance: CopperConvertable) = instance

    protected abstract fun createTheirInstance(type: Class<out TheirType>, ourType: Class<out CopperConvertable>?): TheirType
    protected open fun finalizeTheirInstance(instance: TheirType) = instance

    protected abstract fun getValue(instance: TheirType, name: String, type: Class<*>): Any?
    protected abstract fun setValue(instance: TheirType, name: String, value: Any?, type: Class<*>)

    protected open fun getName(name: String, field: Field): String = name
    protected open fun getConverterType(type: Class<Converter<Any, Any>>, field: Field): Class<Converter<Any, Any>> = type
    protected open fun getTypeMapper(type: Class<TypeMapper<out CopperConvertable, CopperConvertable>>, field: Field): Class<TypeMapper<out CopperConvertable, CopperConvertable>> = type

    protected open fun getTheirMappedType(type: Class<out CopperConvertable>, targetFormat: Class<out Any>): Class<out Any> = targetFormat

}
