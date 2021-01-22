package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
abstract class Converter<O : Any, T : Any>(val ourType: Class<O>, val theirType: Class<T>) {

    abstract fun toTheirs(value: O?, agent: CopperfieldAgent, ourType: Class<out O>, targetFormat: Class<out Any>, field: Field?): T?

    abstract fun toOurs(value: T?, agent: CopperfieldAgent, ourType: Class<out O>, targetFormat: Class<out Any>, field: Field?): O?

}
