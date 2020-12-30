package dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter

import com.google.protobuf.ByteString
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class ByteArrayToByteStringConverter : Converter<ByteArray, ByteString>(ByteArray::class.java, ByteString::class.java) {

    override fun toTheirs(value: ByteArray?, field: Field?, registry: Registry<*, *>, type: Class<out ByteArray>): ByteString? {
        if (value == null) return null
        return ByteString.copyFrom(value)
    }

    override fun toOurs(value: ByteString?, field: Field?, registry: Registry<*, *>, type: Class<out ByteArray>): ByteArray? {
        if (value == null) return null
        return value.toByteArray()
    }

}
