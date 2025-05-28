package models.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

public class dateToUTC {
    public static Date convert(Date date) {
        ZoneId zone = ZoneId.systemDefault();
        // Treat the original Date as if it were in GMT+7
        LocalDateTime localTime = LocalDateTime.ofInstant(date.toInstant(), zone);

        // Now reinterpret that same local time as if it's in UTC
        ZonedDateTime utcZoned = localTime.atZone(ZoneOffset.UTC);
        return Date.from(utcZoned.toInstant());
    }
}
