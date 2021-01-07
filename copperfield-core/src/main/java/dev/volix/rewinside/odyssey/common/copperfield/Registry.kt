package dev.volix.rewinside.odyssey.common.copperfield

import com.google.common.cache.CacheBuilder
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperIgnore
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.converter.ConvertibleConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.FallbackAutoConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.IterableConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.MapConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.NoOperationConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.NumberConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.UuidToStringConverter
import java.lang.reflect.Field
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * Allows you to convert [Convertable]s of [OurType] to objects of [TheirType] and vice versa.
 *
 * All fields annotated with [CopperField] will be transformed to fields of [TheirType] as long as [OurType]
 * is not present in the optional [CopperIgnore] annotation. Inheriting registries may support additional
 * or alternative annotations to set/override [CopperField.name] or [CopperField.converter] based on the
 * target format.
 *
 * Registries provide the functionality of replacing and adding default [Converter]s for given object types.
 * The following converters are registered by default (inheriting registries may override standard converters):
 *   - [Number] using [NumberConverter]
 *   - [UUID] using [UuidToStringConverter]
 *   - [Iterable] using [IterableConverter]
 *   - [Map] using [MapConverter]
 *
 * @author Benedikt Wüller
 */
abstract class Registry<OurType : Convertable, TheirType : Any>(private val ourType: Class<OurType>, private val theirType: Class<TheirType>) {

    companion object {
        private val FALLBACK_CONVERTER = NoOperationConverter()
    }

    /**
     * Supported annotations. All annotations _must_ at least follow the signature of [CopperField].
     */
    private val annotations = LinkedHashMap<Class<out Annotation>, CopperFieldAnnotationHelper>()

    /**
     * Registered converters by field supertypes.
     */
    private val defaultConverters = LinkedHashMap<Class<*>, Converter<*, *>>()

    /**
     * Registered converters by converter type.
     */
    private val converters = mutableMapOf<Class<out Converter<*, *>>, Converter<*, *>>()

    /**
     * Cached field definitions by [OurType] subclasses.
     */
    private val fieldDefinitionCache = CacheBuilder.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .build<Class<*>, List<FieldDefinition>>()

    init {
        // Register annotation.
        this.registerAnnotation(CopperField::class.java)

        // Register default converters.
        this.setDefaultConverter(Number::class.java, NumberConverter::class.java)
        this.setDefaultConverter(UUID::class.java, UuidToStringConverter::class.java)
        this.setDefaultConverter(Iterable::class.java, IterableConverter::class.java)
        this.setDefaultConverter(Map::class.java, MapConverter::class.java)
    }

    /**
     * Registers the given annotation [type].
     *
     * If a field is annotated by this [type] only, the registry will act as if [CopperField] is only set
     * for [OurType] (equal behavior as if every other [Convertable] type is present in [CopperIgnore]).
     *
     * If a field is annotated by this [type] additionally to the [CopperField] annotation, the name and/or
     * converter of [type] will override those of [CopperField] if explicitly set.
     */
    protected fun registerAnnotation(type: Class<out Annotation>) {
        this.annotations[type] = CopperFieldAnnotationHelper(type)
    }

    /**
     * Sets the [converter] instance used for the given field [type] by default. Any field inheriting [type]
     * will match this converter. If this is not desirable, set additional [Converter]s after this one.
     */
    fun setDefaultConverter(type: Class<*>, converter: Converter<*, *>) {
        this.defaultConverters[type] = converter
        this.converters[converter.javaClass] = converter
    }

    /**
     * Sets the [converter] type used for the given field [type] by default. This requires [converter] to
     * provide a constructor without parameters. Otherwise consider passing an instance instead.
     *
     * @see setDefaultConverter(Class<*>, Converter<*, *>)
     */
    fun setDefaultConverter(type: Class<*>, converter: Class<out Converter<*, *>>) {
        this.setDefaultConverter(type, converter.newInstance())
    }

    /**
     * Removes any [Converter] assigned to the given [type] if it exists.
     */
    fun unsetConverter(type: Class<*>) {
        val converter = this.defaultConverters.remove(type) ?: return
        this.converters.remove(converter.javaClass)
    }

    /**
     * Returns the registered [Converter] assigned to any supertype of [type] or [type] itself.
     * If there is no matching [Converter] the [FALLBACK_CONVERTER] will be returned.
     */
    fun <T : Any> getConverterByValueType(type: Class<T>): Converter<Any, Any> {
        // Dynamically register converters for specific types extending [OurType].
        if (this.ourType.isAssignableFrom(type) && !this.defaultConverters.containsKey(type)) {
            this.setDefaultConverter(type, ConvertibleConverter(type as Class<out OurType>, this.theirType))
        }

        // Find a matching converter and return the [FALLBACK_CONVERTER] if there is none.
        return (this.defaultConverters.entries.lastOrNull { (supportedType, _) ->
            return@lastOrNull supportedType.isAssignableFrom(type)
        }?.value ?: FALLBACK_CONVERTER) as Converter<Any, Any>
    }

    /**
     * Returns the [Converter] instance of the given [type]. If there is no instance yet, it will be
     * instantiated. For this to work, the [type] _must_ provide an empty constructor.
     */
    fun getConverterByConverterType(type: Class<out Converter<*, *>>) = this.converters.getOrPut(type, type::newInstance) as Converter<Any, Any>

    /**
     * Converts and returns a [convertable] of [OurType] to a new instance of [TheirType].
     * Returns `null` if the [convertable] is `null`.
     */
    fun toTheirs(convertable: OurType?): TheirType? {
        if (convertable == null) return null
        val instance = this.createTheirs(convertable)

        this.getFieldDefinitions(convertable.javaClass).forEach {
            val value = it.field.get(convertable)
            val convertedValue = it.converter.toTheirs(value, it.field, this, it.field.type)

            val writeType = when {
                it.isMap -> Map::class.java
                it.isIterable -> Iterable::class.java
                else -> convertedValue?.javaClass ?: it.converter.theirType
            }

            this.writeTheirValue(it.name, convertedValue, instance, writeType)
        }

        return this.finalizeTheirs(instance)
    }

    /**
     * Converts and returns an [entity] of [TheirType] to a new instance of [type].
     * Returns `null` if the [entity] is `null`.
     */
    fun <T : OurType> toOurs(entity: TheirType?, type: Class<T>): T? {
        if (entity == null) return null
        val instance = this.createOurs(type)

        this.getFieldDefinitions(type).forEach {
            val readType = when {
                it.isMap -> Map::class.java
                it.isIterable -> Iterable::class.java
                else -> it.converter.theirType
            }

            val value = this.readTheirValue(it.name, entity, readType)
            val convertedValue = it.converter.toOurs(value, it.field, this, it.field.type)
            it.field.set(instance, convertedValue)
        }

        return instance
    }

    /**
     * Constructs and returns a list of [FieldDefinition] of all fields annotated with any of the registered
     * [annotations] in the given [type] and its superclasses. Ignores fields where [CopperIgnore.convertables]
     * is set and contains [OurType].
     *
     * The fields are cached to increase performance. To clear the cache, see [clearCache].
     */
    private fun getFieldDefinitions(type: Class<out OurType>): List<FieldDefinition> {
        return this.fieldDefinitionCache.getOrPut(type) {
            return@getOrPut this.findFields(type).map {
                val name = this.getName(it)

                val converterType = this.getConverterType(it)
                val converter = this.getConverterByConverterType(converterType)

                return@map FieldDefinition(it, name, converter)
            }
        }
    }

    /**
     * Searches for fields annotated with any of the registered [annotations] in the given [type] and its
     * superclasses. Ignores fields where [CopperIgnore.convertables] is set and contains [OurType].
     */
    private fun findFields(type: Class<out OurType>): List<Field> {
        val fields = mutableListOf<Field>()
        var currentType: Class<*>? = type

        do {
            fields.addAll(currentType!!.declaredFields)
            currentType = currentType.superclass
        } while (currentType != null)

        return fields
            .filter(this::isAnnotated)
            .filterNot(this::isIgnored)
            .map { it.isAccessible = true; it }
    }

    /**
     * Returns whether any of the registered [annotations] is present for the given [field].
     */
    protected open fun isAnnotated(field: Field) = this.annotations.keys.any(field::isAnnotationPresent)

    /**
     * Returns whether [OurType] is present in the [CopperIgnore] annotation of this [field].
     */
    protected open fun isIgnored(field: Field): Boolean {
        val annotation = field.getDeclaredAnnotation(CopperIgnore::class.java) ?: return false
        return annotation.convertables.any { it.java.isAssignableFrom(this.ourType) }
    }

    /**
     * Returns the name for this field based on the annotations of this [field].
     */
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

    /**
     * Returns the [Converter] for this field based on the annotations of this [field].
     */
    protected open fun getConverterType(field: Field): Class<Converter<Any, Any>> {
        var converterType: Class<Converter<Any, Any>>? = null
        this.annotations.entries.forEach { (type, helper) ->
            val annotation = field.getDeclaredAnnotation(type) ?: return@forEach
            val annotationConverterType = helper.getConverter(annotation)
            if (annotationConverterType == FallbackAutoConverter::class.java && converterType != null) return@forEach
            converterType = annotationConverterType
        }
        return converterType ?: throw IllegalStateException("Unable to find valid converter for field: ${field.name}")
    }

    /**
     * Returns a new instance of [OurType].
     */
    protected open fun <T : OurType> createOurs(type: Class<out T>) = type.newInstance()

    /**
     * Returns a new instance of [TheirType].
     */
    protected abstract fun createTheirs(convertible: OurType): TheirType

    /**
     * Returns an instance of [TheirType] after some finalization on the [instance].
     */
    protected open fun <T : TheirType> finalizeTheirs(instance: T): TheirType = instance

    /**
     * Reads the value of the [entity] field with the name [name].
     * The [type] represents the expected type to return.
     */
    protected abstract fun readTheirValue(name: String, entity: TheirType, type: Class<out Any>): Any?

    /**
     * Sets the [value] of the [entity] field with the name [name].
     * The [type] represents the type of the [value] to set.
     */
    protected abstract fun writeTheirValue(name: String, value: Any?, entity: TheirType, type: Class<out Any>)

    /**
     * Clears every cache of this registry.
     */
    open fun clearCache() {
        this.fieldDefinitionCache.clear()
    }

}
