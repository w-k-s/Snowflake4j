package io.wks.snowflake4j.nodeinfoprovider.api;

import java.util.Objects;
import java.util.function.Function;

public class DataCenterId {

    private final long value;

    private DataCenterId(long value) {
        this.value = value;
    }

    public static DataCenterId of(long value) {
        return new DataCenterId(value);
    }

    public static DataCenterId of(String value) {
        return DataCenterId.of(value, String::hashCode);
    }

    public static DataCenterId of(String value, Function<String, Integer> hashingAlgorithm) {
        Objects.requireNonNull(value, "nodeId value must not be null");
        Objects.requireNonNull(hashingAlgorithm, "hashingAlgorithm must not be null");
        if (value.isEmpty()) {
            throw new IllegalArgumentException("nodeId can not be blank");
        }
        return new DataCenterId(hashingAlgorithm.apply(value));
    }

    public long getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataCenterId id = (DataCenterId) o;
        return value == id.value;
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
