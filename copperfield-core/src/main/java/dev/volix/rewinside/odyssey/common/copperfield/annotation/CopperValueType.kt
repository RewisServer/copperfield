package dev.volix.rewinside.odyssey.common.copperfield.annotation

import dev.volix.rewinside.odyssey.common.copperfield.TypeMapper
import kotlin.reflect.KClass

/**
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperValueType(val type: KClass<*>, val typeMapper: KClass<TypeMapper<*, *>> = TypeMapper::class)
