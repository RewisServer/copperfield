package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import dev.volix.rewinside.odyssey.common.copperfield.FieldFilter
import dev.volix.rewinside.odyssey.common.copperfield.FieldNameMapper
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoIgnore
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoField

object ProtoFieldFilter : FieldFilter<CopperProtoIgnore>(CopperProtoIgnore::class.java) {
    override fun shouldIgnoreOnSerialize(annotation: CopperProtoIgnore) = annotation.ignoreSerialize
    override fun shouldIgnoreOnDeserialize(annotation: CopperProtoIgnore) = annotation.ignoreDeserialize
}

object ProtoFieldNameMapper : FieldNameMapper<CopperProtoField>(CopperProtoField::class.java) {
    override fun getName(annotation: CopperProtoField) = annotation.name
}
