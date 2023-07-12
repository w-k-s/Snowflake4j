package io.wks.snowflake4j.snowflake;

import java.util.Objects;

public class Snowflake {

    private final long value;
    private final long nodeId;
    private final long dataCenterId;
    private final long timestamp;
    private final long sequence;

    Snowflake(long value, long nodeId, long dataCenterId, long timestamp, long sequence) {
        this.value = value;
        this.nodeId = nodeId;
        this.dataCenterId = dataCenterId;
        this.timestamp = timestamp;
        this.sequence = sequence;
    }

    public long getValue() {
        return value;
    }

    public long getNodeId() {
        return nodeId;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getSequence() {
        return sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Snowflake snowflake = (Snowflake) o;
        return value == snowflake.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Snowflake{" +
                "value=" + value +
                ", nodeId=" + nodeId +
                ", dataCenterId=" + dataCenterId +
                ", timestamp=" + timestamp +
                ", sequence=" + sequence +
                '}';
    }
}
