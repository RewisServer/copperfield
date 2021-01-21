package dev.volix.rewinside.odyssey.common.copperfield.example;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import dev.volix.rewinside.odyssey.common.copperfield.CopperConvertable;
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField;
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
@CopperProtoClass(type = PartyProtos.Party.class)
public class Party implements CopperConvertable {

    @CopperField
    public UUID id;

    @CopperField
    public OffsetDateTime createdAt;

    @CopperField
    public OffsetDateTime disbandedAt;

    @CopperField
    public UUID leaderUuid;

    @CopperField
    @CopperValueType(type = PartyMember.class)
    public List<PartyMember> members = new ArrayList<>();

    @CopperField
    public PartySettings settings = new PartySettings();

    @CopperField
    @CopperValueType(type = UUID.class)
    public List<UUID> bannedUuids = new ArrayList<>();

    @CopperField
    @CopperValueType(type = TimedPartyEvent.class)
    public List<TimedPartyEvent> events = new ArrayList<>();

}
