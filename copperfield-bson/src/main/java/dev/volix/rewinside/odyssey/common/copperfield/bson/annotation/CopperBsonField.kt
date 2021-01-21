package dev.volix.rewinside.odyssey.common.copperfield.bson.annotation

import dev.volix.rewinside.odyssey.common.copperfield.TypeMapper
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import kotlin.reflect.KClass

/**
 * Flags this field to be included in the conversion process.
 *
 * The [name] will be used as basis for the target name and may be modified to fit conventions based on the implementation.
 * If the [name] is an empty string, the java field name will be used and converted to snake case.
 * If a [converter] is set, it will override the default behavior for this type of field, which is defined by the registry used.
 *
 * @see dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent
 *
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperBsonField(val name: String = "",
                                 val converter: KClass<out Converter<out Any, out Any>> = Converter::class,
                                 val typeMapper: KClass<TypeMapper<*, *>> = TypeMapper::class)
