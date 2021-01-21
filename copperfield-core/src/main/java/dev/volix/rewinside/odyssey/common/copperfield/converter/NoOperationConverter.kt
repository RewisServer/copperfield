package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class NoOperationConverter : Converter<Any, Any>(Any::class.java, Any::class.java) {

    override fun toTheirs(value: Any?, registry: Registry, ourType: Class<out Any>, targetFormatType: Class<*>, field: Field?) = value

    override fun toOurs(value: Any?, registry: Registry, ourType: Class<out Any>, targetFormatType: Class<*>, field: Field?) = value

}
