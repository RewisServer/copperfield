package dev.volix.rewinside.odyssey.common.copperfield.benchmark;

import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonRegistry;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoRegistry;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.UUID;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * @author Benedikt Wüller
 */
public class Benchmark {

    public static void main(String[] args) {
        final BsonRegistry bsonRegistry = new BsonRegistry();
        final ProtoRegistry protoRegistry = new ProtoRegistry();

        final Party party = new Party();
        party.id = new ObjectId();
        party.bannedUuids.add(UUID.randomUUID());
        party.bannedUuids.add(UUID.randomUUID());
        party.bannedUuids.add(UUID.randomUUID());
        party.createdAt = Calendar.getInstance().toInstant().atZone(ZoneId.of("Europe/Berlin"));

        final PartyProtos.Party protoParty = (PartyProtos.Party) protoRegistry.toTheirs(party);
        System.out.println(protoParty);

        final Document bsonParty = bsonRegistry.toTheirs(party);
        System.out.println(bsonParty.toJson());

        final Party protoPartyResult = protoRegistry.toOurs(protoParty, Party.class);
        final Party bsonPartyResult = bsonRegistry.toOurs(bsonParty, Party.class);

        final String foo = "bar";
    }

}
