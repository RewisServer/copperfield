package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * Does not change the value at all.
 *
 * @author Benedikt Wüller
 */
open class NoOperationConverter : Converter<Any, Any>(Any::class.java, Any::class.java) {

    override fun toTheirs(value: Any?, field: Field?, registry: Registry<*, *>, type: Class<out Any>): Any? = value

    override fun toOurs(value: Any?, field: Field?, registry: Registry<*, *>, type: Class<out Any>): Any? = value

}