package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.MessageOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import dev.volix.rewinside.odyssey.common.copperfield.convertFrom
import dev.volix.rewinside.odyssey.common.copperfield.convertTo

/**
 * @author Benedikt Wüller
 */
interface ProtoConvertible<T : GeneratedMessageV3> {

    val protoClass: Class<T>

    @JvmDefault
    fun toProtoMessage(registry: ConverterRegistry<MessageOrBuilder>): T {
        val newBuilderMethod = protoClass.getDeclaredMethod("newBuilder")
        val builder = newBuilderMethod.invoke(null) as GeneratedMessageV3.Builder<*>

        convertTo(this, builder, registry, ProtoFieldNameMapper, ProtoFieldFilter)

        val buildMethod = builder.javaClass.getDeclaredMethod("build")
        return buildMethod.invoke(builder) as T
    }

    @JvmDefault
    fun fromProtoMessage(source: T, registry: ConverterRegistry<MessageOrBuilder>) {
        convertFrom(this, source, registry, ProtoFieldNameMapper, ProtoFieldFilter)
    }

}
