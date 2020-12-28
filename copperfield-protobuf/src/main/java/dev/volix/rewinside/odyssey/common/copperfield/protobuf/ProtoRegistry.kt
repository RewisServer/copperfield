package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.protobuf.MessageOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.ConvertibleProtoConverter
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.ProtoReflectionConverter
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.UuidProtoConverter
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
class ProtoRegistry : ConverterRegistry<MessageOrBuilder>() {

    // TODO
    //  - ByteString (bytes)
    //  - list
    //  - date

    init {
        this.defaultConverter = ProtoReflectionConverter()
        this.registerConverter(UUID::class.java, UuidProtoConverter())
        this.registerConverter(ProtoConvertible::class.java, ConvertibleProtoConverter())
    }

}
