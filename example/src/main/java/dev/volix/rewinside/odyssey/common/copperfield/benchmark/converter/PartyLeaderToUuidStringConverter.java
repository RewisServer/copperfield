package dev.volix.rewinside.odyssey.common.copperfield.benchmark.converter;

import dev.volix.rewinside.odyssey.common.copperfield.Registry;
import dev.volix.rewinside.odyssey.common.copperfield.benchmark.party.PartyMember;
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter;
import java.lang.reflect.Field;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Benedikt Wüller
 */
public class PartyLeaderToUuidStringConverter extends Converter<PartyMember, String> {

    public PartyLeaderToUuidStringConverter() {
        super(PartyMember.class, String.class);
    }

    @Nullable @Override
    public String toTheirs(@Nullable final PartyMember value, @Nullable final Field field, @NotNull final Registry<?, ?> registry, @NotNull final Class<? extends PartyMember> type) {
        if (value == null || value.uuid == null) return null;
        return value.uuid.toString();
    }

    @Nullable @Override
    public PartyMember toOurs(@Nullable final String value, @Nullable final Field field, @NotNull final Registry<?, ?> registry, @NotNull final Class<? extends PartyMember> type) {
        if (value == null) return null;
        final PartyMember partyMember = new PartyMember();
        partyMember.uuid = UUID.fromString(value);
        partyMember.rank = "LEADER";
        return partyMember;
    }

}
