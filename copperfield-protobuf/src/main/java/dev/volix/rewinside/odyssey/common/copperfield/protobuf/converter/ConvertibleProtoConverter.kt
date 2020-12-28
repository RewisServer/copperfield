package dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.MessageOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertible
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class ConvertibleProtoConverter : ProtoReflectionConverter<ProtoConvertible<*>>() {

    override fun convertTo(name: String, value: ProtoConvertible<*>?, target: MessageOrBuilder, field: Field, registry: ConverterRegistry<MessageOrBuilder>) {
        val message = value?.toProtoMessage(registry)
        val type = value?.protoClass ?: (field.type.newInstance() as ProtoConvertible<*>).protoClass
        val method = target.javaClass.getDeclaredMethod(this.getSetterMethodName(name), type)
        method.invoke(target, message)
    }

    override fun convertFrom(name: String, source: MessageOrBuilder, field: Field, registry: ConverterRegistry<MessageOrBuilder>): ProtoConvertible<*>? {
        val message = this.getGetterMethod(name, source.javaClass).invoke(source) as GeneratedMessageV3? ?: return null
        val type = field.type
        val instance = type.newInstance() as ProtoConvertible<GeneratedMessageV3>
        instance.fromProtoMessage(message, registry)
        return instance
    }

}
