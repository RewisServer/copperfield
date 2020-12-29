package dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation

import com.google.protobuf.MessageOrBuilder
import kotlin.reflect.KClass

/**
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperProtoType(val type: KClass<out MessageOrBuilder>)
