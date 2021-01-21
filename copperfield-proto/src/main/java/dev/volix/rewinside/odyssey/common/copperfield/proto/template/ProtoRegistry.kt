package dev.volix.rewinside.odyssey.common.copperfield.proto.template

import com.google.protobuf.MessageLiteOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.CopperConvertable
import dev.volix.rewinside.odyssey.common.copperfield.proto.converter.ByteArrayToProtoByteStringConverter
import dev.volix.rewinside.odyssey.common.copperfield.proto.converter.CopperToProtoConverter
import dev.volix.rewinside.odyssey.common.copperfield.proto.converter.MapToProtoStructConverter
import dev.volix.rewinside.odyssey.common.copperfield.template.Registry

/**
 * @author Benedikt Wüller
 */
class ProtoRegistry : Registry() {

    companion object {
        @JvmField
        val TARGET_PROTO = MessageLiteOrBuilder::class.java
    }

    init {
        this.with(CopperConvertable::class.java, CopperToProtoConverter::class.java, TARGET_PROTO)
        this.with(ByteArray::class.java, ByteArrayToProtoByteStringConverter::class.java, TARGET_PROTO)
        this.with(Map::class.java, MapToProtoStructConverter::class.java, TARGET_PROTO)
    }

}
