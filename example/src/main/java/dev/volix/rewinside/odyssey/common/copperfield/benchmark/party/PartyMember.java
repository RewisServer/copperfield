package dev.volix.rewinside.odyssey.common.copperfield.benchmark.party;

import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonConvertable;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertable;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoType;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import java.util.UUID;

/**
 * @author Benedikt Wüller
 */
@CopperProtoType(type = PartyProtos.PartyMember.class)
public class PartyMember implements BsonConvertable, ProtoConvertable<PartyProtos.PartyMember> {

    @CopperField(name = "uuid")
    public UUID uuid;

    @CopperField(name = "rank")
    public String rank;

}
