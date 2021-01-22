package dev.volix.rewinside.odyssey.common.copperfield.example;

import com.google.protobuf.MessageLiteOrBuilder;
import dev.volix.rewinside.odyssey.common.copperfield.CopperConvertable;
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperFields;
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperIgnore;
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperValueType;
import dev.volix.rewinside.odyssey.common.copperfield.proto.annotation.CopperProtoClass;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Benedikt Wüller
 */
@CopperFields
@CopperProtoClass(type = PartyProtos.Party.class)
public class Party implements CopperConvertable {

    public UUID id;

    public OffsetDateTime createdAt;

    @CopperIgnore(types = { MessageLiteOrBuilder.class })
    public OffsetDateTime disbandedAt;

    public UUID leaderUuid;

    @CopperValueType(type = PartyMember.class)
    public List<PartyMember> members = new ArrayList<>();

    public PartySettings settings = new PartySettings();

    @CopperValueType(type = UUID.class)
    public List<UUID> bannedUuids = new ArrayList<>();

    @CopperValueType(type = TimedPartyEvent.class)
    public List<TimedPartyEvent> events = new ArrayList<>();

}
