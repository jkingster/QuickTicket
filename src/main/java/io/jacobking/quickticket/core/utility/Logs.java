package io.jacobking.quickticket.core.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logs {

    private static final Logs INSTANCE = new Logs();

    private final Logger logger;

    public Logs() {
        this.logger = LoggerFactory.getLogger(Logs.class);
    }

    public static Logs getInstance() {
        return INSTANCE;
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

    public static void exception(final Exception e) {
        getInstance().logger.error(e.getMessage());
        e.printStackTrace();
    }
}
