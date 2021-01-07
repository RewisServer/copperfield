package dev.volix.rewinside.odyssey.common.copperfield.annotation

import dev.volix.rewinside.odyssey.common.copperfield.Convertable
import kotlin.reflect.KClass

/**
 * Excludes this field from conversion to and from the given [Convertable] types.
 *
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperIgnore(vararg val convertables: KClass<out Convertable>)
