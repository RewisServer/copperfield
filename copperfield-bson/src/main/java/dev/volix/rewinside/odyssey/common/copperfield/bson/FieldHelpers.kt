package dev.volix.rewinside.odyssey.common.copperfield.bson

import dev.volix.rewinside.odyssey.common.copperfield.FieldFilter
import dev.volix.rewinside.odyssey.common.copperfield.FieldNameMapper
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.CopperBsonField
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.CopperBsonIgnore

object BsonFieldFilter : FieldFilter<CopperBsonIgnore>(CopperBsonIgnore::class.java) {
    override fun shouldIgnoreOnSerialize(annotation: CopperBsonIgnore) = annotation.ignoreSerialize
    override fun shouldIgnoreOnDeserialize(annotation: CopperBsonIgnore) = annotation.ignoreDeserialize
}

object BsonFieldNameMapper : FieldNameMapper<CopperBsonField>(CopperBsonField::class.java) {
    override fun getName(annotation: CopperBsonField) = annotation.name
}
