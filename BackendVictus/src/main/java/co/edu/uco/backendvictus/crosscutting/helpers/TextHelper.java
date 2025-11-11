package co.edu.uco.backendvictus.crosscutting.helpers;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Provides text related helper methods such as trimming, sanitization and pattern validation.
 */
public final class TextHelper {

    public static final String EMPTY = "";
    private static final Pattern SPACE_PATTERN = Pattern.compile("\\s+");

    private TextHelper() {
        // Utility class
    }

    public static boolean isNull(final String value) {
        return ObjectHelper.isNull(value);
    }

    public static String getDefault(final String value, final String defaultValue) {
        return ObjectHelper.getDefault(value, defaultValue);
    }

    public static String getDefault(final String value) {
        return getDefault(value, EMPTY);
    }

    public static String applyTrim(final String value) {
        return getDefault(value).trim();
    }

    public static boolean isEmpty(final String value) {
        return EMPTY.equals(getDefault(value));
    }

    public static boolean isEmptyAfterTrim(final String value) {
        return EMPTY.equals(applyTrim(value));
    }

    public static int length(final String value) {
        return applyTrim(value).length();
    }

    public static boolean lengthIsBetween(final String value, final int min, final int max) {
        return length(value) >= min && length(value) <= max;
    }

    public static String concat(final String... values) {
        final StringBuilder builder = new StringBuilder(EMPTY);
        for (final String value : values) {
            builder.append(getDefault(value));
        }
        return builder.toString();
    }

    public static boolean matches(final String value, final String pattern) {
        return Pattern.matches(pattern, applyTrim(value));
    }

    public static String sanitize(final String rawValue) {
        if (isNull(rawValue)) {
            return null;
        }

        String value = Normalizer.normalize(rawValue, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", EMPTY)
                .replaceAll("[^\\p{Alnum}\\s@._-]", EMPTY);
        value = applyTrim(value);
        value = SPACE_PATTERN.matcher(value).replaceAll(" ");
        return value;
    }
}
