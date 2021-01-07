package dev.volix.rewinside.odyssey.common.copperfield.annotation

import dev.volix.rewinside.odyssey.common.copperfield.converter.AutoConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import kotlin.reflect.KClass

/**
 * Flags this field to be included in the conversion process.
 *
 * The [name] will be used as basis for the target name and may be modified to fit conventions based on the implementation.
 * If a [converter] is set, it will override the default behavior for this type of field, which is defined by the registry used.
 *
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperField(val name: String, val converter: KClass<out Converter<*, *>> = AutoConverter::class)
