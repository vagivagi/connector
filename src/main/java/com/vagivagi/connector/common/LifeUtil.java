package com.vagivagi.connector.common;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class LifeUtil {
    public static final ZoneId ZONE_TOKYO = ZoneId.of("Asia/Tokyo");
    private final Clock clock;

    public LifeUtil(Clock clock) {
        this.clock = clock;
    }

    public boolean isSleeping() {
        int nowHour = LocalDateTime.now(clock).getHour();
        return 0 <= nowHour && nowHour <= 7;
    }
}
