package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.protobuf.MessageOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.ByteArrayProtoConverter
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.ConvertibleProtoConverter
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.DateProtoConverter
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.ProtoReflectionConverter
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.UuidProtoConverter
import java.util.Date
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
class ProtoRegistry : ConverterRegistry<MessageOrBuilder>() {

    // TODO
    //  - list

    init {
        this.defaultConverter = ProtoReflectionConverter()

        this.registerConverter(UUID::class.java, UuidProtoConverter())
        this.registerConverter(ByteArray::class.java, ByteArrayProtoConverter())
        this.registerConverter(Date::class.java, DateProtoConverter())
        this.registerConverter(ProtoConvertible::class.java, ConvertibleProtoConverter())
    }

}
