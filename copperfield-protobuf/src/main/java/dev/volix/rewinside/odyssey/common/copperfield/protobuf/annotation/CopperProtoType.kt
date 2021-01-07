package dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation

import com.google.protobuf.MessageOrBuilder
import kotlin.reflect.KClass

/**
 * This annotation is required for any class implementing [dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertable].
 * It is used to determine which type of [com.google.protobuf.Message] to used when creating a new instance.
 * The [type] should be equal to the generic type of the [dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertable].
 *
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperProtoType(val type: KClass<out MessageOrBuilder>)
