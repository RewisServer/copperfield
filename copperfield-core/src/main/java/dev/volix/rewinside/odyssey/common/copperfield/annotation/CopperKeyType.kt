package dev.volix.rewinside.odyssey.common.copperfield.annotation

import kotlin.reflect.KClass

/**
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperKeyType(val type: KClass<*>)
