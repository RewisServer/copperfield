package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import dev.volix.rewinside.odyssey.common.copperfield.FieldFilter
import dev.volix.rewinside.odyssey.common.copperfield.FieldNameMapper
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.IgnoreProto
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.ProtoName

object ProtoFieldFilter : FieldFilter<IgnoreProto>(IgnoreProto::class.java) {
    override fun shouldIgnoreOnSerialize(annotation: IgnoreProto) = annotation.ignoreSerialize
    override fun shouldIgnoreOnDeserialize(annotation: IgnoreProto) = annotation.ignoreDeserialize
}

object ProtoFieldNameMapper : FieldNameMapper<ProtoName>(ProtoName::class.java) {
    override fun getName(annotation: ProtoName) = annotation.name
}
