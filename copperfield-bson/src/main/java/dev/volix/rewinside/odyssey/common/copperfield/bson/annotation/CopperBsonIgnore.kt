package dev.volix.rewinside.odyssey.common.copperfield.bson.annotation

/**
 * @author Benedikt Wüller
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CopperBsonIgnore(val ignoreSerialize: Boolean = true, val ignoreDeserialize: Boolean = true)
