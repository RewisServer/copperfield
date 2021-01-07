package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import org.bson.types.Binary
import java.lang.reflect.Field

/**
 * Converts byte arrays to/from bson [Binary] objects.
 *
 * @author Benedikt Wüller
 */
class ByteArrayToBsonBinaryConverter : Converter<ByteArray, Binary>(ByteArray::class.java, Binary::class.java) {

    override fun toTheirs(value: ByteArray?, field: Field?, registry: Registry<*, *>, type: Class<out ByteArray>): Binary? {
        if (value == null) return null
        return Binary(value)
    }

    override fun toOurs(value: Binary?, field: Field?, registry: Registry<*, *>, type: Class<out ByteArray>): ByteArray? {
        if (value == null) return null
        return value.data
    }

}
