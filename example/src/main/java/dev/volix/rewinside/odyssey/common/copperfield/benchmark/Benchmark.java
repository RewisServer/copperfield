package dev.volix.rewinside.odyssey.common.copperfield.benchmark;

import dev.volix.rewinside.odyssey.common.copperfield.benchmark.party.Party;
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonRegistry;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoRegistry;
import dev.volix.rewinside.odyssey.hagrid.protocol.party.PartyProtos;
import org.bson.Document;

/**
 * @author Benedikt Wüller
 */
public class Benchmark {

    public static void main(String[] args) {
        benchmark("Single Cycle", 1);
        benchmark("100 Cycles", 100);
        benchmark("1.000 Cycles", 1_000);
        benchmark("10.000 Cycles", 10_000);
        benchmark("100.000 Cycles", 100_000);
        benchmark("1.000.000 Cycles", 1_000_000);
        benchmark("10.000.000 Cycles", 10_000_000);
        benchmark("100.000.000 Cycles", 100_000_000);
        benchmark("1.000.000.000 Cycles", 1_000_000_000);
    }

    private static void benchmark(final String title, final int amount) {
        final long bsonTime = benchmarkBson(amount);
        final long protoTime = benchmarkBson(amount);

        final double bsonAvg = (double) bsonTime / amount / 1000000;
        final double protoAvg = (double) protoTime / amount / 1000000;

        System.out.println("--- " + title + " ---");
        System.out.println("Bson | Avg: " + bsonAvg + " ms | Total: " + (bsonTime / 1000000) + " ms");
        System.out.println("Proto | Avg: " + protoAvg + " ms | Total: " + (protoTime / 1000000) + " ms");
    }

    private static long benchmarkBson(final int amount) {
        final long start = System.nanoTime();
        final BsonRegistry registry = new BsonRegistry();

        for (int i = 0; i < amount; i++) {
            final Party party = PartyUtil.createParty();
            final Document serialized = registry.toTheirs(party);
            registry.toOurs(serialized, Party.class);
        }

        return System.nanoTime() - start;
    }

    private static long benchmarkProto(final int amount) {
        final long start = System.nanoTime();
        final ProtoRegistry registry = new ProtoRegistry();

        for (int i = 0; i < amount; i++) {
            final Party party = PartyUtil.createParty();
            final PartyProtos.Party serialized = (PartyProtos.Party) registry.toTheirs(party);
            registry.toOurs(serialized, Party.class);
        }

        return System.nanoTime() - start;
    }

}
