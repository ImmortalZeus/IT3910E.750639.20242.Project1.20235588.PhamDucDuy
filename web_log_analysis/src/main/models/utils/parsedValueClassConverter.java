package models.utils;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
// import java.time.OffsetDateTime;
// import java.time.format.DateTimeFormatter;
// import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Date;

public class parsedValueClassConverter {
    public static final HashMap<String, Object> fix(HashMap<String, Object> map) {
        if(map.containsKey("response")) {
            try {
                if (map.get("response") instanceof String) {
                    map.put("response_status_code", Integer.valueOf((String) map.get("response")));
                } else if (map.get("response") instanceof Integer) {
                    map.put("response_status_code", Integer.valueOf((Integer) map.get("response")));
                } else {
                    map.put("response_status_code", null);
                }
            } catch (Exception e) {
                map.put("response_status_code", null);
            } finally {
                map.remove("response");
            }
        }

        if(map.containsKey("bytes")) {
            try {
                if (map.get("bytes") instanceof String) {
                    map.put("bytes", Integer.valueOf((String) map.get("bytes")));
                } else if (map.get("bytes") instanceof Integer) {
                    map.put("bytes", Integer.valueOf((Integer) map.get("bytes")));
                } else {
                    map.put("bytes", null);
                }
            } catch (Exception e) {
                map.put("bytes", null);
            }
        }

        if(map.containsKey("time")) {
            try {
                if (map.get("time") instanceof String) {
                    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
                    // OffsetDateTime dateTime = OffsetDateTime.parse((String) map.get("time"), formatter);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
                    Date date = sdf.parse((String) map.get("time"));
                    map.put("time", new Date(date.getTime()));
                    // SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");
                    // map.put("time", formatter.format(Date.from(date.toInstant().truncatedTo(ChronoUnit.SECONDS))));
                } else {
                    map.put("time", null);
                }
            } catch (Exception e) {
                map.put("time", null);
            }
        }
        return map;
    }
}
