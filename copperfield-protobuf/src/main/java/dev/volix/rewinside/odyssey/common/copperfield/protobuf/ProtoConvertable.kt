package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.protobuf.MessageOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.Convertable

/**
 * @author Benedikt Wüller
 */
interface ProtoConvertable<T : MessageOrBuilder> : Convertable
