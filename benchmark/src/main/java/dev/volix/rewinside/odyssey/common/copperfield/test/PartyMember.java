package dev.volix.rewinside.odyssey.common.copperfield.test;

import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonConvertible;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertible;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * @author Benedikt Wüller
 */
public class PartyMember implements BsonConvertible, ProtoConvertible<PartyProtos.PartyMember> {

    @CopperField(name = "uuid")
    public UUID uuid;

    @CopperField(name = "rank")
    public String rank;

    @NotNull @Override
    public Class<PartyProtos.PartyMember> getProtoClass() {
        return PartyProtos.PartyMember.class;
    }

}
