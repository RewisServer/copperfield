package dev.volix.rewinside.odyssey.common.copperfield.protobuf.typeconverter

import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoRegistry
import dev.volix.rewinside.odyssey.common.copperfield.typeconverter.TypeConverter

/**
 * @author Benedikt Wüller
 */
abstract class ProtoTypeConverter<OURS : Any, THEIRS : Any>(ourType: Class<OURS>, theirType: Class<THEIRS>)
    : TypeConverter<ProtoRegistry, OURS, THEIRS>(ourType, theirType)
