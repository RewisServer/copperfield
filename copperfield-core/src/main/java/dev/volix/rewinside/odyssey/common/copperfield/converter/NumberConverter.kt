package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
open class NumberConverter : Converter<Number, Number>(Number::class.java, Number::class.java) {

    override fun toTheirs(value: Number?, field: Field?, registry: Registry<*, *>, type: Class<out Number>) = value

    override fun toOurs(value: Number?, field: Field?, registry: Registry<*, *>, type: Class<out Number>): Number? {
        if (value == null) return null
        return when(type) {
            Byte::class.java, Byte::class.javaObjectType -> value.toByte()
            Short::class.java, Short::class.javaObjectType -> value.toShort()
            Int::class.java, Int::class.javaObjectType -> value.toInt()
            Long::class.java, Long::class.javaObjectType -> value.toLong()
            Double::class.java, Double::class.javaObjectType -> value.toDouble()
            Float::class.java, Float::class.javaObjectType -> value.toFloat()
            else -> throw IllegalStateException("Unsupported number type: $type.")
        }
    }

}
