package dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation

import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.converter.FallbackAutoConverter
import kotlin.reflect.KClass

/**
 * Essentially the same as [dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField]
 * but for [dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertable]s only.
 *
 * Alternatively you can use it in addition to [dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField]
 * to override the [name] or [converter] for [dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertable]s.
 *
 * @see dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField
 * @see dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoRegistry
 *
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperProtoField(val name: String = "", val converter: KClass<out Converter<*, *>> = FallbackAutoConverter::class)
