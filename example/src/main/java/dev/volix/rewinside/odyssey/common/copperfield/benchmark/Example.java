package dev.volix.rewinside.odyssey.common.copperfield.benchmark;

import dev.volix.rewinside.odyssey.common.copperfield.benchmark.party.Party;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonRegistry;
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.ObjectIdToStringConverter;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoRegistry;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * @author Benedikt Wüller
 */
public class Example {

    public static void main(String[] args) {
        final BsonRegistry bsonRegistry = new BsonRegistry();

        final ProtoRegistry protoRegistry = new ProtoRegistry();
        protoRegistry.setDefaultConverter(ObjectId.class, ObjectIdToStringConverter.class);

        final Party party = PartyUtil.createParty();

        final Document bsonParty = bsonRegistry.toTheirs(party);
        System.out.println(bsonParty.toJson());

        final PartyProtos.Party protoParty = (PartyProtos.Party) protoRegistry.toTheirs(party);
        System.out.println(protoParty);

        final Party protoPartyResult = protoRegistry.toOurs(protoParty, Party.class);
        final Party bsonPartyResult = bsonRegistry.toOurs(bsonParty, Party.class);

        final String foo = "bar";
    }

}
