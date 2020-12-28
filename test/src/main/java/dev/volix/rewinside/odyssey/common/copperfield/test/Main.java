package dev.volix.rewinside.odyssey.common.copperfield.test;

import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonRegistry;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoRegistry;
import dev.volix.rewinside.odyssey.common.copperfield.typeconverter.ZonedDateTimeTypeConverter;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * @author Benedikt Wüller
 */
public class Main {

    public static void main(String[] args) {
        runBsonTest(1_000_000);
        runProtoTest(1_000_000);
    }

    private static void runBsonTest(final int amount) {
        final BsonRegistry registry = new BsonRegistry();
        final long start = System.nanoTime();

        for (int i = 0; i < amount; i++) {
            final PartyMember member = new PartyMember();
            member.uuid = UUID.randomUUID();
            member.rank = "LEADER";

            final Party party = new Party();
            party.id = new ObjectId();
            party.createdAt = Calendar.getInstance().toInstant().atZone(ZoneId.of("Europe/Berlin"));

            party.bannedUuids = new ArrayList<>();
            party.bannedUuids.add(UUID.randomUUID().toString());
            party.bannedUuids.add(UUID.randomUUID().toString());
            party.bannedUuids.add(UUID.randomUUID().toString());

            party.members = new ArrayList<>();
            party.members.add(member);

            final Document serializedBsonParty = registry.write(party, Document.class);
            registry.read(serializedBsonParty, Party.class);
        }

        final long end = System.nanoTime();
        System.out.println("Bson Test: " + (end - start) + " ns (avg " + ((end - start) / amount ) + " ns)");
    }

    private static void runProtoTest(final int amount) {
        final ProtoRegistry registry = new ProtoRegistry();
        registry.setConverter(ZonedDateTime.class, new ZonedDateTimeTypeConverter<>());
        registry.setConverter(ObjectId.class, new ObjectIdProtoTypeConverter());

        final long start = System.nanoTime();

        for (int i = 0; i < amount; i++) {
            final PartyMember member = new PartyMember();
            member.uuid = UUID.randomUUID();
            member.rank = "LEADER";

            final Party party = new Party();
            party.id = new ObjectId();
            party.createdAt = Calendar.getInstance().toInstant().atZone(ZoneId.of("Europe/Berlin"));

            party.bannedUuids = new ArrayList<>();
            party.bannedUuids.add(UUID.randomUUID().toString());
            party.bannedUuids.add(UUID.randomUUID().toString());
            party.bannedUuids.add(UUID.randomUUID().toString());

            party.members = new ArrayList<>();
            party.members.add(member);

            final PartyProtos.Party serializedBsonParty = registry.write(party, party.getProtoClass());
            registry.read(serializedBsonParty, Party.class);
        }

        final long end = System.nanoTime();
        System.out.println("Proto Test: " + (end - start) + " ns (avg " + ((end - start) / amount ) + " ns)");
    }

}
