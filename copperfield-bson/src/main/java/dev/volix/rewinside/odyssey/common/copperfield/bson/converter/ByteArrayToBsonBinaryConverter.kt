package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import org.bson.types.Binary
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class ByteArrayToBsonBinaryConverter : Converter<ByteArray, Binary>(ByteArray::class.java, Binary::class.java) {

    override fun toTheirs(value: ByteArray?, registry: Registry, ourType: Class<out ByteArray>, targetFormatType: Class<*>, field: Field?): Binary? {
        if (value == null) return null
        return Binary(value)
    }

    override fun toOurs(value: Binary?, registry: Registry, ourType: Class<out ByteArray>, targetFormatType: Class<*>, field: Field?): ByteArray? {
        if (value == null) return null
        return value.data
    }

}
