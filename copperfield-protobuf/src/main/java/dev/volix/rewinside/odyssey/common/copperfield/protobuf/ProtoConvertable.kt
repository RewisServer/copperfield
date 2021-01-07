package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.protobuf.MessageOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.Convertable

/**
 * This interface indicates, that this class may be converted to and from [com.google.protobuf.Message]s
 * instances of type [T] using a [ProtoRegistry].
 *
 * When implementing this interface, make sure to also annotate the class with
 * [dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoType]. The type should
 * equal [T].
 *
 * @author Benedikt Wüller
 */
interface ProtoConvertable<T : MessageOrBuilder> : Convertable
