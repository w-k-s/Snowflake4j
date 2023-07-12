package io.wks.snowflake4j.snowflake;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class SnowflakeFactoryTest {

    private static final Instant EPOCH = Instant.ofEpochMilli(1288834974657L);
    private static final Clock FIXED_CLOCK = Clock.fixed(
            LocalDateTime.of(
                    LocalDate.of(2023, Month.JULY, 10),
                    LocalTime.of(17, 30)
            ).toInstant(ZoneOffset.UTC),
            ZoneOffset.UTC
    );

    @Test
    public void shouldGenerateId() {
        final Supplier<Snowflake> factory = SnowflakeFactory
                .forStandaloneNode(EPOCH, FIXED_CLOCK);

        assertThat(factory.get()).isInstanceOfSatisfying(Snowflake.class, id -> {
            assertThat(id.getValue()).isEqualTo(1678456548357181440L);
            assertThat(id.getNodeId()).isEqualTo(1L);
            assertThat(id.getDataCenterId()).isEqualTo(1L);
            assertThat(id.getTimestamp()).isEqualTo(FIXED_CLOCK.millis());
            assertThat(id.getSequence()).isEqualTo(0L);
        });
    }

    @Test
    public void shouldGenerateOneMillionIdsQuickly() {
        final Supplier<Snowflake> factory = SnowflakeFactory
                .forStandaloneNode(EPOCH);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1_000_000; i++) {
            factory.get();
        }
        long duration = System.currentTimeMillis() - start;
        long rate = 1_000_000_000/duration;
        System.out.printf("Generated a million ids in %d millis (rate: %d ids/sec)", duration, rate);
    }
}