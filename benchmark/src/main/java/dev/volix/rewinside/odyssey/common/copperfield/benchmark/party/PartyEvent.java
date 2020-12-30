package dev.volix.rewinside.odyssey.common.copperfield.benchmark.party;

import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField;
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperMapTypes;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonConvertable;
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.CopperBsonField;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertable;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoType;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Benedikt Wüller
 */
@CopperProtoType(type = PartyProtos.PartyEvent.class)
public class PartyEvent implements BsonConvertable, ProtoConvertable<PartyProtos.PartyEvent> {

    @CopperField(name = "at")
    public ZonedDateTime at;

    @CopperField(name = "type")
    public String type;

    @CopperBsonField(name = "details") @CopperMapTypes(keyType = String.class, valueType = Object.class) // TODO: write proto converter
    public Map<String, Object> details = new HashMap<>();

}
