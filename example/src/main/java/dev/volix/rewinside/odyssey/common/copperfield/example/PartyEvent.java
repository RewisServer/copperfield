package dev.volix.rewinside.odyssey.common.copperfield.example;

import com.google.protobuf.Struct;
import dev.volix.rewinside.odyssey.common.copperfield.CopperConvertable;
import dev.volix.rewinside.odyssey.common.copperfield.proto.annotation.CopperProtoClass;

/**
 * @author Benedikt Wüller
 */
@CopperProtoClass(type = Struct.class)
public interface PartyEvent extends CopperConvertable {
}
