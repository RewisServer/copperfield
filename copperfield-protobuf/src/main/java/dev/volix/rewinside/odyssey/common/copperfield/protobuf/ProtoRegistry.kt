package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.protobuf.MessageOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.ProtoReflectionConverter

/**
 * @author Benedikt Wüller
 */
class ProtoRegistry : ConverterRegistry<MessageOrBuilder>() {

    // TODO
    //  - ByteString (bytes)
    //  - list
    //  - date
    //  - convertible
    //  - uuid

    init {
        // Register default converters.
        this.registerConverter(Int::class.java, ProtoReflectionConverter(Int::class.java))
        this.registerConverter(Long::class.java, ProtoReflectionConverter(Long::class.java))
        this.registerConverter(Double::class.java, ProtoReflectionConverter(Double::class.java))
        this.registerConverter(Float::class.java, ProtoReflectionConverter(Float::class.java))
        this.registerConverter(Boolean::class.java, ProtoReflectionConverter(Boolean::class.java))
        this.registerConverter(String::class.java, ProtoReflectionConverter(String::class.java))
    }

}
