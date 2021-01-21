package dev.volix.rewinside.odyssey.common.copperfield.proto.converter

import com.google.protobuf.ByteString
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class ByteArrayToProtoByteStringConverter : Converter<ByteArray, ByteString>(ByteArray::class.java, ByteString::class.java) {

    override fun toTheirs(value: ByteArray?, registry: Registry, ourType: Class<out ByteArray>, targetFormatType: Class<*>,
                          field: Field?): ByteString? {
        if (value == null) return null
        return ByteString.copyFrom(value)
    }

    override fun toOurs(value: ByteString?, registry: Registry, ourType: Class<out ByteArray>, targetFormatType: Class<*>,
                        field: Field?): ByteArray? {
        if (value == null) return null
        return value.toByteArray()
    }

}
