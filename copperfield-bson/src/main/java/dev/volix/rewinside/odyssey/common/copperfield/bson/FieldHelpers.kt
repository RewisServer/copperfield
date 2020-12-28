package dev.volix.rewinside.odyssey.common.copperfield.bson

import dev.volix.rewinside.odyssey.common.copperfield.FieldFilter
import dev.volix.rewinside.odyssey.common.copperfield.FieldNameMapper
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.BsonName
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.IgnoreBson

object BsonFieldFilter : FieldFilter<IgnoreBson>(IgnoreBson::class.java) {
    override fun shouldIgnoreOnSerialize(annotation: IgnoreBson) = annotation.ignoreSerialize
    override fun shouldIgnoreOnDeserialize(annotation: IgnoreBson) = annotation.ignoreDeserialize
}

object BsonFieldNameMapper : FieldNameMapper<BsonName>(BsonName::class.java) {
    override fun getName(annotation: BsonName) = annotation.name
}
