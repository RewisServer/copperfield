package dev.volix.rewinside.odyssey.common.copperfield.example;

import dev.volix.rewinside.odyssey.common.copperfield.CopperConvertable;
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField;
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperFields;
import dev.volix.rewinside.odyssey.common.copperfield.proto.annotation.CopperProtoClass;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import java.time.OffsetDateTime;

/**
 * @author Benedikt Wüller
 */
@CopperFields
@CopperProtoClass(type = PartyProtos.PartyEvent.class)
public class TimedPartyEvent implements CopperConvertable {

    public OffsetDateTime at;

    public PartyEventType type;

    @CopperField(typeMapper = PartyEventCopperTypeMapper.class)
    public PartyEvent details;

}
