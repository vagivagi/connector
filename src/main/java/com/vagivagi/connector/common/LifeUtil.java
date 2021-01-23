package com.vagivagi.connector.common;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class LifeUtil {
    public boolean isSleeping() {
        int nowHour = LocalDateTime.now(ZoneId.of("Asia/Tokyo")).getHour();
        return 0 <= nowHour && nowHour <= 7;
    }
}
