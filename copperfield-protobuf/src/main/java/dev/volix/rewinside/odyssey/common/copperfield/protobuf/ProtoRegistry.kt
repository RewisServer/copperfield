package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.protobuf.MessageOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
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
    //  - convertible

    init {
        // Register default converters.
        this.registerConverter(Int::class.java, ProtoReflectionConverter(Int::class.java))
        this.registerConverter(Int::class.javaObjectType, ProtoReflectionConverter(Int::class.java))

        this.registerConverter(Long::class.java, ProtoReflectionConverter(Long::class.java))
        this.registerConverter(Long::class.javaObjectType, ProtoReflectionConverter(Long::class.java))

        this.registerConverter(Double::class.java, ProtoReflectionConverter(Double::class.java))
        this.registerConverter(Double::class.javaObjectType, ProtoReflectionConverter(Double::class.java))

        this.registerConverter(Float::class.java, ProtoReflectionConverter(Float::class.java))
        this.registerConverter(Float::class.javaObjectType, ProtoReflectionConverter(Float::class.java))

        this.registerConverter(Boolean::class.java, ProtoReflectionConverter(Boolean::class.java))
        this.registerConverter(Boolean::class.javaObjectType, ProtoReflectionConverter(Boolean::class.java))

        this.registerConverter(String::class.java, ProtoReflectionConverter(String::class.java))

        this.registerConverter(UUID::class.java, UuidProtoConverter())
    }

}
