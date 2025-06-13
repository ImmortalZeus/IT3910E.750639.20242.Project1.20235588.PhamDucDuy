package models.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class secureLogger {
    private static final Logger logger = Logger.getLogger(secureLogger.class.getName());

    static {
        // Remove default handlers
        LogManager.getLogManager().reset();

        // Create and configure a console handler
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new secureLoggerFormatter()); // Just the message
        handler.setLevel(Level.ALL);

        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
    }

    public static final void info(String msg) {
        if(msg == null) return;
        secureLogger.logger.info(msg);
    }

    public static final void warning(String msg) {
        if(msg == null) return;
        secureLogger.logger.warning(msg);
    }

    public static final void severe(String msg) {
        if(msg == null) return;
        secureLogger.logger.severe(msg);
    }

    public static final void fine(String msg) {
        if(msg == null) return;
        secureLogger.logger.fine(msg);
    }

    public static final void finer(String msg) {
        if(msg == null) return;
        secureLogger.logger.finer(msg);
    }

    public static final void finest(String msg) {
        if(msg == null) return;
        secureLogger.logger.finest(msg);
    }
}
