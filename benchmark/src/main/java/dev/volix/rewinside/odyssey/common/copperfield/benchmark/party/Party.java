package dev.volix.rewinside.odyssey.common.copperfield.benchmark.party;

import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField;
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperIterableType;
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperMapTypes;
import dev.volix.rewinside.odyssey.common.copperfield.benchmark.converter.PartyLeaderToUuidStringConverter;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonConvertable;
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.CopperBsonName;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertable;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoIgnore;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoType;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @CopperField(name = "leader_uuid", converter = PartyLeaderToUuidStringConverter.class)
    public PartyMember leader;

    @CopperField(name = "members")
    @CopperIterableType(innerType = PartyMember.class)
    public List<PartyMember> members = new ArrayList<>();

    @CopperField(name = "settings")
    @CopperMapTypes(keyType = String.class, valueType = Object.class)
    @CopperProtoIgnore // TODO: write converter
    public Map<String, Object> settings = new HashMap<>();

    @CopperField(name = "banned_uuids")
    @CopperIterableType(innerType = UUID.class)
    public List<UUID> bannedUuids = new ArrayList<>();

    @CopperField(name = "events")
    @CopperIterableType(innerType = PartyEvent.class)
    public List<PartyEvent> events = new ArrayList<>();

}
