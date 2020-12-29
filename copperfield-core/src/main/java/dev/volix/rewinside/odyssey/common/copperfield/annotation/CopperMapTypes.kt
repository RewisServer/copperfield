package dev.volix.rewinside.odyssey.common.copperfield.annotation

import dev.volix.rewinside.odyssey.common.copperfield.converter.AutoConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import kotlin.reflect.KClass

/**
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperMapTypes(
    val keyType: KClass<*>, val keyConverter: KClass<out Converter<*, *>> = AutoConverter::class,
    val valueType: KClass<*>, val valueConverter: KClass<out Converter<*, *>> = AutoConverter::class
)
