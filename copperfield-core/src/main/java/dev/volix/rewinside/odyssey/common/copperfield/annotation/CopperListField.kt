package dev.volix.rewinside.odyssey.common.copperfield.annotation

import kotlin.reflect.KClass

/**
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperListField(val name: String = "", val innerType: KClass<*>)
