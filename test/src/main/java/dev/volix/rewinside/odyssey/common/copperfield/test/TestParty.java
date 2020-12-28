package dev.volix.rewinside.odyssey.common.copperfield.test;

import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField;
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperListField;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonConvertible;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonRegistry;
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.CopperBsonField;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertible;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoRegistry;
import dev.volix.rewinside.odyssey.common.copperfield.typeconverter.ZonedDateTimeTypeConverter;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

/**
 * @author Benedikt Wüller
 */
public class TestParty implements BsonConvertible, ProtoConvertible<PartyProtos.Party> {

    @CopperBsonField(name = "_id")
    private ObjectId id;

    @CopperField(name = "created_at")
    private ZonedDateTime createdAt;

    @CopperListField(name = "banned_uuids", innerType = String.class)
    private List<String> bannedUuids;

    @CopperListField(name = "members", innerType = TestPartyMember.class)
    private List<TestPartyMember> members;

    private Float value;

    @NotNull @Override
    public Class<PartyProtos.Party> getProtoClass() {
        return PartyProtos.Party.class;
    }

    public static class TestPartyMember implements BsonConvertible, ProtoConvertible<PartyProtos.PartyMember> {

        @CopperField(name = "uuid")
        private UUID uuid;

        @CopperField(name = "rank")
        private String rank;

        @NotNull @Override
        public Class<PartyProtos.PartyMember> getProtoClass() {
            return PartyProtos.PartyMember.class;
        }

    }

    public static void main(String[] args) {
        final ProtoRegistry protoRegistry = new ProtoRegistry();
        protoRegistry.setConverter(ZonedDateTime.class, new ZonedDateTimeTypeConverter<>());

        final BsonRegistry bsonRegistry = new BsonRegistry();

        final TestPartyMember member = new TestPartyMember();
        member.uuid = UUID.randomUUID();
        member.rank = "LEADER";

        final TestParty testParty = new TestParty();
        testParty.id = new ObjectId();
        testParty.createdAt = Calendar.getInstance().toInstant().atZone(ZoneId.of("Europe/Berlin"));

        testParty.bannedUuids = new ArrayList<>();
        testParty.bannedUuids.add(UUID.randomUUID().toString());
        testParty.bannedUuids.add(UUID.randomUUID().toString());
        testParty.bannedUuids.add(UUID.randomUUID().toString());

        testParty.members = new ArrayList<>();
        testParty.members.add(member);

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
    }

}
