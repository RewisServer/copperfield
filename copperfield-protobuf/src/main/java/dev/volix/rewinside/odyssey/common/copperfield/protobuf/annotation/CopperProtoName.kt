package dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation

/**
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperProtoName(val name: String)
