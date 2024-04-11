package io.jacobking.quickticket.core.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logs {

    private static Logs instance = null;

    private final Logger logger;

    public Logs() {
        this.logger = LoggerFactory.getLogger(Logs.class);
    }

    public static synchronized Logs getInstance() {
        if (instance == null) {
            instance = new Logs();
        }
        return instance;
    }

    public static void warn(final String message, final Object... objects) {
        getInstance().logger.warn(message, objects);
    }

    public static void info(final String message, final Object... objects) {
        getInstance().logger.info(message, objects);
    }

    public static void debug(final String message, final Object... objects) {
        getInstance().logger.debug(message, objects);
    }
}
