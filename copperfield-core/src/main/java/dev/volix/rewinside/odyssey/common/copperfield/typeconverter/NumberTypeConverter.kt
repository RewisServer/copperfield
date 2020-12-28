package dev.volix.rewinside.odyssey.common.copperfield.typeconverter

import dev.volix.rewinside.odyssey.common.copperfield.Convertible
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class NumberTypeConverter<T : Any, C : Convertible, R : Registry<T, C, R>>
    : TypeConverter<R, Number, Number>(Number::class.java, Number::class.java) {

    override fun convertOursToTheirs(value: Number, field: Field, registry: R): Number = value

    override fun convertTheirsToOurs(value: Number, field: Field, registry: R): Number {
        return when(field.type) {
            Byte::class.java, Byte::class.javaObjectType -> value.toByte()
            Short::class.java, Short::class.javaObjectType -> value.toShort()
            Int::class.java, Int::class.javaObjectType -> value.toInt()
            Long::class.java, Long::class.javaObjectType -> value.toLong()
            Double::class.java, Double::class.javaObjectType -> value.toDouble()
            Float::class.java, Float::class.javaObjectType -> value.toFloat()
            else -> throw IllegalStateException("Unsupported number type: ${field.type}.")
        }
    }

}
