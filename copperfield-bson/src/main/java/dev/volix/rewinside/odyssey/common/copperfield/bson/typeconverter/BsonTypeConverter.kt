package dev.volix.rewinside.odyssey.common.copperfield.bson.typeconverter

import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonRegistry
import dev.volix.rewinside.odyssey.common.copperfield.typeconverter.TypeConverter

/**
 * @author Benedikt Wüller
 */
abstract class BsonTypeConverter<OURS : Any, THEIRS : Any>(ourType: Class<OURS>, theirType: Class<THEIRS>)
    : TypeConverter<BsonRegistry, OURS, THEIRS>(ourType, theirType)
