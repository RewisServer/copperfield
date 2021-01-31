package dev.volix.rewinside.odyssey.common.copperfield.example;

import dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent;
import dev.volix.rewinside.odyssey.common.copperfield.bson.registry.BsonRegistry;
import dev.volix.rewinside.odyssey.common.copperfield.proto.registry.ProtoRegistry;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;
import org.bson.Document;

/**
 * @author Benedikt Wüller
 */
public class Main {

    public static void main(String[] args) {
        final Party party = createParty();

        final CopperfieldAgent agent = new CopperfieldAgent(new BsonRegistry(), new ProtoRegistry());

        final Document document = agent.toTheirs(party, Document.class);
        System.out.println(document.toJson());
        final Party bsonCopy = agent.toOurs(document, Party.class);

        final PartyProtos.Party proto = agent.toTheirs(party, PartyProtos.Party.class);
        System.out.println(proto.toString());
        final Party protoCopy = agent.toOurs(proto, Party.class);

        final String foo = "bar";
    }

    private static Party createParty() {
        final Party party = new Party();
        party.id = UUID.randomUUID();
        party.createdAt = OffsetDateTime.now(ZoneId.of("CET"));
        party.disbandedAt = OffsetDateTime.now(ZoneId.of("CET"));
        party.leaderUuid = UUID.randomUUID();

        final PartyMember member = new PartyMember();
        member.uuid = UUID.randomUUID();
        member.rank = "MEMBER";
        party.members.put(member.uuid.toString(), member);

        party.settings.cockLengthInCentimeters = 25.0;

        party.bannedUuids.add(UUID.randomUUID());
        party.bannedUuids.add(UUID.randomUUID());
        party.bannedUuids.add(UUID.randomUUID());

        final TimedPartyEvent event = new TimedPartyEvent();
        event.at = OffsetDateTime.now(ZoneId.of("CET"));
        event.type = PartyEventType.PARTY_CREATED_EVENT;

        final PartyCreatedEvent eventDetails = new PartyCreatedEvent();
        eventDetails.byUuid = UUID.randomUUID();
        event.details = eventDetails;

        party.events.add(event);

        return party;
    }

}
