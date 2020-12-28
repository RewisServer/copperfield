package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.protobuf.GeneratedMessageV3
import dev.volix.rewinside.odyssey.common.copperfield.Converter


/**
 * @author Benedikt Wüller
 */
interface ProtoConverter<T : Any> : Converter<GeneratedMessageV3.Builder<*>, T>
