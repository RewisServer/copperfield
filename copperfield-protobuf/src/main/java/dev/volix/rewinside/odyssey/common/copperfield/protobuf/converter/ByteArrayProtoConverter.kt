package dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter

import com.google.protobuf.ByteString
import com.google.protobuf.MessageOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * @author Benedikt Wüller
 */
class ByteArrayProtoConverter : ProtoReflectionConverter<ByteArray>() {

    override fun convertTo(name: String, value: ByteArray?, target: MessageOrBuilder, field: Field, registry: ConverterRegistry<MessageOrBuilder>) {
        this.getSetterMethod(name, target.javaClass, field).invoke(target, if (value == null) null else ByteString.copyFrom(value))
    }

    override fun convertFrom(name: String, source: MessageOrBuilder, field: Field, registry: ConverterRegistry<MessageOrBuilder>): ByteArray? {
        val value = this.getGetterMethod(name, source.javaClass).invoke(source) as ByteString? ?: return null
        return value.toByteArray()
    }

    override fun getSetterMethod(name: String, type: Class<MessageOrBuilder>, field: Field): Method {
        return type.getDeclaredMethod(this.getSetterMethodName(name), ByteString::class.java)
    }

}
