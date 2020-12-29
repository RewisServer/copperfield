package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
abstract class Converter<OurType : Any, TheirType : Any>(val ourType: Class<out OurType>, val theirType: Class<out TheirType>) {

    abstract fun toTheirs(value: OurType?, field: Field?, registry: Registry<*, *>, type: Class<out OurType>): TheirType?

    abstract fun toOurs(value: TheirType?, field: Field?, registry: Registry<*, *>, type: Class<out OurType>): OurType?

}
