package dev.volix.rewinside.odyssey.common.copperfield.test;

import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField;
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperListField;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonConvertible;
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.CopperBsonField;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertible;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import java.time.ZonedDateTime;
import java.util.List;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

/**
 * @author Benedikt Wüller
 */
public class Party implements BsonConvertible, ProtoConvertible<PartyProtos.Party> {

    @CopperField(name = "id")
    @CopperBsonField(name = "_id") // different name for bson
    public ObjectId id;

    @CopperField(name = "created_at")
    public ZonedDateTime createdAt;

    @CopperListField(name = "banned_uuids", innerType = String.class)
    public List<String> bannedUuids;

    @CopperListField(name = "members", innerType = PartyMember.class)
    public List<PartyMember> members;

    @NotNull @Override
    public Class<PartyProtos.Party> getProtoClass() {
        return PartyProtos.Party.class;
    }

}
