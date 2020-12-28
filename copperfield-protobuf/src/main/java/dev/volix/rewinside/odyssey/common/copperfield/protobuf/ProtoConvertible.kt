package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.protobuf.Message
import dev.volix.rewinside.odyssey.common.copperfield.Convertible

/**
 * @author Benedikt Wüller
 */
interface ProtoConvertible<T : Message> : Convertible<T> {

    fun toProtoMessage(): T {
        TODO()
    }

    fun fromProtoMessage(source: T) {
        TODO()
    }

}
