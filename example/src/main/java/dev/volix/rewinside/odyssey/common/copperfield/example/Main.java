package dev.volix.rewinside.odyssey.common.copperfield.example;

import dev.volix.rewinside.odyssey.common.copperfield.Registry;
import dev.volix.rewinside.odyssey.common.copperfield.bson.template.BsonTemplate;
import dev.volix.rewinside.odyssey.common.copperfield.converter.OffsetDateTimeToStringConverter;
import dev.volix.rewinside.odyssey.common.copperfield.proto.template.ProtoTemplate;
import dev.volix.rewinside.odyssey.common.copperfield.template.DefaultTemplate;
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

        final Registry registry = new Registry(new DefaultTemplate()
            .with(new BsonTemplate())
            .with(new ProtoTemplate())
            .with(OffsetDateTime.class, OffsetDateTimeToStringConverter.class, ProtoTemplate.FORMAT));

        final Document document = (Document) registry.toTheirs(party, Party.class, BsonTemplate.FORMAT);
        final String json = document.toJson();
        final Party bsonCopy = (Party) registry.toOurs(document, Party.class, BsonTemplate.FORMAT);

        final PartyProtos.Party proto = (PartyProtos.Party) registry.toTheirs(party, Party.class, ProtoTemplate.FORMAT);
        final Party protoCopy = (Party) registry.toOurs(proto, Party.class, ProtoTemplate.FORMAT);

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
        party.members.add(member);

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
