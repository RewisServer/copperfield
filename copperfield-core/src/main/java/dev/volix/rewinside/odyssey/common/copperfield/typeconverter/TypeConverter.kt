package dev.volix.rewinside.odyssey.common.copperfield.typeconverter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
abstract class TypeConverter<R : Registry<*, *, *>, OURS : Any, THEIRS : Any>(val ourType: Class<OURS>, val theirType: Class<out THEIRS>) {

    open fun tryConvertOursToTheirs(value: OURS?, field: Field, registry: R): THEIRS? {
        if (value == null) return value
        return this.convertOursToTheirs(value, field, registry)
    }

    open fun tryConvertTheirsToOurs(value: THEIRS?, field: Field, registry: R): OURS? {
        if (value == null) return value
        return this.convertTheirsToOurs(value, field, registry)
    }

    abstract fun convertOursToTheirs(value: OURS, field: Field, registry: R): THEIRS?

    abstract fun convertTheirsToOurs(value: THEIRS, field: Field, registry: R): OURS?

}
