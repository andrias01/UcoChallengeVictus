package co.edu.uco.backendvictus.crosscutting.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Centralizes the creation of loggers to avoid exposing logging frameworks to every layer.
 */
public final class LoggerHelper {

    private LoggerHelper() {
        // Utility class
    }

    public static Logger getLogger(final Class<?> source) {
        return LoggerFactory.getLogger(source);
    }

    public static void info(final Class<?> source, final String message, final Object... arguments) {
        getLogger(source).info(message, arguments);
    }

    public static void warn(final Class<?> source, final String message, final Object... arguments) {
        getLogger(source).warn(message, arguments);
    }

    public static void debug(final Class<?> source, final String message, final Object... arguments) {
        getLogger(source).debug(message, arguments);
    }

    public static void error(final Class<?> source, final String message, final Object... arguments) {
        getLogger(source).error(message, arguments);
    }
}
