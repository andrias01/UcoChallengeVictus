package co.edu.uco.backendvictus.crosscutting.helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Date helper providing safe parsing and default handling.
 */
public final class DateHelper {

    public static final LocalDate DEFAULT_DATE = LocalDate.of(1900, 1, 1);
    public static final LocalDateTime DEFAULT_DATE_TIME = LocalDateTime.of(1900, 1, 1, 0, 0);
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    private DateHelper() {
        // Utility class
    }

    public static boolean isNull(final LocalDate date) {
        return ObjectHelper.isNull(date);
    }

    public static LocalDate getDefault(final LocalDate date, final LocalDate defaultValue) {
        return ObjectHelper.getDefault(date, defaultValue);
    }

    public static LocalDate getDefault(final LocalDate date) {
        return getDefault(date, DEFAULT_DATE);
    }

    public static LocalDate parseDate(final String value) {
        return parseDate(value, DEFAULT_DATE_FORMAT);
    }

    public static LocalDate parseDate(final String value, final String format) {
        try {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(ObjectHelper.getDefault(value, TextHelper.EMPTY), formatter);
        } catch (final DateTimeParseException exception) {
            return DEFAULT_DATE;
        }
    }

    public static String formatDate(final LocalDate date) {
        return formatDate(date, DEFAULT_DATE_FORMAT);
    }

    public static String formatDate(final LocalDate date, final String format) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return getDefault(date).format(formatter);
    }

    public static boolean isEqual(final LocalDate first, final LocalDate second) {
        return getDefault(first).isEqual(getDefault(second));
    }

    public static boolean isEmpty(final LocalDate date) {
        return DEFAULT_DATE.equals(getDefault(date));
    }
}
