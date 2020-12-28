package dev.volix.rewinside.odyssey.common.copperfield.test;

import dev.volix.rewinside.odyssey.common.copperfield.CopperField;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonConvertible;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonRegistry;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertible;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoRegistry;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import java.util.UUID;
import org.bson.Document;

/**
 * @author Benedikt Wüller
 */
public class Test implements BsonConvertible, ProtoConvertible<PartyProtos.Party> {

    @CopperField(name = "id")
    private String id;

    @CopperField(name = "leader_uuid")
    private String leaderUuid;

    public static void main(String[] args) {
        final BsonRegistry bsonRegistry = new BsonRegistry();
        final ProtoRegistry protoRegistry = new ProtoRegistry();

        System.out.println(new Test().toBsonDocument(bsonRegistry).toJson());

        final Test instance = new Test();
        instance.id = UUID.randomUUID().toString();
        instance.leaderUuid = UUID.randomUUID().toString();

        final Document document = instance.toBsonDocument(bsonRegistry);
        System.out.println(document.toJson());

        final PartyProtos.Party party = instance.toProtoMessage(PartyProtos.Party.class, protoRegistry);
        System.out.println(party);

        final Test anotherInstance = new Test();
        anotherInstance.fromBsonDocument(document, bsonRegistry);

        final Test yetAnotherInstance = new Test();
        yetAnotherInstance.fromProtoMessage(party, protoRegistry);

        final String foo = "bar";
    }

}
