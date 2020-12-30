package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Convertable
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
open class ConvertibleConverter<OurType : Convertable, TheirType : Any>(ourType: Class<out OurType>, theirType: Class<out TheirType>)
    : Converter<OurType, TheirType>(ourType, theirType) {

    override fun toTheirs(value: OurType?, field: Field?, registry: Registry<*, *>, type: Class<out OurType>): TheirType? {
        return (registry as Registry<OurType, TheirType>).toTheirs(value)
    }

    override fun toOurs(value: TheirType?, field: Field?, registry: Registry<*, *>, type: Class<out OurType>): OurType? {
        return (registry as Registry<OurType, TheirType>).toOurs(value, this.ourType)
    }

}
