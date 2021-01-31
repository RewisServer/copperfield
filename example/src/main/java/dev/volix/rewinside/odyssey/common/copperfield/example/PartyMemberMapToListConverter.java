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
        final ArrayList<Object> list = new ArrayList<>();
        if (value == null) return list;
        for (final Object object : value.values()) {
            list.add(agent.toTheirs(object, PartyMember.class, contextType, field));
        }
        return list;
    }

    @Nullable @Override
    public Map toOurs(@Nullable final List value, @NotNull final CopperfieldAgent agent, @NotNull final Class<? extends Map> ourType, @NotNull final Class<?> contextType, @Nullable final Field field) {
        final Map<String, PartyMember> map = new HashMap<>();
        if (value == null) return null;
        for (Object object : value) {
            final PartyMember member = agent.toOurs(object, PartyMember.class, contextType, field);
            map.put(member.uuid.toString(), member);
        }
        return map;
    }

}
