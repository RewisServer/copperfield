package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.protobuf.GeneratedMessageV3
import dev.volix.rewinside.odyssey.common.copperfield.Convertible

/**
 * @author Benedikt Wüller
 */
interface ProtoConvertible<T : GeneratedMessageV3> : Convertible {

    val protoClass: Class<T>

}
