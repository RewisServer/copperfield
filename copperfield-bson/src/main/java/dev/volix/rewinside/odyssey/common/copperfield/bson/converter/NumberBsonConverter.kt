package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import org.bson.Document
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class NumberBsonConverter : SimpleBsonConverter<Number>() {

    override fun convertFrom(name: String, source: Document, field: Field, registry: ConverterRegistry<Document>): Number? {
        val number = source.get(name, Number::class.java) ?: return null
        return when(field.type) {
            Byte::class.java -> number.toByte()
            Short::class.java -> number.toShort()
            Int::class.java -> number.toInt()
            Long::class.java -> number.toLong()
            Double::class.java -> number.toDouble()
            Float::class.java -> number.toFloat()
            else -> throw IllegalStateException("Unsupported number type: ${field.type}.")
        }
    }

}
