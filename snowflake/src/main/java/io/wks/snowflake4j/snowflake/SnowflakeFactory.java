package io.wks.snowflake4j.snowflake;

import io.wks.snowflake4j.standalone.StandaloneNodeInfoSupplier;
import io.wks.snowflake4j.nodeinfoprovider.api.NodeInfo;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class SnowflakeFactory implements Supplier<Snowflake> {

    // NODE ID
    private static final long NUM_NODE_ID_BITS = 5L;
    private static final long MAX_NODE_ID = 0b11111;

    // DATA CENTER ID

    private static final long NUM_DATA_CENTER_ID_BITS = 5L;
    private static final long MAX_DATA_CENTER_ID = 0b11111;

    // SEQUENCE
    private static final short NUM_SEQUENCE_ID_BITS = 12;
    private static final long SEQUENCE_MASK = 0b111111111111;

    private volatile long sequence = 0;

    // TIMESTAMP
    private volatile long lastTimestamp = 0;

    // SHIFTS
    private static final long SHL_TIMESTAMP = NUM_SEQUENCE_ID_BITS + NUM_NODE_ID_BITS + NUM_DATA_CENTER_ID_BITS;
    private static final long SHL_DATACENTER_ID = NUM_SEQUENCE_ID_BITS + NUM_NODE_ID_BITS;
    private static final long SHL_NODE_ID = NUM_SEQUENCE_ID_BITS;

    // Properties
    private final Clock clock;
    private final long epoch;
    private final long nodeId;
    private final long dataCenterId;

    private SnowflakeFactory(
            Clock clock,
            Instant epoch,
            long nodeId,
            long dataCenterId
    ) {
        Objects.requireNonNull(clock, "clock");
        Objects.requireNonNull(epoch, "epoch");

        this.clock = clock;
        this.epoch = epoch.toEpochMilli();
        this.nodeId = nodeId;
        this.dataCenterId = dataCenterId;
    }

    @Override
    public synchronized Snowflake get() {
        long timestamp = clock.millis();

        if (lastTimestamp > timestamp) {
            long past = lastTimestamp - timestamp;
            throw new InvalidSystemClockException(
                    String.format("Clock moved backwards. " +
                                    "A Snowflake was requested for a timestamp that is %d earlier than the previously requested timestamp. " +
                                    "An earlier timestamp could result in a non-unique snowflake. ",
                            past
                    )
            );
        }

        if (lastTimestamp == timestamp) {
            // If an id has already been requested within this timestamp, then increment the sequence number.
            // Ensure, though, that the sequence is no longer than 12 bits.
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0L) {
                // If after masking, the sequence is 0, then use a different timestamp
                timestamp = nextTimestampAfter(lastTimestamp);
            }
        } else {
            // If this is the first id generated at this timestamp, use sequence = 0.
            sequence = 0;
        }

        lastTimestamp = timestamp;

        final long snowflake = timestamp - epoch << SHL_TIMESTAMP |
                dataCenterId << SHL_DATACENTER_ID |
                nodeId << SHL_NODE_ID |
                sequence;

        return new Snowflake(
                snowflake,
                nodeId,
                dataCenterId,
                timestamp,
                sequence
        );
    }

    private long nextTimestampAfter(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        if (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    public static Builder builder(Instant epoch) {
        return new Builder(epoch);
    }

    public static SnowflakeFactory forStandaloneNode(Instant epoch) {
        return SnowflakeFactory.builder(epoch)
                .build();
    }

    public static SnowflakeFactory forStandaloneNode(Instant epoch, Clock clock) {
        return SnowflakeFactory.builder(epoch)
                .withClock(clock)
                .build();
    }

    public static class Builder {

        private final Instant epoch;
        private Clock clock;
        private Supplier<NodeInfo> nodeInfoSupplier;

        Builder(Instant epoch) {
            this.clock = Clock.systemUTC();
            this.epoch = epoch;
            this.nodeInfoSupplier = StandaloneNodeInfoSupplier.INSTANCE;
        }

        public Builder withNodeInfoSupplier(Supplier<NodeInfo> nodeInfoSupplier) {
            this.nodeInfoSupplier = nodeInfoSupplier;
            return this;
        }

        public Builder withClock(Clock clock) {
            this.clock = clock;
            return this;
        }

        public SnowflakeFactory build() {
            final NodeInfo nodeInfo = nodeInfoSupplier.get();
            final long nodeId = nodeInfo.getNodeId().getValue() & MAX_NODE_ID;
            final long dataCenterId = nodeInfo.getDataCenterId().getValue() & MAX_DATA_CENTER_ID;

            return new SnowflakeFactory(
                    clock,
                    epoch,
                    nodeId,
                    dataCenterId
            );
        }
    }
}
