package dev.volix.rewinside.odyssey.common.copperfield.annotation

import dev.volix.rewinside.odyssey.common.copperfield.converter.AutoConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import kotlin.reflect.KClass

/**
 * Provides the [keyType] and [valueType] of map fields.
 * The optional [keyConverter] and [valueConverter] can be set for custom key/value transformation.
 *
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperMapTypes(
    val keyType: KClass<*>, val keyConverter: KClass<out Converter<*, *>> = AutoConverter::class,
    val valueType: KClass<*>, val valueConverter: KClass<out Converter<*, *>> = AutoConverter::class
)
