package dev.volix.rewinside.odyssey.common.copperfield.bson.annotation

/**
 * @author Benedikt Wüller
 */
annotation class IgnoreBson(val ignoreSerialize: Boolean = true, val ignoreDeserialize: Boolean = true)
