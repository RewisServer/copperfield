package dev.volix.rewinside.odyssey.common.copperfield.benchmark;

import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField;
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperIterableType;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonConvertable;
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.CopperBsonName;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertable;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoIgnore;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoType;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bson.types.ObjectId;

/**
 * @author Benedikt Wüller
 */
@CopperProtoType(type = PartyProtos.Party.class)
public class Party implements BsonConvertable, ProtoConvertable<PartyProtos.Party> {

    @CopperField(name = "id")
    @CopperBsonName(name = "_id")
    @CopperProtoIgnore // TODO: write converter
    public ObjectId id;

    @CopperField(name = "created_at")
    public ZonedDateTime createdAt;

    @CopperField(name = "banned_uuids")
    @CopperIterableType(innerType = UUID.class)
    public List<UUID> bannedUuids = new ArrayList<>();

}
