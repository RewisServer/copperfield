package dev.volix.rewinside.odyssey.common.copperfield.example;

import dev.volix.rewinside.odyssey.common.copperfield.CopperTypeMapper;
import org.jetbrains.annotations.NotNull;

/**
 * @author Benedikt Wüller
 */
public class PartyEventCopperTypeMapper extends CopperTypeMapper<TimedPartyEvent, PartyEvent> {

    public PartyEventCopperTypeMapper() {
        super("type");
    }

    @NotNull @Override
    public Class<? extends PartyEvent> mapType(final TimedPartyEvent instance, @NotNull final Class<?> valueType) {
        return instance.type.type;
    }

}
