package com.vagivagi.connector.common;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class LifeUtil {
    public static final ZoneId ZONE_TOKYO = ZoneId.of("Asia/Tokyo");
    public boolean isSleeping() {
        int nowHour = LocalDateTime.now(ZONE_TOKYO).getHour();
        return 0 <= nowHour && nowHour <= 7;
    }
}
