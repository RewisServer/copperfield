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
        final ProtoRegistry protoRegistry = new ProtoRegistry();
        protoRegistry.setConverter(ZonedDateTime.class, new ZonedDateTimeTypeConverter<>());
        protoRegistry.setConverter(ObjectId.class, new ObjectIdProtoTypeConverter());

        final BsonRegistry bsonRegistry = new BsonRegistry();

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

        System.out.println("--- Bson Serialized Party ---");

        final Document serializedBsonParty = bsonRegistry.write(party, Document.class);
        System.out.println(serializedBsonParty.toJson());

        System.out.println("--- Bson Deserialized Party ---");

        final Party deserializedBsonParty = bsonRegistry.read(serializedBsonParty, Party.class);
        System.out.println(bsonRegistry.write(deserializedBsonParty, Document.class).toJson());

        System.out.println("--- Proto Serialized Party ---");

        final PartyProtos.Party serializedProtoParty = protoRegistry.write(party, PartyProtos.Party.class);
        System.out.println(serializedProtoParty);

        System.out.println("--- Proto Deserialized Party ---");

        final Party deserializedProtoParty = protoRegistry.read(serializedProtoParty, Party.class);
        System.out.println(protoRegistry.write(deserializedProtoParty, PartyProtos.Party.class));
    }

}
