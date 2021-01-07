package dev.volix.rewinside.odyssey.common.copperfield.annotation

import dev.volix.rewinside.odyssey.common.copperfield.converter.AutoConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import kotlin.reflect.KClass

/**
 * Provides the [valueType] of iterable fields.
 * The optional [valueConverter] can be set for custom value transformation.
 *
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperIterableType(val valueType: KClass<*>, val valueConverter: KClass<out Converter<*, *>> = AutoConverter::class)
