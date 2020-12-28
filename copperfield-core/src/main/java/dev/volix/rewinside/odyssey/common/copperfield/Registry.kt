package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperListField
import dev.volix.rewinside.odyssey.common.copperfield.typeconverter.ConvertibleTypeConverter
import dev.volix.rewinside.odyssey.common.copperfield.typeconverter.DummyTypeConverter
import dev.volix.rewinside.odyssey.common.copperfield.typeconverter.NumberTypeConverter
import dev.volix.rewinside.odyssey.common.copperfield.typeconverter.TypeConverter
import dev.volix.rewinside.odyssey.common.copperfield.typeconverter.UuidTypeConverter
import dev.volix.rewinside.odyssey.common.copperfield.typeconverter.ZonedDateTimeTypeConverter
import java.lang.reflect.Field
import java.time.ZonedDateTime
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
abstract class Registry<T : Any, C : Convertible, R : Registry<T, C, R>>(
    protected val type: Class<T>, protected val convertibleType: Class<C>) {

    private val typeConverters = mutableMapOf<Class<*>, TypeConverter<R, *, *>>()
    protected var defaultTypeConverter = DummyTypeConverter<T, C, R>()

    private val fieldCache = mutableMapOf<Pair<ConversionDirection, Class<*>>, Map<Field, String>>()

    init {
        this.setConverter(Number::class.java, NumberTypeConverter())
        this.setConverter(UUID::class.java, UuidTypeConverter())
        this.setConverter(ZonedDateTime::class.java, ZonedDateTimeTypeConverter())
    }

    fun <OURS : Any, THEIRS : Any> setConverter(type: Class<OURS>, converter: TypeConverter<R, OURS, THEIRS>) {
        this.typeConverters[type] = converter as TypeConverter<R, *, *>
    }

    fun <OURS : Any> removeConverter(type: Class<OURS>) = this.typeConverters.remove(type)

    fun <OURS : Any> getConverter(type: Class<OURS>): TypeConverter<R, Any, Any> {
        return (this.typeConverters.entries.firstOrNull { (supportedType, _) ->
            return@firstOrNull supportedType.isAssignableFrom(type)
        }?.value ?:
            if (this.convertibleType.isAssignableFrom(type)) {
                val converter = this.createConvertibleTypeConverter(type as Class<C>)
                this.setConverter(type, converter as TypeConverter<R, C, R>)
                converter
            } else {
                this.defaultTypeConverter
            }
        ) as TypeConverter<R, Any, Any>
    }

    protected open fun <A : C> createConvertibleTypeConverter(type: Class<A>): ConvertibleTypeConverter<T, A, *> {
        return ConvertibleTypeConverter<T, A, R>(type, this.type)
    }

    protected fun convertOurListToTheirs(list: List<*>, field: Field): List<*> {
        val annotation = field.getDeclaredAnnotation(CopperListField::class.java)
            ?: throw IllegalStateException("Trying to convert list without @CopperListField annotation: ${field.name}")

        return list.map {
            val converter = this.getConverter(annotation.innerType.java)
            return@map converter.tryConvertOursToTheirs(it, field, this as R)
        }
    }

    protected fun convertTheirListToOurs(list: List<*>, field: Field): List<*> {
        val annotation = field.getDeclaredAnnotation(CopperListField::class.java)
            ?: throw IllegalStateException("Trying to convert list without @CopperListField annotation: ${field.name}")

        return list.map {
            val converter = this.getConverter(annotation.innerType.java)
            return@map converter.tryConvertTheirsToOurs(it, field, this as R)
        }
    }

    abstract fun <A : T> write(entity: C?, type: Class<A>): A?

    abstract fun <A : C> read(entity: T?, type: Class<A>): A?

    protected open fun <A : C> getFields(type: Class<A>, direction: ConversionDirection): Map<Field, String> {
        val key = Pair(direction, type)
        return this.fieldCache.getOrPut(key) {
            val fields = type.declaredFields
                .filter { this.isAnnotated(it) }
                .filterNot { this.isIgnored(it, direction) }

            val map = mutableMapOf<Field, String>()
            fields.forEach { field ->
                field.isAccessible = true

                val name = this.getName(field)
                if (name.isNullOrEmpty()) throw IllegalStateException("Serialized name for field \"${field.name}\" in $type is empty. " +
                        "Set the serialized name using @CopperField or @CopperBsonField. " +
                        "Alternatively, disable this field for Bson serialization using @CopperBsonIgnore.")

                val isList = List::class.java.isAssignableFrom(field.type)
                val hasListAnnotation = field.isAnnotationPresent(CopperListField::class.java)
                if (isList && !hasListAnnotation) {
                    throw IllegalStateException("Field \"${field.name}\" in $type is a list but does not annotate @CopperListField.")
                } else if (!isList && hasListAnnotation) {
                    throw IllegalStateException("Field \"${field.name}\" annotates @CopperListField but is not a list.")
                }

                map[field] = name
            }

            return@getOrPut map
        }
    }

    protected open fun isIgnored(field: Field, direction: ConversionDirection) = false

    protected open fun isAnnotated(field: Field) = field.isAnnotationPresent(CopperField::class.java) || field.isAnnotationPresent(CopperListField::class.java)

    protected open fun getName(field: Field): String? {
        var name = field.getDeclaredAnnotation(CopperListField::class.java)?.name
        if (name.isNullOrEmpty()) {
            name = field.getDeclaredAnnotation(CopperField::class.java)?.name
        }
        return name
    }

    fun initializeInsertionProcess() {
        println("Dick has been planted.")
    }

}
