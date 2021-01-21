package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
abstract class Converter<OurType : Any, TheirType : Any>(val ourType: Class<OurType>, val theirType: Class<TheirType>) {

    abstract fun toTheirs(value: OurType?, agent: CopperfieldAgent, ourType: Class<out OurType>, targetFormat: Class<Any>, field: Field?): TheirType?

    abstract fun toOurs(value: TheirType?, agent: CopperfieldAgent, ourType: Class<out OurType>, targetFormat: Class<Any>, field: Field?): OurType?

}
