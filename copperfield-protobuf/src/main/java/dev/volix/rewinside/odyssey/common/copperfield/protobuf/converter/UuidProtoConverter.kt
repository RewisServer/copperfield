package dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter

import com.google.protobuf.MessageOrBuilder
import java.lang.reflect.Method
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
class UuidProtoConverter : ProtoReflectionConverter<UUID>(UUID::class.java) {

    override fun convertTo(name: String, value: UUID?, target: MessageOrBuilder) {
        this.getSetterMethod(name, target.javaClass).invoke(target, value?.toString())
    }

    override fun convertFrom(name: String, source: MessageOrBuilder): UUID? {
        val value = this.getGetterMethod(name, source.javaClass).invoke(source) as String? ?: return null
        return UUID.fromString(value)
    }

    override fun getSetterMethod(name: String, type: Class<MessageOrBuilder>): Method {
        return type.getDeclaredMethod(this.getSetterMethodName(name), String::class.java)
    }

}
