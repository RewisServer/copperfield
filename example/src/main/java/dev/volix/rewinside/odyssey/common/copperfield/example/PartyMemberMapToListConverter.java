package dev.volix.rewinside.odyssey.common.copperfield.example;

import dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent;
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Benedikt Wüller
 */
public class PartyMemberMapToListConverter extends Converter<Map, List> {

    public PartyMemberMapToListConverter() {
        super(Map.class, List.class);
    }

    @Nullable @Override
    public List toTheirs(@Nullable final Map value, @NotNull final CopperfieldAgent agent, @NotNull final Class<? extends Map> ourType, @NotNull final Class<?> contextType, @Nullable final Field field) {
        if (value == null) return new ArrayList<>();
        return (List) agent.toTheirs(value.values(), List.class, contextType, field);
    }

    @Nullable @Override
    public Map toOurs(@Nullable final List value, @NotNull final CopperfieldAgent agent, @NotNull final Class<? extends Map> ourType, @NotNull final Class<?> contextType, @Nullable final Field field) {
        final Map<String, PartyMember> map = new HashMap<>();

        final List<PartyMember> members = agent.toOurs(value, List.class, contextType, field);
        if (members == null) return map;

        for (PartyMember member : members) {
            map.put(member.uuid.toString(), member);
        }

        return map;
    }

}

