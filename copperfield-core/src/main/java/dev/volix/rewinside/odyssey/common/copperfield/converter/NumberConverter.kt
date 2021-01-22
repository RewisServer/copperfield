package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent
import java.lang.reflect.Field

/**
 * Makes sure the [Number]s are converted to the right type when converting `theirs` to `ours`.

 * @author Benedikt Wüller
 */
class NumberConverter : Converter<Number, Number>(Number::class.java, Number::class.java) {

    override fun toTheirs(value: Number?, agent: CopperfieldAgent, ourType: Class<out Number>, contextType: Class<out Any>, field: Field?) = value

    override fun toOurs(value: Number?, agent: CopperfieldAgent, ourType: Class<out Number>, contextType: Class<out Any>, field: Field?): Number? {
        if (value == null) return null
        return when(ourType) {
            Byte::class.javaPrimitiveType, Byte::class.javaObjectType -> value.toByte()
            Short::class.javaPrimitiveType, Short::class.javaObjectType -> value.toShort()
            Int::class.javaPrimitiveType, Int::class.javaObjectType -> value.toInt()
            Long::class.javaPrimitiveType, Long::class.javaObjectType -> value.toLong()
            Double::class.javaPrimitiveType, Double::class.javaObjectType -> value.toDouble()
            Float::class.javaPrimitiveType, Float::class.javaObjectType -> value.toFloat()
            else -> throw IllegalStateException("Unsupported number type: $ourType.")
        }
    }

}
