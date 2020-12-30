package dev.volix.rewinside.odyssey.common.copperfield.benchmark;

import dev.volix.rewinside.odyssey.common.copperfield.benchmark.party.Party;
import dev.volix.rewinside.odyssey.common.copperfield.benchmark.party.PartyEvent;
import dev.volix.rewinside.odyssey.common.copperfield.benchmark.party.PartyMember;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonRegistry;
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.ObjectIdToStringConverter;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoRegistry;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * @author Benedikt Wüller
 */
public class Example {

    public static void main(String[] args) {
        final BsonRegistry bsonRegistry = new BsonRegistry();

        final ProtoRegistry protoRegistry = new ProtoRegistry();
        protoRegistry.setConverter(ObjectId.class, ObjectIdToStringConverter.class);

        final Party party = createParty();

        final PartyProtos.Party protoParty = (PartyProtos.Party) protoRegistry.toTheirs(party);
        System.out.println(protoParty);

        final Document bsonParty = bsonRegistry.toTheirs(party);
        System.out.println(bsonParty.toJson());

        final Party protoPartyResult = protoRegistry.toOurs(protoParty, Party.class);
        final Party bsonPartyResult = bsonRegistry.toOurs(bsonParty, Party.class);

        final String foo = "bar";
    }

    private static Party createParty() {
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

    private static PartyMember createPartyMember(final String rank) {
        final PartyMember partyMember = new PartyMember();
        partyMember.uuid = UUID.randomUUID();
        partyMember.rank = rank;
        return partyMember;
    }

    private static PartyEvent createPartyEvent(final String type) {
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
