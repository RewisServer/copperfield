package dev.volix.rewinside.odyssey.common.copperfield.benchmark;

import dev.volix.rewinside.odyssey.common.copperfield.benchmark.party.Party;
import dev.volix.rewinside.odyssey.common.copperfield.benchmark.party.PartyEvent;
import dev.volix.rewinside.odyssey.common.copperfield.benchmark.party.PartyMember;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bson.types.ObjectId;

/**
 * @author Benedikt Wüller
 */
public class PartyUtil {

    public static Party createParty() {
        final Party party = new Party();
        party.id = new ObjectId();
        party.createdAt = Calendar.getInstance().toInstant().atZone(ZoneId.of("Europe/Berlin"));
        party.leader = createPartyMember("LEADER");

        party.bannedUuids.add(UUID.randomUUID());
        party.bannedUuids.add(UUID.randomUUID());

        party.members.add(party.leader);
        party.members.add(createPartyMember("MEMBER"));
        party.members.add(createPartyMember("MEMBER"));
        party.members.add(createPartyMember("MEMBER"));

        party.settings.put("max_size", 10);
        party.settings.put("topic", "BedWars");

        party.events.add(createPartyEvent("WET"));
        party.events.add(createPartyEvent("ASS"));
        party.events.add(createPartyEvent("PUSSY"));

        return party;
    }

    public static PartyMember createPartyMember(final String rank) {
        final PartyMember partyMember = new PartyMember();
        partyMember.uuid = UUID.randomUUID();
        partyMember.rank = rank;
        return partyMember;
    }

    public static PartyEvent createPartyEvent(final String type) {
        final PartyEvent event = new PartyEvent();
        event.at = Calendar.getInstance().toInstant().atZone(ZoneId.of("Europe/Berlin"));
        event.type = type;
        event.details.put("some_key", "some_value");

        final Map<String, Object> test = new HashMap<>();
        test.put("another_key", "another_value");
        event.details.put("test", test);

        return event;
    }

}
