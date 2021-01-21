package dev.volix.rewinside.odyssey.common.copperfield.example;

import dev.volix.rewinside.odyssey.common.copperfield.TypeMapper;
import org.jetbrains.annotations.NotNull;

/**
 * @author Benedikt Wüller
 */
public class PartyEventTypeMapper implements TypeMapper<TimedPartyEvent, PartyEvent> {

    @NotNull @Override
    public Class<? extends PartyEvent> map(final TimedPartyEvent instance, @NotNull final Class<?> valueType) {
        return instance.type.type;
    }

    @NotNull @Override
    public String[] getRequiredFieldNames() {
        return new String[] { "type" };
    }

}
