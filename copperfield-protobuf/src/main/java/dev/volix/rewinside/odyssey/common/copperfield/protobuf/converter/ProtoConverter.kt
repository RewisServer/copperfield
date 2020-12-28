package dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter

import com.google.protobuf.MessageOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter


/**
 * @author Benedikt Wüller
 */
interface ProtoConverter<T : Any> : Converter<MessageOrBuilder, T>
