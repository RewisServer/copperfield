package dev.volix.rewinside.odyssey.common.copperfield.typeconverter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
abstract class TypeConverter<R : Registry<*, *, *>, OURS : Any, THEIRS : Any>(val ourType: Class<OURS>, val theirType: Class<THEIRS>) {

    abstract fun convertOursToTheirs(value: OURS?, field: Field, registry: R): THEIRS?

    abstract fun convertTheirsToOurs(value: THEIRS?, field: Field, registry: R): OURS?

}
