package dev.volix.rewinside.odyssey.common.copperfield.test;

import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonConvertible;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertible;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoRegistry;
import dev.volix.rewinside.odyssey.hagrid.protocol.Packet;
import dev.volix.rewinside.odyssey.hagrid.protocol.StatusCode;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * @author Benedikt Wüller
 */
public class TestPacket implements BsonConvertible, ProtoConvertible<Packet> {

    @CopperField(name = "id")
    private UUID id;

    @CopperField(name = "request_id")
    private UUID requestId;

    @CopperField(name = "status")
    private Status status;

    @CopperField(name = "payload")
    private Payload payload;

    @Override @NotNull
    public Class<Packet> getProtoClass() {
        return Packet.class;
    }

    public static class Status implements BsonConvertible, ProtoConvertible<dev.volix.rewinside.odyssey.hagrid.protocol.Status> {

        @CopperField(name = "code")
        private StatusCode code;

        @CopperField(name = "message")
        private String message;

        @Override @NotNull
        public Class<dev.volix.rewinside.odyssey.hagrid.protocol.Status> getProtoClass() {
            return dev.volix.rewinside.odyssey.hagrid.protocol.Status.class;
        }

    }

    public static class Payload implements BsonConvertible, ProtoConvertible<Packet.Payload> {

        @CopperField(name = "type_url")
        private String typeUrl;

        @Override @NotNull
        public Class<Packet.Payload> getProtoClass() {
            return Packet.Payload.class;
        }

    }

    public static void main(String[] args) {
        final ProtoRegistry protoRegistry = new ProtoRegistry();

        final TestPacket testPacket = new TestPacket();
        testPacket.id = UUID.randomUUID();
        testPacket.requestId = UUID.randomUUID();

        testPacket.status = new Status();
        testPacket.status.code = StatusCode.OK;
        testPacket.status.message = "Success";

        testPacket.payload = new Payload();
        testPacket.payload.typeUrl = "https://www.google.com";

        final Packet packet = testPacket.toProtoMessage(protoRegistry);
        System.out.println(packet);

        final TestPacket result = new TestPacket();
        result.fromProtoMessage(packet, protoRegistry);

        final String foo = "bar";
    }

}
