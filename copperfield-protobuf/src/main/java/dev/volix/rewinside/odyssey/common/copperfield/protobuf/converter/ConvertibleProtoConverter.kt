package dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.MessageOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertible
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * @author Benedikt Wüller
 */
class ConvertibleProtoConverter : ProtoReflectionConverter<ProtoConvertible<*>>(ProtoConvertible::class.java) {

    override fun convertTo(name: String, value: ProtoConvertible<*>?, target: MessageOrBuilder, field: Field, registry: ConverterRegistry<MessageOrBuilder>) {
        val message = (value as ProtoConvertible<GeneratedMessageV3>?)?.toProtoMessage(field.type as Class<GeneratedMessageV3>, registry)
        this.getSetterMethod(name, target.javaClass).invoke(target, message)
    }

    override fun convertFrom(name: String, source: MessageOrBuilder, field: Field, registry: ConverterRegistry<MessageOrBuilder>): ProtoConvertible<*>? {
        val message = this.getGetterMethod(name, source.javaClass).invoke(source) as GeneratedMessageV3? ?: return null
        val type = field.type
        val instance = type.newInstance() as ProtoConvertible<GeneratedMessageV3>
        instance.fromProtoMessage(message, registry)
        return instance
    }

    override fun getSetterMethod(name: String, type: Class<MessageOrBuilder>): Method {
        return type.getDeclaredMethod(this.getSetterMethodName(name), GeneratedMessageV3::class.java)
    }

}
