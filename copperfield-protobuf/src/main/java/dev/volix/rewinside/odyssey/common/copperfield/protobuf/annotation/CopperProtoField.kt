package dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation

import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.converter.CopperFieldOrAutoConverter
import kotlin.reflect.KClass

/**
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperProtoField(val name: String = "", val converter: KClass<out Converter<*, *>> = CopperFieldOrAutoConverter::class)
