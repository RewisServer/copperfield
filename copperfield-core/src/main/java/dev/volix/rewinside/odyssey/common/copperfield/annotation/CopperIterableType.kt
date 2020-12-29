package dev.volix.rewinside.odyssey.common.copperfield.annotation

import dev.volix.rewinside.odyssey.common.copperfield.converter.AutoConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import kotlin.reflect.KClass

/**
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperIterableType(val innerType: KClass<*>, val innerConverter: KClass<out Converter<*, *>> = AutoConverter::class)
