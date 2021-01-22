package dev.volix.rewinside.odyssey.common.copperfield.example;

import com.google.protobuf.Struct;
import dev.volix.rewinside.odyssey.common.copperfield.CopperConvertable;
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField;
import dev.volix.rewinside.odyssey.common.copperfield.proto.annotation.CopperProtoClass;

/**
 * @author Benedikt Wüller
 */
@CopperProtoClass(type = Struct.class)
public class PartySettings implements CopperConvertable {

    @CopperField(name = "cock_length_in_inches", converter = CentimetersToInchesConverter.class)
    public double cockLengthInCentimeters;

}
