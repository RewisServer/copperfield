package dev.volix.rewinside.odyssey.common.copperfield.proto.template

import com.google.protobuf.MessageLiteOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.CopperConvertable
import dev.volix.rewinside.odyssey.common.copperfield.proto.converter.ByteArrayToProtoByteStringConverter
import dev.volix.rewinside.odyssey.common.copperfield.proto.converter.CopperToProtoConverter
import dev.volix.rewinside.odyssey.common.copperfield.proto.converter.MapToProtoStructConverter
import dev.volix.rewinside.odyssey.common.copperfield.proto.converter.OffsetDateTimeToProtoTimestampConverter
import dev.volix.rewinside.odyssey.common.copperfield.template.Template
import java.time.OffsetDateTime

/**
 * @author Benedikt Wüller
 */
class ProtoTemplate : Template() {

    companion object {
        @JvmField
        val FORMAT = MessageLiteOrBuilder::class.java
    }

    init {
        this.with(CopperConvertable::class.java, CopperToProtoConverter(), FORMAT)
        this.with(ByteArray::class.java, ByteArrayToProtoByteStringConverter(), FORMAT)
        this.with(Map::class.java, MapToProtoStructConverter(), FORMAT)
        this.with(OffsetDateTime::class.java, OffsetDateTimeToProtoTimestampConverter(), FORMAT)
    }

}
