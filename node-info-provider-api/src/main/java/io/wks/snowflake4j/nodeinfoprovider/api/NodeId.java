package io.wks.snowflake4j.nodeinfoprovider.api;

import java.util.Objects;
import java.util.function.Function;

public class NodeId {
    private final long value;

    private NodeId(long value) {
        this.value = value;
    }

    public static NodeId of(long value) {
        return new NodeId(value);
    }

    public static NodeId of(String value) {
        return NodeId.of(value, String::hashCode);
    }

    public static NodeId of(String value, Function<String, Integer> hashingAlgorithm) {
        Objects.requireNonNull(value, "nodeId value must not be null");
        if (value.isEmpty()) {
            throw new IllegalArgumentException("nodeId can not be blank");
        }
        return new NodeId(hashingAlgorithm.apply(value));
    }

    public long getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeId nodeId = (NodeId) o;
        return value == nodeId.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
