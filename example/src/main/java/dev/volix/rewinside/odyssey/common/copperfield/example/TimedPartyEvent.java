package dev.volix.rewinside.odyssey.common.copperfield.example;

import dev.volix.rewinside.odyssey.common.copperfield.CopperConvertable;
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField;
import dev.volix.rewinside.odyssey.common.copperfield.proto.annotation.CopperProtoClass;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import java.time.OffsetDateTime;

/**
 * @author Benedikt Wüller
 */
@CopperProtoClass(type = PartyProtos.PartyEvent.class)
public class TimedPartyEvent implements CopperConvertable {

    @CopperField
    public OffsetDateTime at;

    @CopperField
    public PartyEventType type;

    @CopperField(typeMapper = PartyEventTypeMapper.class)
    public PartyEvent details;

}
