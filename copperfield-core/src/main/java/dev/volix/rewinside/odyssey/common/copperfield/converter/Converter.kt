package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * Provides functionality to convert objects of [OurType] to objects of [TheirType] and vice versa.
 *
 * @author Benedikt Wüller
 */
abstract class Converter<OurType : Any, TheirType : Any>(val ourType: Class<out OurType>, val theirType: Class<out TheirType>) {

    /**
     * Transforms the [value] from [OurType] to [TheirType].
     * The [field] is set, if converting to/from a [Field] directly.
     * The [registry] is the [Registry] executing the conversion.
     * The [type] is the type on _our_ side. Use [type] instead of [Field.type].
     */
    abstract fun toTheirs(value: OurType?, field: Field?, registry: Registry<*, *>, type: Class<out OurType>): TheirType?

    /**
     * Transforms the [value] from [TheirType] to [OurType].
     * The [field] is set, if converting to/from a [Field] directly.
     * The [registry] is the [Registry] executing the conversion.
     * The [type] is the type on _our_ side. Use [type] instead of [Field.type].
     */
    abstract fun toOurs(value: TheirType?, field: Field?, registry: Registry<*, *>, type: Class<out OurType>): OurType?

}
