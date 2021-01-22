package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class NoOperationConverter : Converter<Any, Any>(Any::class.java, Any::class.java) {

    override fun toTheirs(value: Any?, agent: CopperfieldAgent, ourType: Class<out Any>, targetFormat: Class<out Any>, field: Field?) = value

    override fun toOurs(value: Any?, agent: CopperfieldAgent, ourType: Class<out Any>, targetFormat: Class<out Any>, field: Field?) = value

}
