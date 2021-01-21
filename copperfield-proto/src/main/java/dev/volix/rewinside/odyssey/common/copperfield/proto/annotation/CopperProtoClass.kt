package dev.volix.rewinside.odyssey.common.copperfield.proto.annotation

import com.google.protobuf.MessageLiteOrBuilder
import kotlin.reflect.KClass

/**
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperProtoClass(val type: KClass<out MessageLiteOrBuilder>)
