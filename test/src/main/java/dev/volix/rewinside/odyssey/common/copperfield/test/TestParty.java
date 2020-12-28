package dev.volix.rewinside.odyssey.common.copperfield.test;

import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonConvertible;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonRegistry;
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.CopperBsonField;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertible;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoRegistry;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * @author Benedikt Wüller
 */
public class TestParty implements BsonConvertible, ProtoConvertible {

    @CopperBsonField(name = "_id")
    private ObjectId id;

    @CopperField(name = "created_at")
    private ZonedDateTime createdAt;

    @CopperField(name = "banned_uuids")
    private List<String> bannedUuids;

    @CopperBsonField(name = "number")
    private Float value;

    public static void main(String[] args) {
        final ProtoRegistry protoRegistry = new ProtoRegistry();
        final BsonRegistry bsonRegistry = new BsonRegistry();

        final TestParty testParty = new TestParty();
        testParty.id = new ObjectId();
        testParty.createdAt = Calendar.getInstance().toInstant().atZone(ZoneId.of("Europe/Berlin"));
        testParty.bannedUuids = new ArrayList<>();
        testParty.bannedUuids.add(UUID.randomUUID().toString());
        testParty.bannedUuids.add(UUID.randomUUID().toString());
        testParty.bannedUuids.add(UUID.randomUUID().toString());
        testParty.value = 0.25f;

        System.out.println("--- Bson Serialized Party ---");

        final Document serializedBsonParty = bsonRegistry.write(testParty, Document.class);
        System.out.println(serializedBsonParty.toJson());

        System.out.println("--- Bson Deserialized Party ---");

        final TestParty deserializedBsonParty = bsonRegistry.read(serializedBsonParty, TestParty.class);
        System.out.println(bsonRegistry.write(deserializedBsonParty, Document.class).toJson());

        System.out.println("--- Proto Serialized Party ---");

        final PartyProtos.Party serializedProtoParty = protoRegistry.write(testParty, PartyProtos.Party.class);
        System.out.println(serializedProtoParty);

        System.out.println("--- Proto Deserialized Party ---");

        final TestParty deserializedProtoParty = protoRegistry.read(serializedProtoParty, TestParty.class);
        System.out.println(protoRegistry.write(deserializedProtoParty, PartyProtos.Party.class));

//        System.out.println(party.toBsonDocument(bsonRegistry));
//        System.out.println(party.toProtoMessage(protoRegistry));
    }

}
