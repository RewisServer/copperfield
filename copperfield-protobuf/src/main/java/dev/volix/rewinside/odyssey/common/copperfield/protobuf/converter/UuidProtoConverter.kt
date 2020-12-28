package dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter

import com.google.protobuf.MessageOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
class UuidProtoConverter : ProtoReflectionConverter<UUID>() {

    override fun convertTo(name: String, value: UUID?, target: MessageOrBuilder, field: Field, registry: ConverterRegistry<MessageOrBuilder>) {
        this.getSetterMethod(name, target.javaClass, field).invoke(target, value?.toString())
    }

    override fun convertFrom(name: String, source: MessageOrBuilder, field: Field, registry: ConverterRegistry<MessageOrBuilder>): UUID? {
        val value = this.getGetterMethod(name, source.javaClass).invoke(source) as String? ?: return null
        return UUID.fromString(value)
    }

    override fun getSetterMethod(name: String, type: Class<MessageOrBuilder>, field: Field): Method {
        return type.getDeclaredMethod(this.getSetterMethodName(name), String::class.java)
    }

}
