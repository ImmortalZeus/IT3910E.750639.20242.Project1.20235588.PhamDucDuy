package models.logger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class secureLoggerFormatter extends Formatter {
    @Override
    public final String format(LogRecord record) {
        if(record.getClass() == LogRecord.class)
        {
            return "[" + record.getLevel() + "]" + "[" + record.getInstant().toString() + "]" + " " + record.getMessage() + System.lineSeparator();
        }
        else
        {
            return "Failed to log";
        }
    }
}
