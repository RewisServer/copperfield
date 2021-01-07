package dev.volix.rewinside.odyssey.common.copperfield.bson.annotation

import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.converter.FallbackAutoConverter
import kotlin.reflect.KClass

/**
 * Essentially the same as [dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField]
 * but for [dev.volix.rewinside.odyssey.common.copperfield.bson.BsonConvertable]s only.
 *
 * Alternatively you can use it in addition to [dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField]
 * to override the [name] or [converter] for [dev.volix.rewinside.odyssey.common.copperfield.bson.BsonConvertable]s.
 *
 * @see dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField
 * @see dev.volix.rewinside.odyssey.common.copperfield.bson.BsonRegistry
 *
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperBsonField(val name: String = "", val converter: KClass<out Converter<*, *>> = FallbackAutoConverter::class)
