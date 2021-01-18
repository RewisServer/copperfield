package dev.volix.rewinside.odyssey.common.copperfield.benchmark.party;

import com.google.protobuf.Struct;
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonConvertable;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertable;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoType;

/**
 * @author Benedikt Wüller
 */
@CopperProtoType(type = Struct.class)
public class PartySettings implements BsonConvertable, ProtoConvertable<Struct> {

    @CopperField
    public int maxSize;

    @CopperField
    public String topic;

}
