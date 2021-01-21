package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
abstract class Converter<OurType : Any, TheirType : Any>(val ourType: Class<OurType>, val theirType: Class<TheirType>) {

    abstract fun toTheirs(value: OurType?, registry: Registry, ourType: Class<out OurType>, targetFormatType: Class<*>, field: Field?): TheirType?

    abstract fun toOurs(value: TheirType?, registry: Registry, ourType: Class<out OurType>, targetFormatType: Class<*>, field: Field?): OurType?

}
