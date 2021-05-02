package com.vagivagi.connector.common;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

public class LifeUtilTest {
    private LifeUtil lifeUtil;

    @Test
    public void noon() {
        LocalDateTime localDateTime = LocalDateTime.of(2021, 4, 1, 14, 59);
        Clock clock = Clock.fixed(localDateTime.toInstant(ZoneOffset.UTC), ZoneId.of("Asia/Tokyo"));
        lifeUtil = new LifeUtil(clock);
        assertThat(lifeUtil.isSleeping()).isFalse();
    }

    @Test
    public void start_night_sleeping() {
        LocalDateTime localDateTime = LocalDateTime.of(2021, 4, 2, 15, 0);
        Clock clock = Clock.fixed(localDateTime.toInstant(ZoneOffset.UTC), ZoneId.of("Asia/Tokyo"));
        lifeUtil = new LifeUtil(clock);
        assertThat(lifeUtil.isSleeping()).isTrue();
    }

    @Test
    public void end_night_sleeping() {
        LocalDateTime localDateTime = LocalDateTime.of(2021, 4, 1, 22, 59);
        Clock clock = Clock.fixed(localDateTime.toInstant(ZoneOffset.UTC), ZoneId.of("Asia/Tokyo"));
        lifeUtil = new LifeUtil(clock);
        assertThat(lifeUtil.isSleeping()).isTrue();
    }

    @Test
    public void morning() {
        LocalDateTime localDateTime = LocalDateTime.of(2021, 4, 1, 23, 0);
        Clock clock = Clock.fixed(localDateTime.toInstant(ZoneOffset.UTC), ZoneId.of("Asia/Tokyo"));
        lifeUtil = new LifeUtil(clock);
        assertThat(lifeUtil.isSleeping()).isFalse();
    }
}
