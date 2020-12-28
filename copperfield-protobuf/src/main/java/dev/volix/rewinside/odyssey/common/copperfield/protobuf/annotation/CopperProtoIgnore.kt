package dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation

/**
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperProtoIgnore(val ignoreSerialize: Boolean = true, val ignoreDeserialize: Boolean = true)
